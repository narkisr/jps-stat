(ns jps-stat.jpscore
  (:gen-class)
  (:require
   [table.core :refer [table]]
   [clojure.string :as str]
   [clojure.java.shell :refer (sh)]))

(defn jps []
  (let [{:keys [exit out err]} (sh "jps")]
    (if-not (= exit 0)
      {}
      (map (fn [p] (zipmap [:pid :name] (str/split p #" "))) (str/split out #"\n")))))

(defn jstat
  "https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html"
  [pid]
  (let [{:keys [exit out err]} (sh "jstat" "-gc" pid "1" "1")]
    (when (= exit 0)
      (zipmap
       [:S0C :S1C :S0U :S1U :EC :EU :OC :OU :MC :MU :CCSC :CCSU :YGC :YGCT :FGC :FGCT :GCT]
       (map (fn [n] (BigDecimal. n)) (filter (comp not empty?) (str/split (second (str/split out #"\n")) #" ")))))))

(defn used-heap [m]
  (when m
    (/ (apply + (vals (select-keys m [:S0U :S1U :EU :OU]))) 1024.0)))

(defn statm
  [pid]
  (try
    (let [f (java.io.FileReader. (str "/proc/" pid "/statm"))]
      (zipmap
       [:size :resident :shared :text :lib :data :dt]
       (map (fn [n] (BigDecimal. n)) (str/split (str/trim (slurp f)) #" "))))
    (catch java.io.FileNotFoundException e nil)))

(defn used-ram [m]
  (when m
    (let [{:keys [resident data text shared]} m]
      (/ (+ resident data text shared) 1024.0))))

(defn cpu-use [pid]
  (let [{:keys [exit out]} (sh "ps" "-p" pid "-o" "%cpu")]
    (when (= exit 0)
      (when-let [c (second (str/split (str/trim out) #" "))]
        (BigDecimal. c)))))

(defn usage [{:keys [pid] :as m}]
  (merge m {:heap (used-heap (jstat pid)) :ram (used-ram (statm pid)) :cpu (cpu-use pid)}))

(defn unit [u f]
  (fn [i] (str (f i) u)))

(defn display [m]
  (-> m
      (update :heap (unit "MB" int))
      (update :ram (unit "MB" int))
      (update :cpu (unit "%" float))))

(defn java-top []
  (letfn [(valid? [{:keys [name] :as p}]
            (and (not (some nil? (vals p))) (not (= name "jpscore"))))]
    (let [us (pmap usage (jps))]
      (table (map display (filter valid? us)) :style :borderless))))

(defn -main [& args]
  (java-top)
  (System/exit 0))

(ns jps-stat.jpscore
  (:gen-class)
  (:require
   [cli-matic.core :refer (run-cmd)]
   [table.core :refer [table]]
   [clojure.string :as str]
   [clojure.java.shell :refer (sh)]))

;; (set! *warn-on-reflection* true)

(defn jps []
  (let [{:keys [exit out err]} (sh "jps")]
    (if-not (= exit 0)
      {}
      (map (fn [p] (zipmap [:pid :name] (str/split p #" "))) (str/split out #"\n")))))

(defn jstat
  "https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html"
  [pid]
  (let [{:keys [exit out err]} (sh "jstat" "-gc" "-t" pid "1" "1")]
    (when (= exit 0)
      (zipmap
       [:timestamp :S0C :S1C :S0U :S1U :EC :EU :OC :OU :MC :MU :CCSC :CCSU :YGC :YGCT :FGC :FGCT :GCT]
       (map (fn [^String n] (BigDecimal. n)) (filter (comp not empty?) (str/split (second (str/split out #"\n")) #" ")))))))

(defn used-heap [m]
  (when m
    (/ (apply + (vals (select-keys m [:S0U :S1U :EU :OU]))) 1024.0)))

(defn gc-per [m]
  (when m
    (.divide ^BigDecimal (m :GCT) ^BigDecimal (m :timestamp) java.math.RoundingMode/HALF_EVEN)))

(defn statm
  [pid]
  (try
    (let [f (java.io.FileReader. (str "/proc/" pid "/statm"))]
      (zipmap
       [:size :resident :shared :text :lib :data :dt]
       (map (fn [n] (BigDecimal. ^String n)) (str/split (str/trim (slurp f)) #" "))))
    (catch java.io.FileNotFoundException e nil)))

(defn used-ram [m]
  (when m
    (let [{:keys [resident data text shared]} m]
      (/ (+ resident data text shared) 1024.0))))

(defn cpu-use [pid]
  (let [{:keys [exit out]} (sh "ps" "-p" pid "-o" "%cpu")]
    (when (= exit 0)
      (when-let [c (second (str/split (str/trim out) #"\s+"))]
        (BigDecimal. ^String c)))))

(defn usage [{:keys [pid] :as m}]
  (let [stats (jstat pid)]
    (merge m {:heap (used-heap stats) :gc (gc-per stats) :ram (used-ram (statm pid)) :cpu (cpu-use pid)})))

(defn unit [u f]
  (fn [i] (str (f i) u)))

(defn display [m]
  (-> m
      (update :heap (unit "MB" int))
      (update :ram (unit "MB" int))
      (update :cpu (unit "%" float))
      (update :gc (unit "%" float))))

(defn java-top [_]
  (letfn [(valid? [{:keys [name] :as p}]
            (and (not (some nil? (vals p))) (not (= name "jpscore"))))]
    (let [us (pmap usage (jps))]
      (table (map display (filter valid? us)) :style :borderless))))

(defn raw [{:keys [f]}]
  (letfn [(valid? [{:keys [name] :as p}]
            (and (not (some nil? (vals p))) (not (= name "jpscore"))))]
    (let [us (pmap usage (jps))]
      (doseq [p (if f (filter valid? us) us)]
        (println p)))))

(def cli
  {:app {:command     "jps-stat"
         :description "Java processe stats"
         :version     "0.2.2"}

   :global-opts []

   :commands    [{:command     "top"
                  :description "Run and get the stats for the curret running jvm processes"
                  :opts        []
                  :runs        java-top}

                 {:command     "raw"
                  :description "Run and get the stats printing them in raw edn format"
                  :opts        [{:option "f" :as "Filter" :type :flag}]
                  :runs        raw}]})

(defn -main [& args]
  (try
    (run-cmd args cli)
    (System/exit 0)
    (catch Exception e
      (println e)
      (System/exit 1))))

(comment
  (cpu-use "10021")
  (statm "4336"))

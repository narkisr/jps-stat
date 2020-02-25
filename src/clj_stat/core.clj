(ns clj-stat.core
  (:require
   [clojure.string :as str]
   [clojure.java.shell :refer (sh)]))

(defn jps []
  (let [{:keys [exit out err]} (sh "jps")]
    (map (fn [p] (zipmap [:pid :name] (str/split p #" "))) (str/split out #"\n"))))

(defn jstat
  "https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html"
  [pid]
  (let [{:keys [exit out err]} (sh "jstat" "-gc" pid "1" "1")]
    (zipmap
     [:S0C :S1C :S0U :S1U :EC :EU :OC :OU :MC :MU :CCSC :CCSU :YGC :YGCT :FGC :FGCT :GCT]
     (map (fn [n] (BigDecimal. n)) (filter (comp not empty?) (str/split (second (str/split out #"\n")) #" "))))))

(defn used-heap [m]
  (/ (apply + (vals (select-keys m [:S0U :S1U :EU :OU]))) 1024.0))

(defn ram
  [pid]
  (zipmap
   [:size :resident :shared :text :lib :data :dt]
   (map (fn [n] (BigDecimal. n)) (str/split (str/trim (slurp (java.io.FileReader. (str "/proc/" pid "/statm")))) #" "))))

(defn used-ram [{:keys [resident data text shared]}]
  (/ (+ resident data text shared) 1024.0))

(comment
  (used-heap (jstat "24032"))
  (used-ram (ram "24032")))

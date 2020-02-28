(defproject clj-stat "0.1.0"
  :description ""
  :url ""
  :license {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
      [rm-hull/table "0.7.0"]
      [org.clojure/clojure "1.10.1"]]
  :repl-options {:init-ns clj-stat.core}
  :main clj-stat.core
  :aot [clj-stat.core])

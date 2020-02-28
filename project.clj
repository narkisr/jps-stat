(defproject jps-stat "0.1.0"
  :description "A simple tools for measure java processes heap memory and cpu usage"
  :url ""
  :license {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}

  :dependencies [
      [org.clojure/clojure "1.10.1"]
      [rm-hull/table "0.7.0"]]

  :repl-options {:init-ns jps-stat.jpscore}

  :plugins  [[lein-cljfmt "0.5.6"]
             [lein-ancient "0.6.15" :exclusions [org.clojure/clojure]]
             [lein-tag "0.1.0"]
             [lein-set-version "0.3.0"]]


  ; supporting graalvm --no-fallback
  :jvm-opts ["-Dclojure.compiler.direct-linking=true"]

  :main jps-stat.jpscore

  :aot [jps-stat.jpscore]

  :aliases {
      "travis" ["cljfmt" "check"]
   }
)

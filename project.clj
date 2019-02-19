(defproject jmeter-report-aggregator "0.1.0-SNAPSHOT"
  :description "Jmeter report aggregator"
  
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cljfreechart "0.2.0"]]
  
  :main jmeter-report-aggregator.core
  :aot [jmeter-report-aggregator.core]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

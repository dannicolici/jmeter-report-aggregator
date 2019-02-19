(ns jmeter-report-aggregator.core
  (:gen-class)
  (:require [clojure.xml :as xml]
            [jmeter-report-aggregator.visual :as visual]))

(def endpoint :lb)
(def duration :t)
(def attributes :attrs)

(defn- update-vals [m f]
  (reduce-kv (fn [m k v]
               (assoc m k (f v))) {} m))

(defn- flatten-times [map-vec]
         (map (fn [m] (Integer/parseInt (get m duration))) map-vec))

(defn- to-stats-map [times-list]
  (let [min (apply min times-list)
        max (apply max times-list)
        avg (/ (reduce + 0 times-list) (count times-list))]
    {:min min :avg (int avg) :max max}))

(defn- keep-url-and-time [sample-maps]
  (map #(select-keys (get % attributes) [endpoint duration]) sample-maps))

(defn- compute-stats [groupped-by-url]
  (let [url-times-maps (map #(update-vals % flatten-times) groupped-by-url)
        url-stats-maps (map #(update-vals % to-stats-map) url-times-maps)]
    url-stats-maps))

(defn- aggregate-reports [args]
  (let [xmls (pmap #(xml/parse %) args)
        samples (map #(get %1 :content) xmls)
        url-time-maps (map #(keep-url-and-time %) samples)
        groupped-by-url (map #(group-by endpoint %) url-time-maps)
        stats (compute-stats groupped-by-url)
        merged-stats (apply merge-with (comp flatten list) stats)]
    merged-stats))

(defn- validate [args]
  (if
   (and (nil? args)
        (not (reduce #(and %1 (.exists (clojure.java.io/as-file %2))) true args)))
    (throw (AssertionError. "Cannot read input xml files"))
    args))

(defn -main [& args]
  (try
    (-> args
        (validate)
        (aggregate-reports)
        (visual/chart))
    (catch Exception e 
      (println (str "There's been an issue: " (.getMessage e))))
    (finally (System/exit 0))))

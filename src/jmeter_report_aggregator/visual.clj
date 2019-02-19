(ns jmeter-report-aggregator.visual
  (:require [cljfreechart.core :as c]))


(defn- line-chart [line-data]
  (-> line-data
      (c/make-category-dataset {:group-key :group})
      (c/make-line-chart (str "millis / run (" (java.util.Date.) ")")
                         {:category-title "run"
                          :value-title "millis"})))

(defn- make-chart-data [url-to-statslist-map key]
  (reduce (fn [acc [k v]]
            (into acc (map-indexed 
                       (fn [idx itm] {:group k (inc idx) (key itm)}) v)))
          [] url-to-statslist-map))

(defn- adjust-ui [chart] 
  (let [plot (.getPlot chart)]
    (.setBackgroundPaint plot java.awt.Color/WHITE)
    (.setRangeGridlinePaint plot java.awt.Color/LIGHT_GRAY)
    chart))

(defn- make-chart [url-to-statslist-map key]
  (let [chart (line-chart (make-chart-data url-to-statslist-map key))]
    (c/save-chart-as-file
     (adjust-ui chart)
     (str (name key) ".png") {:width 1920 :height 1400})))

(defn chart [url-to-statslist-map]
  (doall
    (map #(make-chart url-to-statslist-map %) [:min :avg :max])))

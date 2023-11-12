(ns io.dbme.frontend.views.events-visualizer.subs
  (:require
   [io.dbme.frontend.views.events-visualizer.shared :refer [bar-height]]
   [re-frame.core :as rf]))

(rf/reg-sub
  :sound-events.v1/data
  (fn [db]
    (:sound-events.v1/data db)))

(defn rescale-y-axis [window-height events-data]
  (let [y-offset (* -1 (apply min (map :y events-data)))
        max-y (+ y-offset (apply max (map :y events-data)))
        y-ratio (/ (- window-height (* 2 bar-height)) max-y)]
    (map (fn [d] (update d :y #(* y-ratio (+ y-offset %)))) events-data)))

(rf/reg-sub
  :sound-events.v1/rescaled-data
  :<- [:window/data]
  :<- [:sound-events.v1/data]
  (fn [[{:keys [width]} events-data]]
    (rescale-y-axis width events-data)))

(ns io.dbme.frontend.views.events-visualizer.subs
  (:require
   [io.dbme.frontend.views.events-visualizer.sample-data :as sample-data]
   [io.dbme.frontend.views.events-visualizer.shared :refer [bar-height]]
   [io.dbme.utils :refer [format-seconds]]
   [re-frame.core :as rf]))

(rf/reg-sub
 :sound-events.v1/config
 (fn [db] (:sound-events.v1/config db)))

(rf/reg-sub
  :sound-events.v1/data
  (fn [db]
    sample-data/sample
    #_(:sound-events.v1/data db)))

(defn rescale-x-axis
  [x-scale-factor events-data]
  (map (fn [d]
         (-> d
             (update :x #(* x-scale-factor %))
             (update :width #(* x-scale-factor %))))
       events-data))

(defn rescale-y-axis
  [y-scale-factor window-height events-data]
  (let [y-offset (* -1 (apply min (map :y events-data)))
        max-y (+ y-offset (apply max (map :y events-data)))
        y-ratio (/ (- window-height (* 2 bar-height)) max-y)]
    (map (fn [d] (update d :y #(* y-scale-factor y-ratio (+ y-offset %)))) events-data)))

(rf/reg-sub
 :sound-events.v1/events
 :<- [:sound-events.v1/data]
 (fn [data] (:events data)))

(rf/reg-sub
  :sound-events.v1/horizontal-grid-lines
  :<- [:sound-events.v1/config]
  :<- [:window/data]
  :<- [:sound-events.v1/data]
  (fn [[{:keys [y-scale-factor
]} {:keys [height]} data]]
    (rescale-y-axis y-scale-factor
 height (:horizontal-grid-lines (:config data)
                                                   []))))

(rf/reg-sub
  :sound-events.v1/rescaled-data
  :<- [:sound-events.v1/config]
  :<- [:window/data]
  :<- [:sound-events.v1/events]
  (fn [[{:keys [x-scale-factor y-scale-factor]} {:keys [height]} events-data]]
    (->> events-data
         (rescale-y-axis y-scale-factor height)
         (rescale-x-axis x-scale-factor))))

(rf/reg-sub
  :sound-events.v1/visualizer-width
  :<- [:sound-events.v1/rescaled-data]
  (fn [events-data]
    (->> events-data (map (fn [{:keys [x width]}] (+ x width)))
         (apply max))))

(rf/reg-sub
 :sound-events.v1/temporal-grid-lines ;; vertical grid
 :<- [:sound-events.v1/config]
 :<- [:sound-events.v1/data]
 :<- [:sound-events.v1/visualizer-width]
 (fn [[{:keys [grid-lines-seconds x-scale-factor]} data viz-width]]
   (let [px-per-second (:px-per-second (:config data) [])
         grid-lines-gap (* px-per-second grid-lines-seconds)
         grid-x-range (range 0 (+ viz-width grid-lines-gap) grid-lines-gap)]
     (->> grid-x-range
          (map-indexed (fn [i t] {:label (format-seconds (* i (/ grid-lines-seconds
                                                                 x-scale-factor)))
                                  :x t}))
          (drop 1)
          (drop-last 1)))))

(ns io.dbme.frontend.views.events-visualizer.v1
  "Sound events visualizer - piano roll style"
  (:require
   [io.dbme.frontend.views.events-visualizer.shared :refer [bar-height]]
   [io.dbme.frontend.views.events-visualizer.subs :refer [rescale-y-axis]]
   [re-frame.core :as rf]))

(defn main []
  (let [events-data @(rf/subscribe [:sound-events.v1/rescaled-data])
        x-grid-lines @(rf/subscribe [:sound-events.v1/horizontal-grid-lines])
        width* (->> events-data (map (fn [{:keys [x width]}] (+ x width)))
                    (apply max))
        width (or width* "100vh")
        data (rescale-y-axis (:height @(rf/subscribe [:window/data])) events-data)]
    [:div {:class "p-4 bg-black h-screen"
           :style {:width (+ 200 width)}}
     [:div {:class "relative bg-black h-full text-white"
            :style {:width width}}
      (if (seq data)
        (concat (map (fn [{:keys [y label]}]
                       [:div {:class "absolute"
                              :style {:background-color "yellow" :width "100%" :height 1 :top y :left 0}}])
                     x-grid-lines)
                (map-indexed (fn [i {:keys [color x y width]}]
                               [:div {:key [i x y color]
                                      :class "absolute flex items-center border border-1 border-black pl-1"
                                      :style {:line-height 1
                                              :bottom y
                                              :left x
                                              :background-color color
                                              :width width
                                              :height bar-height}}
                                [:span {:style {:filter "drop-shadow(0px 0px 3px black)" }}
                                 "event"]])
                             data))
        "No data")]]))

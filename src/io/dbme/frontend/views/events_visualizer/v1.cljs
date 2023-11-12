(ns io.dbme.frontend.views.events-visualizer.v1
  "Sound events visualizer - piano roll style"
  (:require
   [io.dbme.frontend.views.events-visualizer.shared :refer [bar-height]]
   [io.dbme.frontend.views.events-visualizer.subs :refer [rescale-y-axis]]
   [re-frame.core :as rf]))

(defn main []
  (let [events-data @(rf/subscribe [:sound-events.v1/rescaled-data])
        width* (->> events-data (map (fn [{:keys [x width]}] (+ x width)))
                    (apply max))
        width (or width* "100vh")
        data (rescale-y-axis (:height @(rf/subscribe [:window/data])) events-data)]
    [:div {:class "p-4 bg-black h-screen"
           :style {:width (+ 200 width)}}
     [:div {:class "relative bg-black h-full text-white"
            :style {:width width}}
      (if (seq data)
        (map (fn [{:keys [color x y width]}]
               [:div {:key [x y color]
                      :class "absolute flex items-center border border-1 border-black"
                      :style {:line-height 1
                              :bottom y
                              :left x
                              :background-color color
                              :width width
                              :height bar-height}} "event"])
             data)
        "No data")]]))

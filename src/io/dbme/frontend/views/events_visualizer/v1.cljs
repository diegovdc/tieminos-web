(ns io.dbme.frontend.views.events-visualizer.v1
  "Sound events visualizer - piano roll style"
  (:require
   [io.dbme.frontend.views.events-visualizer.shared :refer [bar-height]]
   [io.dbme.frontend.views.events-visualizer.subs :refer [rescale-y-axis]]
   [re-frame.core :as rf]))

(defn main
  []
  (let [events-data @(rf/subscribe [:sound-events.v1/rescaled-data])
        x-grid-lines @(rf/subscribe [:sound-events.v1/horizontal-grid-lines])
        y-grid-lines @(rf/subscribe [:sound-events.v1/temporal-grid-lines])
        width* @(rf/subscribe [:sound-events.v1/visualizer-width])
        width (or width* "100vh")
        {:keys [y-scale-factor]} @(rf/subscribe [:sound-events.v1/config])
        #_ #_events-data (rescale-y-axis y-scale-factor
                                    (:height @(rf/subscribe [:window/data]))
                                    events-data)]
    [:div {:class "p-4 bg-black"
           :style {:width (+ 200 width)
                   :height "100vh"}}
     [:div {:class "relative bg-black h-full text-white"
            :style {:width width}}
      (if (seq events-data
)
        (concat (map
                  (fn [{:keys [x label]}]
                    [:div {:key label
                              :class "absolute"
                              :style {:background-color "rgba(255,255,255,0.5)"
                                      :width 1
                                      :height "calc(100vh - 10px)"
                                      :top 0
                                      :left x}}
                        [:span {:title label
                                :class "absolute"
                                :style {:top 0
                                        :transform "translate(-50%, -20px)"}}
                         label]])
                  y-grid-lines)
                (map (fn [{:keys [y label]}]
                       [:div {:key label
                              :class "absolute"
                              :style {:background-color "yellow"
                                      :width "100%"
                                      :height 1
                                      :bottom y
                                      :left 0}}
                        [:span {:class "fixed"
                                :style {:bottom (+ y 12) :left 0}}
                         label]])
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
                             events-data
))
        "No data")]]))

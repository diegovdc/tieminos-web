(ns io.dbme.frontend.components
  (:require [re-frame.core :as rf]))

(defn calculate-elapsed-percentage
  [current-time start-time dur-ms]
  (let [end-time (+ start-time dur-ms)
        elapsed (* 100 (/ (- current-time start-time)
                          (- end-time start-time)))]
    elapsed))

(defn remaining-time-bar
  [{:keys [color start-time dur-ms]}]
  (let [time* @(rf/subscribe [:app/ticker.time])
        percentage (calculate-elapsed-percentage time* start-time dur-ms)
        width (max 0 (int (- 100 percentage)))]
    [:div {:style {:width (str width "%")
                   :background-color color
                   :height 20}}]))

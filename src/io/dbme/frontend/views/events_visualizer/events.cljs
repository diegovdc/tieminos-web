(ns io.dbme.frontend.views.events-visualizer.events
  (:require
   [re-frame.core :as rf]))


(rf/reg-event-db
 :sound-events.v1/set-x-scale-factor
 (fn [db [_ scale-factor]]
   (assoc-in db [:sound-events.v1/config :x-scale-factor] scale-factor)))

(rf/reg-event-db
  :sound-events.v1/set-y-scale-factor
  (fn [db [_ scale-factor]]
    (assoc-in db [:sound-events.v1/config :y-scale-factor] scale-factor)))

(comment
  (rf/dispatch [:sound-events.v1/set-y-scale-factor 1])
  (rf/dispatch [:sound-events.v1/set-y-scale-factor 0.5])
  (rf/dispatch [:sound-events.v1/set-x-scale-factor 1])
  (rf/dispatch [:sound-events.v1/set-x-scale-factor 0.1])

  )

(ns io.dbme.frontend.subs
  (:require [re-frame.core :as rf]
            [io.dbme.frontend.views.events-visualizer.subs]))

(rf/reg-sub
 :app/data
 (fn [db]
   (get-in db [:data])))

(rf/reg-sub
 :app/connected
 (fn [db _]
   (true? (get-in db [:connected]))))

(rf/reg-sub
 :current-route
 (fn [db]
   (:current-route db)))

(rf/reg-sub
  :window/data
  (fn [db]
    (:window/data db)))



(comment
  @(rf/subscribe [:current-route])
  )

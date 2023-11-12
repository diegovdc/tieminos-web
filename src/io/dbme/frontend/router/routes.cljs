(ns io.dbme.frontend.router.routes
  (:require
   [io.dbme.frontend.views.home :as home]
   [io.dbme.frontend.views.events-visualizer.v1 :as events-visualizer.v1]))

(def routes
  [["/"
    {:name :routes/home
     :view #'home/main
     :controllers [{ ;; Do whatever initialization needed for home page
                    ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                    :start (fn [_params])
                    ;; Teardown can be done here.
                    :stop  (fn [_params])}]}]
   ["/events-visualizer"
    {:name :routes/events-visualizer
     :view #'events-visualizer.v1/main
     :controllers [{ ;; Do whatever initialization needed for home page
                    ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                    :start (fn [_params])
                    ;; Teardown can be done here.
                    :stop  (fn [_params])}]}]])

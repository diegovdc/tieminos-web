(ns io.dbme.frontend.router.routes
  (:require
   [io.dbme.frontend.views.events-visualizer.v1 :as events-visualizer.v1]
   [io.dbme.frontend.views.garden-earth.tuning-trainer :as garden-earth-tuning]
   [io.dbme.frontend.views.gusano-cuantico-bardo.main :as gusano-cuantico-bardo]
   [io.dbme.frontend.views.in-volcanic-times.main :as in-volcanic-times]
   [re-frame.core :as rf]))

(defn home
  []
  [:div "hola"])

(def routes
  [["/"
    {:name :routes/home
     :view (fn []
             [:div (map (fn [[href {:keys [name]}]]
                          [:div {:key href
                                 :style {:margin-bottom 16}}
                           [:a {:href href
                                :style {:text-decoration "underline"
                                        :color "lightgreen"}}
                            (str name)]])
                        routes)])
     :controllers [{;; Do whatever initialization needed for home page
                    ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                    :start (fn [_params])
                    ;; Teardown can be done here.
                    :stop  (fn [_params])}]}]
   ["/events-visualizer"
    {:name :routes/events-visualizer
     :view #'events-visualizer.v1/main
     :controllers [{;; Do whatever initialization needed for home page
                    ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                    :start (fn [_params])
                    ;; Teardown can be done here.
                    :stop  (fn [_params])}]}]
   ["/gusano-cuantico-bardo"
    {:name :routes/gusano-cuantico-bardo
     :view #'gusano-cuantico-bardo/main
     :controllers [{;; Do whatever initialization needed for home page
                    ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                    :start (fn [_params]
                             (rf/dispatch [:app/start-ticker 50]))
                    ;; Teardown can be done here.
                    :stop  (fn [_params])}]}]
   ["/garden-earth/tuning"
    {:name :routes/garden-earth-tuning
     :view #'garden-earth-tuning/main
     :controllers [{;; Do whatever initialization needed for home page
                    ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                    :start (fn [_params])
                    ;; Teardown can be done here.
                    :stop  (fn [_params])}]}]
   ["/in-volcanic-times"
    {:name :routes/in-volcanic-times
     :view #'in-volcanic-times/main
     :controllers [{;; Do whatever initialization needed for home page
                    ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
                    :start (fn [_params]
                             (rf/dispatch [:app/start-ticker 50]))
                    ;; Teardown can be done here.
                    :stop  (fn [_params])}]}]])

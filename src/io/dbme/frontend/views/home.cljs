(ns io.dbme.frontend.views.home
  (:require
   [cljs.reader :as reader]
   #_[io.dbme.frontend.router.routes :refer [routes]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn connect-button []
  [:input {:type     :button
           :value    "Connect"
           :disabled @(rf/subscribe [:app/connected])
           :on-click #(rf/dispatch [:app/connect])}])

(defn input-field []
  (let [state (r/atom {:text ""
                       :error false})]
    (fn []
      (cond-> [:div
               [:textarea {:id "text-field"
                           :rows 5
                           :value (:text @state)
                           :on-change #(swap! state assoc :text (.. % -target -value))}]
               [:input {:type     :button
                        :value    "Send"
                        :disabled (not @(rf/subscribe [:app/connected]))
                        :on-click #(try
                                     (rf/dispatch [:app/send (reader/read-string (:text @state))])
                                     (swap! state assoc :text "" :error false)
                                     (catch js/Error _
                                       (swap! state assoc :error true)))}]]
        (:error @state) (conj [:div {:style {:color :red}} "Input not valid edn"])))))

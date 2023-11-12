(ns io.dbme.frontend.views.home
  (:require
   [cljs.reader :as reader]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reitit.frontend.easy :as rtfe]))

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

(defn main []
  [:div
   [connect-button]
   [:a {:href (rtfe/href :routes/events-visualizer)} "Events visualizer"]
   [:h2
    (str @(rf/subscribe [:app/data]))]
   [input-field]])

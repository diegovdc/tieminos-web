(ns io.dbme.frontend.views.in-volcanic-times.main
  (:require
   [io.dbme.utils :refer [format-seconds]]
   [re-frame.core :as rf]))

(defn section
  []
  (let [{:keys [dur/minutes start-time] :as  section-data} @(rf/subscribe [:in-volcanic-times/section-data])
        {:keys [piece/running?] :as piece-data} @(rf/subscribe [:in-volcanic-times/piece-data])
        section-end-time (+ (* 1000 60 minutes) start-time)
        time* @(rf/subscribe [:app/ticker.time])
        remaining-time-secs (-> (- section-end-time time*)
                                (/ 1000)
                                int
                                format-seconds)
        piece-elapsed-time (-> (- time* (:piece/start-time piece-data 0))
                               (/ 1000)
                               int
                               format-seconds)]
    (if-not running?
      [:div {:style {:font-size 80 :color "yellow"}} "In Volcanic Temporality" [:small " (Not Playing)"]]
      [:div {:style {}}
       [:h1 {:style {:display "flex"
                     :align-items "center"
                     :justify-content "space-between"
                     :font-size 36
                     :font-weight "bold"}}
        [:span {:style {:display "flex" :align-items "center" :gap 16}} (:name section-data "N/A")

         [:span {:style {:font-size 48 :color "deeppink"}} remaining-time-secs]
         [:small {:style {:font-size 18}} "(Remaining)"]]
        [:span {:style {:font-size 80 :color "yellow"}} piece-elapsed-time ]]])))

(defn general-state
  []
  (let [{:keys [exp/pedal-1 exp/btn-2]} @(rf/subscribe [:in-volcanic-times/general-state])]
    [:div {:style {:display "flex" :gap 24}}
     [:div ":exp/pedal-1 " pedal-1]
     [:div ":exp/btn-2 " btn-2]]))

(defn section-handlers
  []
  (let [handlers @(rf/subscribe [:in-volcanic-times/section-handlers])]
    [:section {:id "handlers"}
     (map
       (fn [[k {:keys [description]}]]
         [:p {:style {:display "flex" :gap 19 :font-size 36}}
          [:b k] [:span description]])
       handlers)]))


(defn main
  []
  [:div  {:style {:padding 16}}
   (section)
   [:div {:style {:margin-bottom 20}}
    (general-state)]
   (section-handlers) ])

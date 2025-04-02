(ns io.dbme.frontend.views.in-volcanic-times.main
  (:require
   [io.dbme.utils :refer [format-seconds]]
   [re-frame.core :as rf]))

(defn section-description
  "Section description, can be a string or [string]"
  [description]
  (let [styles {:class "text-3xl text-green-300 bg-white bg-opacity-10 p-1 rounded-sm"}]
    (if (string? description)
      [:p styles description]
      [:div (map (fn [d] [:p styles d]) description)])))

(defn section-handlers
  []
  (let [general-state @(rf/subscribe [:in-volcanic-times/general-state])
        handlers @(rf/subscribe [:in-volcanic-times/section-handlers])]
    [:section {:id "handlers"}
     (map
      (fn [[k {:keys [description]}]]
        [:p {:class "flex gap-2 items-center"}
         [:b {:class (str "text-3xl "
                          (when-not (or (nil? (-> general-state k))
                                        (zero? (-> general-state k)))
                            " text-red-500"))}
          k]
         [:span {:class "text-2xl"} description]])
      handlers)]))

(defn general-state
  []
  (let [general-state (-> @(rf/subscribe [:in-volcanic-times/general-state])
                          (dissoc :piece/running?)
                          #_(->> (sort-by first)))]
    [:div {:style {:display "flex" :gap 24}}
     (map (fn [[k v]]
            [:div [:span {:class "text-nowrap"} k] " " v])
          general-state)]))

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
      [:div {:class "text-8xl text-yellow-100"}
       [:h1 "En una temporalidad geológica"]
       [:div {:class "p-4 text-xl"} (general-state)]]
      [:div {:style {}}
       [:h1 {:style {:display "flex"
                     :align-items "center"
                     :justify-content "space-between"
                     :font-size 36
                     :font-weight "bold"}}
        [:span {:style {:display "flex" :align-items "center" :gap 16}} (:name section-data "N/A")

         [:span {:style {:font-size 48 :color "deeppink"}} remaining-time-secs]
         [:small {:style {:font-size 18}} "(Remaining)"]]
        [:span {:style {:font-size 80 :color "yellow"}} piece-elapsed-time]]
       [:div #_{:class "flex"}
        (section-description (:description section-data))
        [:div {:class "p-4"}
         (general-state)]]])))


(defn main
  []
  [:div  {:style {:padding 16}}
   (section)
   [:div {:class "py-4"}
    (section-handlers)]])

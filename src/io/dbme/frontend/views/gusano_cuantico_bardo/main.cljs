(ns io.dbme.frontend.views.gusano-cuantico-bardo.main
  (:require
   [io.dbme.frontend.components :refer [remaining-time-bar]]
   [io.dbme.frontend.utils :refer [round2]]
   [re-frame.core :as rf]))

(defn rec-display-row-head
  []
  [:div {:style {:padding-top 42 :font-size 36}}
   [:p {:style {:margin-bottom 30}} "Dur: "]
   [:p "Pulse:"]])

(defn rec-display
  [{:keys [on? label dur pulse start-time]}]

  [:div [:div {:style  {:display "flex" :gap 16
                        :align-items "center"}}
         [:div {:style {:background-color (if on? "red" "black")
                        :border-radius "100%"
                        :border "3px solid red"
                        :height 40
                        :width 40}}]
         label]
   (remaining-time-bar {:color "red"
                  :start-time start-time
                  :dur-ms (* 1000 dur)} )
   [:div {:style {:font-size 48 :color "yellow"}}
    [:p (when dur (str dur "\""))]
    [:p pulse]]])

(defn rec-section [rec-data]
  [:div {:style {:margin-bottom 20}}
   [:h2 {:style {:font-size 36 :text-align "center" :text-decoration "underline"}} "Recording"]
   [:div {:style {:font-size 28 :display "flex" :justify-content "center" :gap 40}}
    (rec-display-row-head)
    (rec-display (assoc (:guitar rec-data) :label "Guitar"))
    (rec-display (assoc (:mic-1 rec-data) :label "Micro 1"))
    (rec-display (assoc (:mic-2 rec-data) :label "Micro 2"))]])

(defn play-icon
  []
  [:svg {:xmlns "http://www.w3.org/2000/svg"
         :viewBox "0 0 100 100"
         :width "80"
         :height "80"}
   [:polygon {:points "30,20 70,50 30,80" :fill "lightgreen"}]])

(defn cloud-display-rows
  []
  [:div
   [:div {:style {:height 80}}]
   [:div {:style {:font-size 48 :color "yellow"}}
    [:p [:small {:style {:color "white"}} "Amp: "]]
    [:p [:small {:style {:color "white"}} "Env: "]]
    [:p [:small {:style {:color "white"}} "Rhy: "]]
    [:p [:small {:style {:color "white"}} "Lib Size: "]]]])

(defn cloud-display [label {:keys [on? amp env rhythm sample-lib-size]}]
  [:div #_{:style {:width "50%"}}
   [:h2 {:style {:font-size 48 :color "#fbb847"}}
    [:div {:style {:display "flex"  :align-items "center"}} label (when on? (play-icon))]]
   [:div {:style {:font-size 48
                  :color "yellow"}}
    [:p (when amp [:span
                   (round2 2 amp)
                   [:small "db"]])]
    [:p env]
    [:p rhythm]
    [:p sample-lib-size]]])

(defn clouds-section [clouds-data]
  [:div {:style {:margin-bottom 20}}
   [:h2 {:style {:font-size 36 :text-align "center" :text-decoration "underline"}} "Algo 2.2.9 Clouds"]
   [:div {:style {:font-size 28 :display "flex" :justify-content "center" :gap 40}}
    (cloud-display-rows)
    (cloud-display "Diego" (:diego clouds-data))
    (cloud-display "Milo" (:milo clouds-data))]])

(defn main
  []
  (let [live-state @(rf/subscribe [:gusano-cuantico-bardo/live-state])]
    [:div {:style {:padding 16}}
     [:h1 {:style {:font-size 48 :text-align "center"}} "Gusano Cuántico / Bardó"]
     (rec-section (:rec live-state))
     (clouds-section (:algo-2.2.9-clouds live-state))]))

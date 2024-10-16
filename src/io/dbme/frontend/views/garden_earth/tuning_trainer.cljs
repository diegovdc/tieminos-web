(ns io.dbme.frontend.views.garden-earth.tuning-trainer
  (:require
   [io.dbme.frontend.utils :refer [round2]]
   [re-frame.core :as rf]))


(defn live-state-square [{:keys [label on? content]}]
  [:h2 {:style {:display "flex"
                :align-items "center"
                :font-size 30
                :color "#ffd27f"
                :gap 16}}
   label
   (let [color (if on? "lightgreen" "red")]
     [:div {:style {:background-color (if on? color "black")
                    :border-radius "100%"
                    :border (str "3px solid " color)
                    :height 30
                    :width 30}}])
   [:div {:style {:font-size 24 :color "white"}} content]])

(defn note-tuning*
  [{:keys [label diff-cents last-sets]}]
  (let [width 600
        ;; diff-cents 10
        cents-range 100
        ;; 50 is for 50 cents
        marker-position (-> diff-cents
                            (min 50)
                            (max -50)
                            (*  (/ width 2 (/ cents-range 2)))
                            (->> (round2 1)))]
    [:div
     [:div {:style {:width width
                    :text-align "center"
                    :font-size 40}}
      [:h2 label]
      [:h3 (int diff-cents)]]
     [:div
      [:div {:style {:border "1px solid white"
                     ;; :background-color "#b15aef"
                     :width width
                     :position "relative"
                     :height 50}}

       [:div {:style {:height "100%"
                      :width 6
                      :position "absolute"
                      :left "50%"
                      :background-color "white"
                      :transform "translateX(-50%)"}}]
       [:svg {:xmlns "http://www.w3.org/2000/svg"
              :viewBox "0 0 100 100"
              :style {:width 50
                      :height 50
                      :position "absolute"
                      :left (str "calc(50% + "  marker-position "px)")
                      :transform "translateX(-50%)"}}
        [:polygon {:style {:fill "rgb(102 250 232)"
                           :stroke "black"
                           :stroke-width 6}
                   :points "50,15 90,85 10,85"}]]]]
     #_[:div {:style {:padding-top 16
                      :font-size 30}}
        (map (fn [set*] [:p (->> set*
                                 sort
                                 (str/join "."))])
             last-sets)]]))

(defn note-tuning []
  (let [note-tuning-data @(rf/subscribe [:garden-earth.tuning/note-tuning])]
    (note-tuning* note-tuning-data)))

(defn main
  []
  (let [fingering @(rf/subscribe [:garden-earth.tuning/fingering])
        live-state @(rf/subscribe [:garden-earth/live-state])]
    [:div {:style {:padding 16}}
     [:div {:style {:font-size 30}}
      [:pre fingering]]
     (note-tuning)
     [:div#live-state {:style {:padding-top 32}}
      #_[:h2 {:style {:font-size 48 :text-align :center}}
       (str "S." (:section live-state 0))]
      (live-state-square {:label "Arp"
                          :on? (:arp.refrain/on? live-state)
                          :content [:div
                                    (map (fn [s] [:p s])
                                         (:arp/harmony-strs live-state))
                                    [:div (str "cell: " (:arp/pattern live-state))
                                     (when (:arp.refrain/on? live-state) (str ":: " (:arp/pattern-str live-state)))]]})

      (live-state-square {:label "Harmonizer"
                          :on? (:harmonizer/on? live-state)
                          :content (:harmonizer/harmony-str live-state)})]]))

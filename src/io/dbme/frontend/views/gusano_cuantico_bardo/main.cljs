(ns io.dbme.frontend.views.gusano-cuantico-bardo.main
  (:require
   [clojure.string :as str]
   [io.dbme.frontend.components :refer [remaining-time-bar]]
   [io.dbme.frontend.utils :refer [round2]]
   [re-frame.core :as rf]))

#_(defn rec-display-row-head
  []
  [:div {:style {:padding-top 42 :font-size 16}}
   [:p {:style {:margin-bottom 30}} "Dur: "]
   [:p "Pulse:"]])

(defn rec-display
  [{:keys [on? label dur _pulse last-rec-timestamp]}]

  [:div {:class "w-[100px]"}
   [:div 
    label
    [:div {:style {:background-color (if on? "red" "black")
                   :border-radius "100%"
                   :border "3px solid red"
                   :height 40
                   :width 40}}]]
   (remaining-time-bar
     {:color "red"
      :start-time last-rec-timestamp
      :dur-ms (* 1000 dur)})
   [:div {:style {:font-size 30 :color "yellow"}}
    [:p (when dur (str dur "\""))]
    #_[:p pulse]]])

(defn rec-section [rec-data]
  [:div {:style {:margin-bottom 20}}
   [:h2 {:style {:font-size 36 :text-align "center" :text-decoration "underline"}} "Rec"]
   [:div {:style {:font-size 28 :display "flex"  :gap 8}}
    #_(rec-display-row-head)
    (rec-display (assoc (:guitar rec-data) :label "Gtr."))
    (rec-display (assoc (:mic-1 rec-data) :label "Mic1"))
    (rec-display (assoc (:mic-2 rec-data) :label "Mic2"))]])

(defn play-icon
  []
  [:svg {:xmlns "http://www.w3.org/2000/svg"
         :viewBox "0 0 100 100"
         :width "80"
         :height "80"}
   [:polygon {:points "30,20 70,50 30,80" :fill "lightgreen"}]])

#_(defn cloud-display-rows
  []
  [:div
   [:div {:style {:height 44}}]
   [:div {:style {:font-size 28 :line-height 1 :color "yellow"}}
    [:p {:class "text-white"} "Synth: "]
    [:p {:class "text-white mb-4"} "Hy: "]
    [:p {:class "text-white"} "Smpls: "]
    [:p {:class "text-white"} "Amp: "]
    [:p {:class "text-white"} "Env: "]
    [:p {:class "text-white"} "Rhy: "]
    [:p {:class "text-white"} "Lib Size: "]]])

(defn cloud-display
  [label {:keys [on?
                 amp
                 env
                 rhythm
                 active-synth
                 active-banks
                 sample-lib-size
                 harmonic-range
                 harmonic-speed
                 harmony
                 reaper.send/reverb]}]

  [:div
   [:h2 
    [:div {:style {:display "flex"  :align-items "center"}} label (when on? (play-icon))]]
   [:div {:style {:font-size 32
                  :line-height 1
                  :color "yellow"}}
    [:p.mb-1 active-synth]
    [:pre.text-xl.mb-1
     (if harmony (drop 1 (str/replace (str harmony) #"meta" "m")) "no-harmony")
     [:span " Δ " harmonic-speed] " "
     (:low harmonic-range) "/"
     (:high harmonic-range)]
    (when (seq active-banks)
      [:p
       [:small.text-sm " (bnks)"]
       (->> active-banks sort (str/join ","))])
    [:p [:small.text-sm " (amp)"] (if amp
                                    [:span (round2 2 amp) [:small "db"]]
                                    [:span "-Inf" [:small "db"]])]
    [:p [:small.text-sm " (env)"] env]
    [:p [:small.text-sm " (rhythm)"] rhythm]
    [:p [:small.text-sm " (rev)"] (round2 2 (:clean reverb)) "/" (round2 2 (:processes reverb))]
    [:p [:small.text-sm " (lib)"] sample-lib-size]]])

(defn clouds-section [clouds-data]
  [:div {:style {:margin-bottom 20}}
   [:h2 {:style {:font-size 36 :text-align "center" :text-decoration "underline"}} "Algo 2.2.9 Clouds"]
   [:div {:style {:font-size 28 :display "flex" :justify-content "center" :gap 20}}
    #_(cloud-display-rows)
    (cloud-display "Diego" (:diego clouds-data))
    (cloud-display "Milo" (:milo clouds-data))]])

(defn main
  []
  (let [live-state @(rf/subscribe [:gusano-cuantico-bardo/live-state])]
    [:div {:style {:padding 16}}
     [:h1 {:class "text-2xl text-center text-green-300"} "Gusano Cuántico / Bardó"]
     [:div {:class "flex gap-8"}
      (rec-section (:rec live-state))
      (clouds-section (:algo-2.2.9-clouds live-state))]]))

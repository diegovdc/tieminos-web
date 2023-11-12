(ns io.dbme.frontend.events
  (:require [re-frame.core :as rf]
            [io.dbme.frontend.socket :as socket]))

(rf/reg-event-db
 :app/init
 (fn [_ _]
   {:connected false}))

(rf/reg-event-db
 :app/connected
 (fn [db [_ value]]
   (assoc-in db [:connected] value)))

;; TODO maybe refactor this and make the event handler dispatch different events
(defmulti set-data (fn [_db data] (:websocket/route data :default)))

(defmethod set-data :default
  [_db data]
  {:fx [[:log/error {:data data
                     :message "No `:websocket/route` no `data`"}]]})

(defmethod set-data :hacia-un-nuevo-universo/score
  [db data]
  {:db (assoc db
              :hacia-un-nuevo-universo/score
              (dissoc data :websocket/route))})

(rf/reg-event-fx
  :app/set-data
  (fn [{:keys [db]} [_ data]]
    (set-data db data)))

(rf/reg-event-fx
 :app/connect
 (fn [_ _]
   (socket/start!)
   {:dispatch [:app/connected true]}))

(rf/reg-event-fx
 :app/send
 (fn [_ [_ data]]
   (socket/send-data data)
   ;; TODO
   {}))

(rf/reg-fx
  :log/error
  (fn [{:keys [message data]}]
    (js/console.error message (clj->js data))))

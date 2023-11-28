(ns io.dbme.frontend.events
  (:require
   [io.dbme.frontend.db :refer [initial-db]]
   [io.dbme.frontend.socket :as socket]
   [io.dbme.frontend.views.events-visualizer.events]
   [re-frame.core :as rf]))

(defonce set-window-data
  (fn []
    (rf/dispatch [:window/set-data
                  {:width js/window.innerWidth
                   :height js/window.innerHeight}])))

(rf/reg-fx
 :window/setup-listeners
 (fn [_]
   (.removeEventListener js/window "resize" set-window-data)
   (.addEventListener js/window "resize" set-window-data)))

(rf/reg-event-fx
 :window/set-data
 (fn [{:keys [db]} [_ window-data]]
   {:db (update db :window/data merge window-data)}))

(rf/reg-event-fx
  :app/init
  (fn [{:keys [db]} _]
    (when-not (:db/initialized? db)
      {:db initial-db
       :fx [[:window/setup-listeners]
            [:dispatch [:window/set-data
                        {:width js/window.innerWidth
                         :height js/window.innerHeight}]]]})))

(rf/reg-event-db
 :app/connected
 (fn [db [_ value]]
   (assoc-in db [:connected] value)))

;; TODO maybe refactor this and make the event handler dispatch different events
(defmulti set-data (fn [_db data] (:websocket/route data :default)))

(defmethod set-data :default
  [_db data]
  (when data ;; if there is no data the event is probably a ping
    {:fx [[:log/error {:data data
                       :message "No `:websocket/route` no `data`"}]]}))

(defmethod set-data :hacia-un-nuevo-universo/score
  [db data]
  {:db (assoc db
              :hacia-un-nuevo-universo/score
              (:data data))})

(defmethod set-data :sound-events.v1/visualize
  [db data]
  {:db (assoc db
              :sound-events.v1/data
              (:data data))})

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

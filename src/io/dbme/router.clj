(ns io.dbme.router
  (:require
   [clojure.edn :as edn]
   [compojure.core :as comp :refer (defroutes GET POST)]
   [compojure.route :as route]
   [io.dbme.handlers :as handlers]
   [io.dbme.socket :as socket :refer [send-data]]
   [ring.middleware.defaults]
   [ring.util.request :refer [body-string]]
   [ring.util.response :as response]
   [taoensso.sente :as sente]))

(defn post-visualizer-data
  [ring-req]
  (println ring-req)
  (send-data {:websocket/route :sound-events.v1/visualize
              :data (-> ring-req
                        body-string
                        edn/read-string)}))

(defn forward-data
  [websocket-route ring-req]
  (println ring-req)
  (send-data {:websocket/route websocket-route
              :data (-> ring-req
                        body-string
                        edn/read-string)}))

;; FIXME when adding new routes it seems like it is necessary to restart the repl
(defroutes ring-routes
  (GET "/" _ring-req (response/content-type (response/resource-response "index.html") "text/html"))
  (GET "/ping"  _ring-req (response/response {:status 201} ))
  (GET  "/chsk"  ring-req (socket/ring-ajax-get-or-ws-handshake ring-req))
  (POST "/chsk"  ring-req (socket/ring-ajax-post                ring-req))
  (POST "/visualizer-data"  ring-req
    (do
      (println "PV" (:body ring-req))
      (post-visualizer-data ring-req)

      (response/response {:status 201} )))
  (POST "/hacia-un-nuevo-universo-score"  ring-req
    (do
      (println ring-req)
      (response/response {:status 201})))
  (POST "/gusano-cuantico-bardo"  ring-req
    (do
      (println ring-req)
      (forward-data :gusano-cuantico-bardo/live-state ring-req)
      (response/response {:status 201})))
  (POST "/garden-earth/tuning/fingering"  ring-req
    (do
      (println ring-req)
      (forward-data :garden-earth.tuning/fingering ring-req)
      (response/response {:status 201} )))
  (POST "/garden-earth/tuning/reference-scale"  ring-req
    (do
      (println ring-req)
      (forward-data :garden-earth.tuning/reference-scale ring-req)
      (response/response {:status 201} )))
  (POST "/garden-earth/tuning/note-tuning"  ring-req
    (do
      (println ring-req)
      (forward-data :garden-earth.tuning/note-tuning ring-req)
      (response/response {:status 201} )))
  (POST "/garden-earth/live-state"  ring-req
    (do
      (println ring-req)
      (forward-data :garden-earth/live-state ring-req)
      (response/response {:status 201} )))
  (POST "/in-volcanic-times/live-state"  ring-req
    (do
      (println ring-req)
      (forward-data :in-volcanic-times/live-state ring-req)
      (response/response {:status 201} )))
  (route/resources "/")
  (route/not-found (response/content-type (response/resource-response "index.html") "text/html")))

(defonce router_ (atom nil))

(defn stop! []
  (when-let [stop-fn @router_] (stop-fn)))

(defn start! []
  (stop!)
  (println "starting-router")
  (reset! router_ (sente/start-server-chsk-router! socket/ch-chsk handlers/event-msg-handler)))


(comment
  (-> @router_)
  (reset! router_ nil)
  (stop!)
  (start!)
  )

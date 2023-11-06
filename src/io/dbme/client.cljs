(ns io.dbme.client
  (:require
   [io.dbme.frontend.router.events]
   [cljs.reader]
   [io.dbme.frontend.events]
   [io.dbme.frontend.router.routes :as routes]
   [io.dbme.frontend.router.setup :as router.setup]
   [io.dbme.frontend.socket :as socket]
   [io.dbme.frontend.subs]
   [lambdaisland.glogi :as log]
   [lambdaisland.glogi.console :as glogi-console]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.dom]))

(glogi-console/install!)

(log/set-levels
 {:glogi/root   :info    ;; Set a root logger level, this will be inherited by all loggers
  ;; 'my.app.thing :trace   ;; Some namespaces you might want detailed logging
  })

(defn root-component []
  (let [current-route @(rf/subscribe [:current-route])]
    [:div
     (when current-route
       [(-> current-route :data :view)])]))

(defn ^:dev/after-load start! []
  (rf/clear-subscription-cache!)
  (rf/dispatch [:app/init])
  (let [root-el (.getElementById js/document "app")
        _router (router.setup/init! routes/routes)]
    (reagent.dom/unmount-component-at-node root-el)
    (reagent.dom/render [root-component] root-el)))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (start!))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop")
  (socket/stop-router!))

(ns io.dbme.frontend.router.routes
  (:require
   [io.dbme.frontend.views.home :as home]
   [re-frame.core :as rf]))

(def routes
  [[; TODO rename and make another home that redirects on landing
    "/"
    {:name      :routes/home
     :view home/main
     :controllers
     [{ ;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [_params])
       ;; Teardown can be done here.
       :stop  (fn [_params])}]}]
   #_["/properties/:property-type/:id"
    {:name      :routes/property-details
     :view      property/property-details
     :coercion reitit.coercion.malli/coercion
     :parameters {:path {:id string?
                         :property-type [:enum "hd" "mls"]}}
     :controllers
     [{:parameters {:path [:property-type :id]}
       :start (fn [{:keys [path]}]
                (let [{:keys [property-type id]} path]
                  (rf/dispatch [:property/set-currently-viewed-property-id id])
                  (case property-type
                    "hd" (rf/dispatch [:property/fetch-asset-for-sale
                                       {:property-asset-id id}])
                    "mls" (rf/dispatch [:property/pre-fetch-mls-item
                                        {:mls-item-id id}]))))
       ;; Teardown can be done here.
       :stop  (fn [_params]
                (rf/dispatch [:property/set-currently-viewed-property-id nil]))}]}]])

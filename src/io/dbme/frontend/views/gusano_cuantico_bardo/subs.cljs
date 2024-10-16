(ns io.dbme.frontend.views.gusano-cuantico-bardo.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :gusano-cuantico-bardo/live-state
 (fn [db] (:gusano-cuantico-bardo/live-state db)))

(ns io.dbme.frontend.views.garden-earth.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :garden-earth.tuning/fingering
 (fn [db] (:garden-earth.tuning/fingering db)))

(rf/reg-sub
  :garden-earth.tuning/note-tuning
  (fn [db] (:garden-earth.tuning/note-tuning db)))

(rf/reg-sub
  :garden-earth/live-state
  (fn [db] (:garden-earth/live-state db)))

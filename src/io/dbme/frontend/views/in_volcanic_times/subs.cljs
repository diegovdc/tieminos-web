(ns io.dbme.frontend.views.in-volcanic-times.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :in-volcanic-times/live-state
 (fn [db] (:in-volcanic-times/live-state db)))

(rf/reg-sub
  :in-volcanic-times/section-data
  :<- [:in-volcanic-times/live-state]
  (fn [state] (:section state)))

(rf/reg-sub
  :in-volcanic-times/piece-data
  :<- [:in-volcanic-times/live-state]
  (fn [state] (select-keys state [:piece/running? :piece/start-time])))

(rf/reg-sub
  :in-volcanic-times/general-state
  :<- [:in-volcanic-times/live-state]
  (fn [state] (select-keys state [:piece/running?
                                  :exp/pedal-1
                                  :exp/btn-a
                                  :exp/btn-b
                                  :exp/btn-c
                                  :exp/btn-d
                                  :exp/btn-1
                                  :exp/btn-2
                                  :exp/btn-3
                                  :exp/btn-4
                                  :exp/btn-5
                                  :exp/btn-6])))
(rf/reg-sub
  :in-volcanic-times/section-handlers
  :<- [:in-volcanic-times/live-state]
  (fn [state] (-> state :section :handlers)))

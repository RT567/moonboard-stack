(ns my-app2.subs
  (:require
   [re-frame.core :as re-frame]
   [my-app2.db :as db]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::selected-holds
 (fn [db]
   (::selected-holds db)))

(re-frame/reg-sub
 ::db
 (fn [db]
   (:selected-holds db)))

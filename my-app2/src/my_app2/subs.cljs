(ns my-app2.subs
  (:require
   [re-frame.core :as re-frame]
   [my-app2.db :as db]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::db
 (fn [db query]
   (:selected-holds db)))

(re-frame/reg-sub
 ::grade
 (fn [db]
   (:grade db)))

(re-frame/reg-sub
 ::loading
 (fn [db]
   (:flag db)))

(re-frame/reg-sub
 ::error
 (fn [db]
   (:error db)))

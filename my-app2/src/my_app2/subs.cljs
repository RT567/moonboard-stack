(ns my-app2.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::db
 (fn [db]
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

(ns my-app2.events
  (:require
   [re-frame.core :as re-frame]
   [my-app2.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(defn toggle-hold [value vec]
  (cond
    (not (some #{value} vec)) (conj vec value)
    :else (remove #{value} vec)))

(re-frame/reg-event-db
 :hold-clicked
 (fn [db [_ hold]]
   (let [db-list (:selected-holds db)
         new-list (toggle-hold hold db-list)]
     (assoc db :selected-holds new-list))))

(re-frame/reg-event-db
 :clear-holds
 (fn [db _]
   (assoc db :selected-holds nil)))




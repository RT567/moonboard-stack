(ns my-app2.events
  (:require
   [re-frame.core :as re-frame]
   [my-app2.db :as db]
   [ajax.core :refer [GET]]
   ))

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

(re-frame/reg-event-fx
 :grade-climb
 (fn [{:keys [db]} event-vec]
   {:http {:method :get
           :url "http://httpbin.org/get"
           :on-success [:process-response]
           :on-fail [:process-fail]}
    :db (assoc db :flag true)}))


;; effects below

;; (re-frame/reg-fx 
;;  :http
;;  (fn [value]
;;    (GET "http://www.google.com")))

(re-frame/reg-fx
 :http
 (fn [{:keys [url success-handler error-handler]}]
   (GET url
     {:handler
      (fn [response]
        (print response))
      :error-handler
      (fn [error]
        (print error))})))
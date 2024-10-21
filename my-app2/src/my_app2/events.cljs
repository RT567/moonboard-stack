(ns my-app2.events
  (:require
   [re-frame.core :as re-frame]
   [my-app2.db :as db]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
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
 :grade-climb-old-broken
 (fn [{:keys [db]} event-vec]
   {:http {:method :get
           :url "http://httpbin.org/get"
           :on-success [:process-response]
           :on-fail [:process-fail]}
    :db (assoc db :flag true)}))

(re-frame/reg-event-fx
 :grade-climb
 (fn [{:keys [db]} _]
   (let [holds (:selected-holds db)]
     {:http-xhrio {:method          :post
                   :uri             "https://fastapi-example-f6yd.onrender.com"  ; Update with your backend URL
                   :params          {:holds holds}
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :timeout         8000
                   :on-success      [:process-response]
                   :on-failure      [:process-fail]}
      :db (assoc db :flag true)})))

;; Event to process successful responses
(re-frame/reg-event-db
 :process-response
 (fn [db [_ response]]
   (let [grade (:grade response)]
     (assoc db :grade grade :flag false))))

;; Event to process failed requests
(re-frame/reg-event-db
 :process-fail
 (fn [db [_ error]]
   (assoc db :error error :flag false)))
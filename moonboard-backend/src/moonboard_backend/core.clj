(ns moonboard-backend.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "Text/html"}
   :body "Hello World, reloaded 3"})

(defn -main [& args]
  (jetty/run-jetty handler {:port 3000 :join? false}))



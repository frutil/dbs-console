(ns frutil.dbs.console.comm
 (:require
  [ajax.core :refer [GET POST DELETE PUT]]))


(defn- database-path [ident]
  (str "/api/databases/" (namespace ident) "/" (name ident)))


(defn load-databases [callback]
  (GET "/api/databases/"
       {:handler (fn [response]
                   (js/console.log "RESPONSE" (type response) response)
                   (callback response))}))


(defn create-database [ident callback]
  (POST (database-path ident)
        {:handler (fn [response]
                    (js/console.log "RESPONSE" (type response) response)
                    (callback))}))


(defn delete-database [ident callback]
  (DELETE (database-path ident)
        {:handler (fn [response]
                    (js/console.log "RESPONSE" (type response) response)
                    (callback))}))

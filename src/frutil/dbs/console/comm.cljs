(ns frutil.dbs.console.comm
 (:require
  [cljs.reader :refer [read-string]]
  [ajax.core :refer [GET POST DELETE PUT]]))


(defn- database-path [ident]
  (str "/api/databases/" (namespace ident) "/" (name ident)))


(defn load-databases [callback]
  (GET "/api/databases/"
       {:handler (fn [response]
                   (js/console.log "LIST RESPONSE" (type response) response)
                   (callback response))}))


(defn create-database [ident callback]
  (POST (database-path ident)
        {:handler (fn [response]
                    (js/console.log "CREATE RESPONSE" (type response) response)
                    (callback))}))


(defn delete-database [ident callback]
  (DELETE (database-path ident)
        {:handler (fn [response]
                    (js/console.log "DELETE RESPONSE" (type response) response)
                    (callback))}))


(defn execute-query [db-ident query callback]
  (GET (database-path db-ident)
       {:params {:q query}
        :handler (fn [response]
                   (js/console.log "QUERY RESPONSE" (type response) response)
                   (callback response))}))


(defn load-entities [db-ident wheres callback]
  (GET (str (database-path db-ident) "/entities")
       {:params {:wheres (pr-str wheres)}
        :handler (fn [response]
                   (js/console.log "ENTITIES RESPONSE" (type response) response)
                   (callback response))}))


(defn execute-tx [db-ident tx callback]
  (PUT (database-path db-ident)
       {:params {:tx tx}
        :handler (fn [response]
                   (js/console.log "TX RESPONSE" (type response) response)
                   (callback response))}))

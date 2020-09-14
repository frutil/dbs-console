(ns frutil.dbs.console.state
  (:require
   [reagent.core :as r]
   [frutil.dbs.console.comm :as comm]))


;;; databases list

(defonce DATABASES-LIST (r/atom nil))


(defn databases-list []
  @DATABASES-LIST)


(defn on-database-list-received [databases]
  (reset! DATABASES-LIST databases))


;;; database

(defonce DATABASE (r/atom nil))


(defn database []
  @DATABASE)


(defn database-ident []
  (-> (database) :ident))


(defn on-database-selected [database]
  (reset! DATABASE database))


;;; query


(defn query-result []
  (-> (database) :query-result))


(defn on-query-result-received [result]
  (swap! DATABASE assoc :query-result result))

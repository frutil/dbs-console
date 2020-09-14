(ns frutil.dbs.console.state
  (:require
   [reagent.core :as r]
   [frutil.dbs.console.comm :as comm]))


(defonce DATABASES-LIST (r/atom nil))


(defn databases-list []
  @DATABASES-LIST)


(defn on-database-list-received [databases]
  (reset! DATABASES-LIST databases))


(defn reload-databases []
  (comm/load-databases on-database-list-received))


(defonce DATABASE (r/atom nil))


(defn database []
  @DATABASE)

(defn on-database-received [database]
  (reset! DATABASE database))


(defn load-database [database]
  (on-database-received
   database))

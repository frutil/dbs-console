(ns frutil.dbs.console.commands
  (:require
   [frutil.dbs.console.state :as state]
   [frutil.dbs.console.comm :as comm]))


(defn reload-databases []
  (comm/load-databases state/on-database-list-received))


(defn select-database [database]
  ;; FIXME
  (state/on-database-selected
   database))


(defn create-database [db-ident]
  (comm/create-database
   db-ident
   (fn []
     (reload-databases)
     (select-database {:ident db-ident}))))


(defn delete-database [db-ident]
  (comm/delete-database
   db-ident
   (fn []
     (reload-databases)
     (state/on-database-selected nil))))


(defn execute-query [query]
  (comm/execute-query
   (-> (state/database) :ident)
   query
   (fn [result]
     (state/on-query-result-received result))))


(defn execute-tx [tx]
  (comm/execute-tx
   (-> (state/database) :ident)
   tx
   (fn [])))

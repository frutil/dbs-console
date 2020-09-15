(ns frutil.dbs.console.schema
  (:require
   [frutil.dbs.console.state :as state]
   [frutil.dbs.console.mui :as mui]))


(defn keyword-name [k]
  (when k (name k)))


(defn Schema []
  (let [schema (state/schema)]
    [:div
     "schema"
     [mui/RecordsTable
      {:records (->> schema
                     (map #(assoc % :id (:db/ident %)))
                     (sort-by :id))
       :columns [{:id :db/ident
                  :value #(-> % :db/ident str)}
                 {:id :db/valueType
                  :value #(-> % :db/valueType keyword-name)}
                 {:id :db/cardinality
                  :value #(-> % :db/cardinality keyword-name)}
                 {:id :db/unique
                  :value #(-> % :db/unique keyword-name)}]}]]))

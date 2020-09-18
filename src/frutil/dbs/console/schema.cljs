(ns frutil.dbs.console.schema
  (:require
   [frutil.spa.state :as state]

   [frutil.dbs.console.comm :as comm]
   [frutil.dbs.console.mui :as mui]))


(state/def-state schema
  {:request-f (fn [shelve item-id etag callback]
                (comm/load-entities
                 item-id
                 '[[?e :db/ident _]]
                 (fn [result]
                   (callback result nil))))})


(defn keyword-name [k]
  (when k (name k)))


(defn View [route-match]
  (let [db-namespace (-> route-match :parameters :path :namespace)
        db-name      (-> route-match :parameters :path :name)
        db-ident (keyword db-namespace db-name)
        schema (schema db-ident)]
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


(defn model []
  {:route ["/database/:namespace/:name/schema"
           {:name :schema
            :view #'View
            :parameters {:path {:namespace string?
                                :name string?}}}]})

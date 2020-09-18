(ns frutil.dbs.console.query
  (:require
   [reagent.core :as r]

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.text-field :refer [text-field]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.spa.state :as state]

   [frutil.dbs.console.mui :as mui]

   [frutil.dbs.console.comm :as comm]))


(state/def-state query-result {})


(defn execute-query [db-ident query]
  (comm/execute-query
   db-ident
   query
   (fn [result]
     (state/set! query-result db-ident result nil))))


(def default-query "[:find ?e
 :where
 [?e :db.user/id]]")


(defn Input [db-ident]
  (let [QUERY (r/atom default-query)]
    (fn []
      [text-field
       {:label "Query"
        :default-value default-query
        :variant :outlined
        :multiline true
        :rows-max 10
        :full-width true
        :class :monospace
        :on-change #(reset! QUERY (-> % .-target .-value))
        :on-key-down #(when (and
                             (-> % .-ctrlKey)
                             (= (-> % .-keyCode) 13))
                        (execute-query db-ident @QUERY))}])))


(defn View [route-match]
  (let [db-namespace (-> route-match :parameters :path :namespace)
        db-name      (-> route-match :parameters :path :name)
        db-ident (keyword db-namespace db-name)]
    [card
     [card-content
      [mui/Stack {}
       [Input db-ident]
       [mui/Data (query-result db-ident)]]]]))


(defn model []
  {:route ["/database/:namespace/:name/query"
           {:name :query
            :view #'View
            :parameters {:path {:namespace string?
                                :name string?}}}]})

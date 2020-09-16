(ns frutil.dbs.console.query
  (:require
   [reagent.core :as r]

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.text-field :refer [text-field]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.commands :as commands]
   [frutil.dbs.console.state :as state]))

(def default-query "[:find ?e
 :where
 [?e :db.user/id]]")


(defn Input []
  (let [QUERY (r/atom default-query)]
    (fn []
      [text-field
       {:label "Query"
        :default-value default-query
        :multiline true
        :rows 3
        :full-width true
        :font-family :monospace
        :on-change #(reset! QUERY (-> % .-target .-value))
        :on-key-down #(when (and
                             (-> % .-ctrlKey)
                             (= (-> % .-keyCode) 13))
                        (commands/execute-query @QUERY))}])))


(defn View [route-match]
  (let [db-namespace (-> route-match :parameters :path :namespace)
        db-name      (-> route-match :parameters :path :name)
        db-ident (keyword db-namespace db-name)])
  (let [database (state/database)]
    [card
     [card-content
      [mui/Stack {}
       [:div "Query"]
       [Input]
       [:pre
        (str (state/query-result))]]]]))


(defn model []
  {:route ["/database/:namespace/:name/query"
           {:name :query
            :view #'View
            :parameters {:path {:namespace string?
                                :name string?}}}]})

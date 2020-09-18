(ns frutil.dbs.console.transact
  (:require
   [reagent.core :as r]

   [frutil.spa.state :as state]

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.text-field :refer [text-field]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.comm :as comm]))


(state/def-state tx-result {})


(defn execute-tx [db-ident tx]
  (comm/execute-tx
   db-ident
   tx
   (fn [result]
     (state/set! tx-result db-ident result nil))))


(def default-tx "[{:db.user/id \"root\"
 :db.user/owner? true}]")


(defn Input [db-ident]
  (let [TX (r/atom default-tx)]
    (fn []
      [text-field
       {:label "Transaction"
        :default-value default-tx
        :variant :outlined
        :multiline true
        :rows-max 10
        :full-width true
        :class :monospace
        :on-change #(reset! TX (-> % .-target .-value))
        :on-key-down #(when (and
                             (-> % .-ctrlKey)
                             (= (-> % .-keyCode) 13))
                        (execute-tx db-ident @TX))}])))


(defn View [route-match]
  (let [db-namespace (-> route-match :parameters :path :namespace)
        db-name      (-> route-match :parameters :path :name)
        db-ident (keyword db-namespace db-name)]
    [card
     [card-content
      [mui/Stack {}
       [Input db-ident]
       [mui/Data (tx-result db-ident)]]]]))


(defn model []
  {:route ["/database/:namespace/:name/transact"
           {:name :transact
            :view #'View
            :parameters {:path {:namespace string?
                                :name string?}}}]})

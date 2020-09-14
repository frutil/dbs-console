(ns frutil.dbs.console.database-tx
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

(def default-tx "[{:db.user/id \"root\"
 :db.user/owner? true}]")


(defn Input []
  (let [TX (r/atom default-tx)]
    (fn []
      [text-field
       {:label "Transaction"
        :default-value default-tx
        :multiline true
        :rows 3
        :full-width true
        :font-family :monospace
        :on-change #(reset! TX (-> % .-target .-value))
        :on-key-down #(when (and
                             (-> % .-ctrlKey)
                             (= (-> % .-keyCode) 13))
                        (commands/execute-tx @TX))}])))


(defn DatabaseTx []
  (let [database (state/database)]
    [card
     [card-content
      [mui/Stack {}
       [:div "TX"]
       [Input]]]]))

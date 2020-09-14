(ns frutil.dbs.console.database
  (:require

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.state :as state]))


(defn ActionButton [{:keys [text on-click]}]
  [button
   {:color :primary
    :on-click on-click}
   text])


(defn Database []
  (let [database (state/database)]
    [card
     [card-content
      [mui/Stack {}
       [toolbar
        {:spacing 1}
        [:div (-> database :ident str)]
        [ActionButton
         {:text "Query"}]
        [ActionButton
         {:text "Transact"}]
        [ActionButton
         {:text "Delete"}]]
       [:pre
        (str database)]]]]))

(ns frutil.dbs.console.database-query
  (:require

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.state :as state]))



(defn DatabaseQuery []
  (let [database (state/database)]
    [card
     [card-content
      [mui/Stack {}
       [:div "Query"]
       [:pre
        (str database)]]]]))

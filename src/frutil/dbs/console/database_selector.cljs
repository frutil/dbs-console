(ns frutil.dbs.console.database-selector
  (:require

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.grid :refer [grid]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.state :as state]))


(defn ReloadButton []
  [button
   {:color :secondary
    :on-click #(state/reload-databases)}
   "reload"])


(defn DatabaseButton [database]
  [button
   {:key (-> database :ident)
    :color :primary
    :variant :contained
    :on-click #(state/load-database database)}
   (-> database :ident str)])


(defn DatabaseButtons [databases]
  [mui/Flexbox-with databases DatabaseButton])


(defn DatabaseSelector []
  (let [databases (state/databases-list)]
    [card
     [card-content
      [mui/Stack {}
       [:div
        "Databases"]
       [ReloadButton]]
      [mui/Loader [DatabaseButtons] databases]]]))

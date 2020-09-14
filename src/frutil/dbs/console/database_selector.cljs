(ns frutil.dbs.console.database-selector
  (:require
   [reagent.core :as r]

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button :refer [button]]
   [reagent-material-ui.core.dialog :refer [dialog]]
   [reagent-material-ui.core.dialog-title :refer [dialog-title]]
   [reagent-material-ui.core.dialog-content :refer [dialog-content]]
   [reagent-material-ui.core.dialog-content-text :refer [dialog-content-text]]
   [reagent-material-ui.core.dialog-actions :refer [dialog-actions]]
   [reagent-material-ui.core.text-field :refer [text-field]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.dialogs :as dialogs]
   [frutil.dbs.console.state :as state]
   [frutil.dbs.console.commands :as commands]))


(defn CreateDialog [dispose]
  (let [NAMESPACE (r/atom "")
        NAME (r/atom "")]
    (fn [dispose]
      [dialog
       {:open true
        :on-close dispose}
       [dialog-title "Create Database"]
       [dialog-content
        [text-field
         {:label "Namespace"
          :on-change #(->> % .-target .-value (reset! NAMESPACE))
          :auto-focus true
          :margin :dense
          :full-width true}]
        [text-field
         {:label "Name"
          :on-change #(->> % .-target .-value (reset! NAME))
          :margin :dense
          :full-width true}]]
       [dialog-actions
        [button
         {:color :primary
          :on-click dispose}
         "Cancel"]
        [button
         {:color :primary
          :variant :contained
          :on-click #(do
                       (commands/create-database
                        (keyword @NAMESPACE @NAME))
                       (dispose))}
         "Create Database"]]])))


(defn CreateButton []
  [button
   {:color :secondary
    :on-click #(dialogs/show-dialog [CreateDialog])}
   "create..."])


(defn ReloadButton []
  [button
   {:color :secondary
    :on-click #(commands/reload-databases)}
   "reload"])




(defn DatabaseButton [database]
  [button
   {:key (-> database :ident)
    :color :primary
    :variant :contained
    :on-click #(commands/select-database database)}
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
       [toolbar
        [ReloadButton]
        [CreateButton]]]
      [mui/Loader [DatabaseButtons] databases]]]))

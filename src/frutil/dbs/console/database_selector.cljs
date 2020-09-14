(ns frutil.dbs.console.database-selector
  (:require
   [reagent.core :as r]

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.dialogs :as dialogs]
   [frutil.dbs.console.state :as state]
   [frutil.dbs.console.commands :as commands]))




(defn CreateDialog [dispose]
  (let [FORM_STATE (r/atom {})]
    (fn [dispose]
      [mui/FormDialog
       {:dispose dispose
        :title "Create Database"
        :submit-button-text "Create Database"
        :on-submit #(commands/create-database
                     (keyword (-> @FORM_STATE :fields :namespace :value)
                              (-> @FORM_STATE :fields :name :value)))}
       ;[:pre (str @FORM_STATE)]
       [mui/DialogTextField
        {:id :namespace
         :FORM_STATE FORM_STATE
         :auto-focus true}]
       [mui/DialogTextField
        {:id :name
         :FORM_STATE FORM_STATE}]])))


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

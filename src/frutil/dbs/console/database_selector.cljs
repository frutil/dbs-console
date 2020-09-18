(ns frutil.dbs.console.database-selector
  (:require
   [reagent.core :as r]

   [reitit.frontend.easy :as rfe]

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.spa.state :as state]

   [frutil.dbs.console.navigation :as navigation]
   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.commands :as commands]
   [frutil.dbs.console.comm :as comm]))


(state/def-state databases
  {:request-f (fn [shelve item-id etag callback]
                (comm/load-databases
                 (fn [databases]
                   (callback databases nil))))})


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
    :on-click #(mui/show-dialog [CreateDialog])}
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
    :href (navigation/href :database
                           {:namespace (-> database :namespace)
                            :name (-> database :name)})
    :on-click #(commands/select-database database)}
   (-> database :ident str)])


(defn DatabaseButtons [databases]
  [mui/Flexbox-with databases DatabaseButton])


(defn View []
  (let [databases (databases)]
    [card
     [card-content
      [mui/Stack {}
       [:div
        "Databases"]
       [toolbar
        [ReloadButton]
        [CreateButton]]]
      [mui/Loader [DatabaseButtons] databases]]]))


(defn model []
  {:route [["/" {:name :database-selector
                 :view #'View}]]})

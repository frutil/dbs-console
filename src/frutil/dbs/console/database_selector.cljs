(ns frutil.dbs.console.database-selector
  (:require
   [reagent.core :as r]

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.spa.state :as state]

   [frutil.dbs.console.navigation :as navigation]
   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.comm :as comm]))


(state/def-state databases
  {:request-f (fn [shelve item-id etag callback]
                (comm/load-databases
                 (fn [databases]
                   (callback databases nil))))})


(defn reset-databases []
  (state/clear-all! databases))


(defn create-database [db-namespace db-name]
  (comm/create-database
   (keyword db-namespace db-name)
   (fn []
     (reset-databases)
     (navigation/push-state :database {:namespace db-namespace
                                       :name db-name}))))


(defn CreateDialog [dispose]
  (let [FORM_STATE (r/atom {})]
    (fn [dispose]
      [mui/FormDialog
       {:dispose dispose
        :title "Create Database"
        :submit-button-text "Create Database"
        :on-submit #(create-database
                      (-> @FORM_STATE :fields :namespace :value)
                      (-> @FORM_STATE :fields :name :value))}
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








(defn DatabaseButton [database]
  [button
   {:key (-> database :ident)
    :color :primary
    :variant :contained
    :href (navigation/href :database
                           {:namespace (-> database :namespace)
                            :name (-> database :name)})}
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
        [CreateButton]]]
      [mui/Loader [DatabaseButtons] databases]]]))


(defn model []
  {:route [["/" {:name :database-selector
                 :view #'View}]]})

(ns frutil.dbs.console.database
  (:require

   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button :refer [button]]

   [frutil.dbs.console.mui :as mui]

   [frutil.dbs.console.navigation :as navigation]
   [frutil.dbs.console.comm :as comm]
   [frutil.dbs.console.database-selector :as database-selector]))


(defn delete-database [db-ident]
  (comm/delete-database
   db-ident
   (fn []
     (database-selector/reset-databases)
     (navigation/push-state :database-selector {}))))


(defn ActionButton [{:keys [text on-click]}]
  [button
   {:color :primary
    :on-click on-click}
   text])


(defn View [route-match]
  (let [db-namespace (-> route-match :parameters :path :namespace)
        db-name      (-> route-match :parameters :path :name)
        db-ident (keyword db-namespace db-name)]
    [card
     [card-content
      [mui/Stack {}
       [toolbar
        {:spacing 1}
        [ActionButton
         {:text "Delete"
          :on-click #(delete-database db-ident)}]]]]]))


(defn model []
  {:route ["/database/:namespace/:name/"
           {:name :database
            :view #'View
            :parameters {:path {:namespace string?
                                :name string?}}}]})

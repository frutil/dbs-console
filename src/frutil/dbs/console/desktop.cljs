(ns frutil.dbs.console.desktop
  (:require
   [reagent.core :as r]

   [reagent-material-ui.core.app-bar :refer [app-bar]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.container :refer [container]]

   [reagent-material-ui.core.list :refer [list]]
   [reagent-material-ui.core.list-subheader :refer [list-subheader]]
   [reagent-material-ui.core.list-item :refer [list-item]]
   [reagent-material-ui.core.list-item-text :refer [list-item-text]]
   [reagent-material-ui.core.divider :refer [divider]]

   [frutil.dbs.console.mui :as mui]
   [frutil.dbs.console.navigation :as navigation]))

(defn NavigationListItem [text k params]
  (let [route-match (navigation/match)
        current-k (-> route-match :data :name)]
    [list-item
     {:button true
      :on-click #(navigation/push-state k params)
      :selected (= k current-k)}
     [list-item-text
      {:primary text}]]))


(defn NavigationMenu [route-match]
  (let [db-namespace (-> route-match :parameters :path :namespace)
        db-name      (-> route-match :parameters :path :name)
        route-params {:namespace db-namespace
                      :name db-name}]
    [:div
     [list
      (NavigationListItem
       "Database Selector"
       :database-selector {})]
     (when db-name
       [:div
        [divider]
        [list
         {:subheader (r/as-element
                      [list-subheader
                       db-namespace " / " db-name])}
         (NavigationListItem "Info" :database route-params)
         (NavigationListItem "Schema" :schema route-params)
         (NavigationListItem "Query" :query route-params)
         (NavigationListItem "Transact" :transact route-params)]])]))


(defn Desktop []
  (let [route-match (navigation/match)
        db-namespace (-> route-match :parameters :path :namespace)
        db-name      (-> route-match :parameters :path :name)]
    [:<>
     [mui/DialogsContainer]
     [app-bar
      {:position :fixed}
      [toolbar
       [mui/LeftDrawerToggleIconButton]
       (when db-name
         [:span db-namespace " / " db-name])]]
     [mui/LeftDrawer
      [NavigationMenu (navigation/match)]]
     [container
      [:div.toolbar]
      [:br]
      [:main
       [navigation/Switcher :view]]]]))

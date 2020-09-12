(ns ^:figwheel-hooks frutil.dbs.console.app
  (:require
   [reagent-material-ui.colors :as colors]
   [reagent-material-ui.core.button :refer [button]]
   [reagent-material-ui.core.grid :refer [grid]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.icons.add-box :refer [add-box]]
   [reagent-material-ui.icons.clear :refer [clear]]

   [frutil.dbs.console.mui :as mui]))


(def custom-theme
  {:palette {:primary   colors/purple
             :secondary colors/green}})


(defn custom-styles [{:keys [spacing] :as _theme}]
   {:border (str (spacing 1) "px solid red")
    "& .XXX" {
              "& button" {:border "3px solid green"}}})


(defn Content []
  [grid
   {:container true
    :direction "column"
    :spacing   2}

   [grid {:item true}
    [toolbar
     {:disable-gutters true}
     [:div.XXX
      [button
       {:variant  "contained"
        :color    "primary"}
       "Update value property"
       [add-box]]]

     [button
      {:variant  "outlined"
       :color    "secondary"}
      "Reset"
      [clear]]]]])


(defn mount-app []
  (mui/mount-app custom-theme custom-styles Content))


(defonce app-mounted
  (do
    (mount-app)
    true))


(defn ^:after-load dev-after-load []
  (mount-app))

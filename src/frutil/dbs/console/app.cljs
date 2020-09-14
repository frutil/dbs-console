(ns ^:figwheel-hooks frutil.dbs.console.app
  (:require
   [reagent-material-ui.colors :as colors]
   [reagent-material-ui.core.container :refer [container]]
   [reagent-material-ui.core.button :refer [button]]
   [reagent-material-ui.core.grid :refer [grid]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.icons.add-box :refer [add-box]]
   [reagent-material-ui.icons.clear :refer [clear]]

   [frutil.dbs.console.dialogs :refer [DialogsContainer]]
   [frutil.dbs.console.database :refer [Database]]
   [frutil.dbs.console.database-query :refer [DatabaseQuery]]
   [frutil.dbs.console.database-tx :refer [DatabaseTx]]
   [frutil.dbs.console.database-selector :refer [DatabaseSelector]]
   [frutil.dbs.console.state :as state]
   [frutil.dbs.console.mui :as mui]))


(def custom-theme
  {:palette {:primary {:main (get colors/cyan 800)}
             :secondary {:main (get colors/pink 600)}}})


(defn custom-styles [{:keys [spacing] :as _theme}]
  {})


(defn DatabaseWrapper [database]
  [mui/Stack {}
   [Database]
   [DatabaseQuery]
   [DatabaseTx]])


(defn Content []
  [container
   [:br]
   [mui/Stack {}
    [mui/Loader [DatabaseWrapper] (state/database)]
    [DatabaseSelector]]
   [DialogsContainer]])


(defn mount-app []
  (mui/mount-app custom-theme custom-styles Content))


(defonce app-mounted
  (do
    (mount-app)
    true))


(defn ^:after-load dev-after-load []
  (mount-app))

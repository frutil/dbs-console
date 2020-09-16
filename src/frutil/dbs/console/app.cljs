(ns ^:figwheel-hooks frutil.dbs.console.app
  (:require
   [reagent-material-ui.colors :as colors]

   [frutil.dbs.console.desktop :as desktop]
   [frutil.dbs.console.database-selector :as database-selector]
   [frutil.dbs.console.database :as database]
   [frutil.dbs.console.schema :as schema]
   [frutil.dbs.console.query :as query]
   [frutil.dbs.console.transact :as transact]

   [frutil.dbs.console.navigation :as navigation]
   [frutil.dbs.console.state :as state]
   [frutil.dbs.console.mui :as mui]))


(def theme
  {:palette {:primary {:main (get colors/cyan 800)}
             :secondary {:main (get colors/pink 600)}}})


(defn styles [{:keys [spacing] :as theme}]
  (js/console.log "THEME" (-> theme :mixins))
  {"& .toolbar" (-> theme :mixins :toolbar)
   "& .b" {:font-weight :bold :letter-spacing "1px"}})


(def routes
  [(-> (database-selector/model) :route)
   (-> (database/model) :route)
   (-> (schema/model) :route)
   (-> (query/model) :route)
   (-> (transact/model) :route)])



(defn mount-app []
  (mui/mount-app theme styles #'desktop/Desktop))


(defn initialize []
  (navigation/initialize! routes)
  (mount-app))


(defonce initialized
  (do
    (initialize)
    true))


(defn ^:after-load dev-after-load []
  (initialize))

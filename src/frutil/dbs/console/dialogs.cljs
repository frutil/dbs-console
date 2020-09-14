(ns frutil.dbs.console.dialogs
  (:require
   [reagent.core :as r]))


(defonce ACTIVE_DIALOG (r/atom nil))


(defn DialogsContainer []
  [:div.DialogsContainer
   (when-let [dialog @ACTIVE_DIALOG]
     (conj dialog
           #(reset! ACTIVE_DIALOG nil)))])


(defn show-dialog [dialog]
  (reset! ACTIVE_DIALOG dialog))

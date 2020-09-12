(ns frutil.dbs.console.mui
  (:require
   ;[reagent.core :as r]
   [reagent.dom :as rdom]
   [reagent-material-ui.core.css-baseline :refer [css-baseline]]
   [reagent-material-ui.styles :as styles]))


(set! *warn-on-infer* true)


(defn styled-component [styles component]
  (let [component-styles (fn [theme]
                           {:style (styles theme)})
        with-custom-styles (styles/with-styles component-styles)
        StyledComponent (fn [{:keys [classes] :as _props}]
                          [:div
                           {:class (:style classes)}
                           component])]
    [(with-custom-styles StyledComponent)]))


(defn app [custom-theme custom-styles Content]
  [:<> ; fragment
   [css-baseline]
   [styles/theme-provider (styles/create-mui-theme custom-theme)
    (styled-component custom-styles [Content])]])


(defn mount-app [custom-theme custom-styles Content]
  (rdom/render (app custom-theme custom-styles Content)
               (js/document.getElementById "app")))

(ns frutil.dbs.console.mui
  (:require
   ;[reagent.core :as r]
   [reagent.dom :as rdom]

   [reagent-material-ui.core.grid :refer [grid]]
   [reagent-material-ui.core.css-baseline :refer [css-baseline]]
   [reagent-material-ui.styles :as styles]

   [reagent-material-ui.core.dialog :refer [dialog]]
   [reagent-material-ui.core.dialog-title :refer [dialog-title]]
   [reagent-material-ui.core.dialog-content :refer [dialog-content]]
   [reagent-material-ui.core.dialog-actions :refer [dialog-actions]]
   [reagent-material-ui.core.text-field :refer [text-field]]
   [reagent-material-ui.core.button :refer [button]]))


;(set! *warn-on-infer* true)


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


;;; layouts

(defn Stack [options & components]
  (into
   [grid
    (assoc options
           :container true
           :direction :column
           :spacing (get options :spacing 1))]
   (map (fn [component]
          [grid
           {:item true}
           component])
        components)))


(defn Flexbox-with [items component]
  [:div
   (into
    [grid {:container true :spacing 1}]
    (map (fn [item]
           [grid {:item true}
            [component item]])
         items))])


;;; Components


(defn Loader [partial-component data]
  (if data
    (conj partial-component data)
    [:div "Loading..."]))


;;; forms


(defn FormDialog
  [{:keys [dispose
           title
           submit-button-text
           on-submit]}
   & children]
  [dialog
   {:open true
    :on-close dispose}
   (when title
     [dialog-title title])
   (into
    [dialog-content]
    children)
   [dialog-actions
    [button
     {:color :primary
      :on-click dispose}
     "Cancel"]
    [button
     {:color :primary
      :variant :contained
      :on-click #(do
                   (on-submit)
                   (dispose))}
     (or submit-button-text
         "Submit")]]])


(defn DialogTextField
  [{:keys [id FORM_STATE label auto-focus] :as options}]
  ;; TODO mandatory id, FORM_STATE
  [text-field
   {:name id
    :label (or label (str id))
    :margin :dense
    :full-width true
    :auto-focus auto-focus
    :on-change #(swap! FORM_STATE
                       assoc-in [:fields id :value]
                       (-> % .-target .-value))}])

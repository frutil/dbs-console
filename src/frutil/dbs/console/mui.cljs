(ns frutil.dbs.console.mui
  (:require
   [cljs.pprint :as pprint]

   [reagent.core :as r]
   [reagent.dom :as rdom]

   [reagent-material-ui.core.grid :refer [grid]]
   [reagent-material-ui.core.css-baseline :refer [css-baseline]]
   [reagent-material-ui.styles :as styles]

   [reagent-material-ui.core.table-container :refer [table-container]]
   [reagent-material-ui.core.table :refer [table]]
   [reagent-material-ui.core.table-head :refer [table-head]]
   [reagent-material-ui.core.table-body :refer [table-body]]
   [reagent-material-ui.core.table-row :refer [table-row]]
   [reagent-material-ui.core.table-cell :refer [table-cell]]

   [reagent-material-ui.core.drawer :refer [drawer]]
   [reagent-material-ui.core.paper :refer [paper]]
   [reagent-material-ui.core.card :refer [card]]
   [reagent-material-ui.core.card-content :refer [card-content]]
   [reagent-material-ui.core.dialog :refer [dialog]]
   [reagent-material-ui.core.dialog-title :refer [dialog-title]]
   [reagent-material-ui.core.dialog-content :refer [dialog-content]]
   [reagent-material-ui.core.dialog-actions :refer [dialog-actions]]
   [reagent-material-ui.core.text-field :refer [text-field]]
   [reagent-material-ui.core.button :refer [button]]
   [reagent-material-ui.core.icon-button :refer [icon-button]]

   [reagent-material-ui.icons.menu :refer [menu]]))

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


(defn Data
  [& datas]
  (into
   [:div.Data
    {:style {:display :grid
             :grid-gap "10px"}}]
   (map (fn [data]
          [:code
           {:style {:white-space :pre-wrap
                    :overflow :auto}}
           (try
             (with-out-str (pprint/pprint data))
             (catch :default ex
               "!!! ERROR !!! pprint failed for data"))])
        datas)))


(defn Loader [partial-component data]
  (if data
    (conj partial-component data)
    [:div "Loading..."]))


(defn Card [& children]
  [card
   [card-content
    (into
     [grid {:container true :direction :column :spacing 1}]
     (map (fn [child]
            [grid {:item true}
             child])
          children))]])


;;; table


(defn RecordsTable [{:keys [records columns]}]
  [paper
   [table-container
    [table
     {:size :small}
     [table-head
      [table-row
       (for [column columns]
         ^{:key (-> column :id)}
         [table-cell
          [:span.b
           (or (-> column :head)
               (-> column :id str))]])]]
     [table-body
      (for [record records]
        ^{:key (-> record :id)}
        [table-row
         {:hover true}
         (for [column columns]
           ^{:key (-> column :id)}
           [table-cell ((-> column :value) record)])])]]]])


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


;;; dialogs


(defonce ACTIVE_DIALOG (r/atom nil))


(defn DialogsContainer []
  [:div.DialogsContainer
   (when-let [dialog @ACTIVE_DIALOG]
     (conj dialog
           #(reset! ACTIVE_DIALOG nil)))])


(defn show-dialog [dialog]
  (reset! ACTIVE_DIALOG dialog))


;;; drawers

(defonce LEFT_DRAWER (r/atom nil))


(defn hide-left-drawer []
  (reset! LEFT_DRAWER false))


(defn LeftDrawer [& children]
  [drawer
   {:open (-> @LEFT_DRAWER boolean)
    :anchor :left
    :on-close hide-left-drawer}
   (into
    [:div
     {:on-click hide-left-drawer}]
    children)])


(defn LeftDrawerToggleIconButton []
  [icon-button
   {:color :inherit
    :edge :start
    :on-click #(reset! LEFT_DRAWER true)}
   [menu]])

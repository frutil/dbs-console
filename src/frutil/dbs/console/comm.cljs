(ns frutil.dbs.console.comm
 (:require
  [ajax.core :refer [GET POST]]))


(defn load-databases [callback]
  (js/console.log "LOAD!!")
  (GET "/api/databases/"
       {:handler (fn [response]
                   (js/console.log "RESPONSE" (type response) response)
                   (callback response))}))

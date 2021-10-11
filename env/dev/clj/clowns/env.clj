(ns clowns.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [clowns.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[clowns started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[clowns has shut down successfully]=-"))
   :middleware wrap-dev})

(ns clowns.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[clowns started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[clowns has shut down successfully]=-"))
   :middleware identity})

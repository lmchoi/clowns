(ns clowns.research.handler
  (:require [ring.util.response :refer [response]]
            [clowns.yahoo.client :as yahoo]))

(defn get-matchup [request]
  (response {:result (yahoo/get-matchup (get-in request [:session :access-token]))}))
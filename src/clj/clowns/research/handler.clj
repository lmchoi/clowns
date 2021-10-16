(ns clowns.research.handler
  (:require [ring.util.response :refer [response]]
            [clowns.projections.core :as p]
            [clowns.schedule.core :as nba]
            [clowns.yahoo.client :as yahoo]))

(defn get-matchup [request]
  (let [matchup (yahoo/get-matchup (get-in request [:session :access-token]))
        schedule (nba/get-schedule)
        projection (p/get-projections)]

    (println matchup)
    (response {:result matchup})))
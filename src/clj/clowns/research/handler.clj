(ns clowns.research.handler
  (:require [ring.util.response :refer [response]]
            [clowns.projections.core :as pj]
            [clowns.schedule.core :as nba]
            [clowns.yahoo.client :as yahoo]
            [cheshire.core :as json]
            [clojure.tools.logging :as log]))

(defn get-matchup [request]
  (let [team-rosters (yahoo/get-league-rosters (get-in request [:session :access-token]))
        schedule (nba/get-this-week-schedule)]
    (log/debug (json/generate-string team-rosters {:pretty true}))
    (log/debug (json/generate-string schedule {:pretty true}))
    (response {:predictions (pj/get-projections team-rosters schedule)})))

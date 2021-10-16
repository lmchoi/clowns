(ns clowns.schedule.core
  (:require [cheshire.core :as json]
            [clj-http.client :as http]))

(def schedule-url "https://uk.global.nba.com/stats2/season/schedule.json?days=7&gameDate=2021-10-19")

(defn- extract-game-info [{:keys [awayTeam homeTeam]}]
  {:away-team (get-in awayTeam [:profile :abbr])
   :home-team (get-in homeTeam [:profile :abbr])})

(defn get-schedule []
  (let [{:keys [status body]} (http/get schedule-url)
        dates (get-in (json/parse-string body true) [:payload :dates])]
    (map (fn [{:keys [games]}]
           {:games (map extract-game-info games)}) dates)))

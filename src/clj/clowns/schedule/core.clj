(ns clowns.schedule.core
  (:require [cheshire.core :as json]
            [clj-http.client :as http]))

; TODO change gameDate
(def schedule-url "https://uk.global.nba.com/stats2/season/schedule.json?days=7&gameDate=2021-11-15")

(defn- extract-game-info [{:keys [awayTeam homeTeam]}]
  {:away-team (get-in awayTeam [:profile :abbr])
   :home-team (get-in homeTeam [:profile :abbr])})

(defn get-this-week-schedule []
  (let [{:keys [status body]} (http/get schedule-url)
        dates (get-in (json/parse-string body true) [:payload :dates])]
    (map (fn [{:keys [games]}]
           (map extract-game-info games)) dates)))

(defn get-teams-playing-this-week []
  (->> (get-this-week-schedule)
       (map #(flatten (map vals %)))))

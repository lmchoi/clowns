(ns clowns.yahoo.client
  (:require [clowns.oauth :as oauth]
            [cheshire.core :as json]))

; get roster of a given team
;(def roster-url "https://fantasysports.yahooapis.com/fantasy/v2/team/roster?format=json")
(def roster-url "https://fantasysports.yahooapis.com/fantasy/v2/teams;team_keys=nba.l.112641.t.1,nba.l.112641.t.2,nba.l.112641.t.3,nba.l.112641.t.4,nba.l.112641.t.5,nba.l.112641.t.6,nba.l.112641.t.7,nba.l.112641.t.8,nba.l.112641.t.9,nba.l.112641.t.10/roster?format=json")

(defn extract-player [{:keys [name
                              editorial_team_abbr
                              status
                              selected_position
                              eligible_positions]}]
  {:name              (:full name)
   :team              editorial_team_abbr
   :status            status
   :selected_position (:position (apply merge (flatten selected_position)))})

(defn- extract-roster [team]
  {:team (get-in team [:team 0 2 :name])
   :roster (map (fn [[_ {player :player}]]
                  (extract-player (apply merge (flatten player))))
                (dissoc (get-in team [:team 1 :roster :0 :players]) :count))})

(defn get-matchup [access-token]
  (let [resource (json/parse-string (oauth/request-resource access-token roster-url) true)
        teams (vals (get-in resource [:fantasy_content :teams]))
        teams (filter :team teams)]
    (map extract-roster teams)))

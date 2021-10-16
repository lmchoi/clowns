(ns clowns.research.handler
  (:require [ring.util.response :refer [response]]
            [clowns.projections.core :refer [projections]]
            [clowns.schedule.core :as nba]
            [clowns.yahoo.client :as yahoo]
            [clojure.tools.logging :as log]))

(def stats [:fgm :fga :ftm :fta :tpm :pts :reb :ast :stl :blk :to])

(defn get-player-projections [{:keys [name status]}]
  (if-let [stats (first (filter (fn [record]
                                  (= name (:name record)))
                                projections))]
    (if-not (= status "INJ") stats)
    (log/warn "Unable to find projections for" name)))

(defn team-projections [{:keys [team roster]}]
  (let [pp (keep get-player-projections roster)]
    (assoc (zipmap stats (map #(apply + (map % pp)) stats))
      :team  team
      :count (count pp))))

(defn matchup-projections [teams]
  (map team-projections teams))

; matchup
;{:name John Wall, :team HOU, :status INJ, :selected_position IL}
(defn get-matchup [request]
  (let [matchup (yahoo/get-matchup (get-in request [:session :access-token]))
        ;schedule (nba/get-schedule)
        ]
    (response {:results (matchup-projections matchup)
               ;:matchup matchup
               ;:schedule schedule
               ;:projections projections
               })))
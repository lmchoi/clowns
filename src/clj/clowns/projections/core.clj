(ns clowns.projections.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [mount.core :refer [args defstate]]
            [clojure.tools.logging :as log]))

(def categories [:fgm :fga :ftm :fta :tpm :pts :reb :ast :stl :blk :to])

(declare projections)
(declare get-projections)

(defn- string->number [s]
  (let [n (read-string s)]
    (if (number? n) n s)))

(defn- csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data)
            (map (comp keyword clojure.string/lower-case))
            repeat)
       (map #(map string->number %) (rest csv-data))))

(defstate projections
          :start (csv-data->maps
                   (with-open [reader (io/reader (io/resource "data/projections.csv"))]
                     (doall (csv/read-csv reader)))))

(defn add-player-projections [{:keys [name] :as player}]
  (if-let [stats (first (filter #(= name (:name %)) projections))]
    (do
      (assoc player :stats stats))
    (log/warn "Unable to find projections for" name)))

(defn is-playing? [{:keys [status selected_position team]} teams-playing]
  (and (not= status "INJ")
       (not= status "O")
       ;(not= selected_position "BN")
       ;(not= selected_position "IL")
       (contains? teams-playing team)))

(defn get-daily-projections [roster day-schedule]
  (let [teams-playing (set (flatten (map vals day-schedule)))]
    (let [stats (map :stats (filter #(is-playing? % teams-playing) roster))]
      (zipmap categories (map #(apply + (map % stats)) categories)))))

(defn get-team-projections [{:keys [roster]} schedule]
  (let [roster-stats (map add-player-projections roster)]
    ; for each day in the schedule
    (map #(get-daily-projections roster-stats %) schedule)))

(defn get-projections [team-rosters schedule]
  (map #(get-team-projections % schedule) team-rosters))

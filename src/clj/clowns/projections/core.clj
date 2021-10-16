(ns clowns.projections.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn- string->number [s]
  (let [n (read-string s)]
    (if (number? n) n s)))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data)
            (map (comp keyword clojure.string/lower-case))
            repeat)
       (map #(map string->number %) (rest csv-data))))

(defn get-projections []
  (csv-data->maps
    (with-open [reader (io/reader (io/resource "data/projections.csv"))]
      (doall (csv/read-csv reader)))))

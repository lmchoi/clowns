(ns clowns.oauth
  (:require
    [clowns.config :refer [env]]
    [clowns.scribe :as oauth]
    [mount.core :refer [defstate]]
    [clojure.tools.logging :as log]))

(declare service)
(defstate service
          :start (oauth/create-service (env :oauth-consumer-key)
                                       (env :oauth-consumer-secret)))

(defn fetch-request-token
  "Fetches a request token."
  [_]
  (oauth/get-authorization-url service))

(defn fetch-access-token
  [request-token]
  (oauth/get-access-token service request-token))

(defn request-resource [access-token url]
  (oauth/request-resource service access-token url))

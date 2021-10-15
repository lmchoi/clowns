(ns clowns.oauth
  (:require
    [clowns.config :refer [env]]
    [clowns.scribe :as scribe]
    [mount.core :refer [defstate]]
    [clojure.tools.logging :as log]))

(declare service)
(defstate service
          :start (scribe/create-service (env :oauth-consumer-key)
                                        (env :oauth-consumer-secret)))

(defn fetch-request-token
  "Fetches a request token."
  [_]
  (scribe/get-authorization-url service))

(defn fetch-access-token
  [request-token]
  (scribe/get-access-token service request-token))

(defn request-resource [access-token url]
  (scribe/request-resource service access-token url))

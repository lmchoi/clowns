(ns clowns.routes.oauth
  (:require
    [ring.util.http-response :refer [ok found]]
    [ring.util.response :refer [response]]
    [clowns.oauth :as oauth]))

(defn oauth-init
  [request]
  (-> (oauth/fetch-request-token request)
      found))

(defn oauth-callback
  [_]
  (response {:results "OK!"}))

(defn set-access-token! [access-token {session :session}]
  (-> (response {:results "OK!"})
      (assoc :session (assoc session :access-token access-token))))

(defn enter-verifier [{{{:keys [code]} :body} :parameters :as request}]
  (set-access-token! (oauth/fetch-access-token code) request))

(defn oauth-routes []
  ["/oauth"
   ["/oauth-init" {:get oauth-init}]
   ["/oauth-callback" {:get oauth-callback}]])

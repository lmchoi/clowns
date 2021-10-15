(ns clowns.scribe
  (:import (com.github.scribejava.core.builder ServiceBuilder)
           (com.github.scribejava.core.model OAuthConstants Verb OAuthRequest OAuth2AccessToken)
           (com.github.scribejava.apis YahooApi20)
           (com.github.scribejava.core.oauth OAuth20Service)))

(defn create-service [client-id client-secret]
  (-> client-id
      (ServiceBuilder.)
      (.apiSecret client-secret)
      (.callback (OAuthConstants/OOB))
      ; TODO need to enable ssl?
      ;(.callback "https://localhost:3000/oauth/oauth-callback")
      (.build (YahooApi20/instance))))

(defn get-authorization-url [^OAuth20Service service]
  (.getAuthorizationUrl service))

(defn ^OAuth2AccessToken get-access-token [^OAuth20Service service ^String verifier]
  (.getAccessToken service verifier))

(defn request-resource [^OAuth20Service service ^OAuth2AccessToken access-token url]
  ; TODO check getCode()
  (if access-token
    (let [request (OAuthRequest. (Verb/GET) url)]
      (.signRequest service access-token request)
      (let [scribe-response (.execute service request)]
        (.getBody scribe-response)))
    ; TODO else redirect to log in? refresh token?
    ))

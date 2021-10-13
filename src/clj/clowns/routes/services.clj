(ns clowns.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [clowns.routes.services.graphql :as graphql]
    [clowns.middleware.formats :as formats]
    [clowns.routes.oauth :as oauth]
    [ring.util.http-response :refer :all]
    [clojure.java.io :as io]))

(defn service-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
    :swagger {:id ::api}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 coercion/coerce-exceptions-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc true
        :swagger {:info {:title "my-api"
                         :description "https://cljdoc.org/d/metosin/reitit"}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ["/ping"
    {:get (constantly (ok {:message "pong"}))}]
   
   ["/graphql" {:no-doc true
                :post (fn [req] (ok (graphql/execute-request (-> req :body slurp))))}]

   ["/oauth"
    {:swagger {:tags ["oauth"]}}

    ["/oauth-oob"
     {:post {:summary "enter oob"
             :parameters {:body {:code string?}}
             :responses {200 {:body {:results string?}}}
             :handler oauth/enter-verifier}}]

    ["/request-resource"
     {:post {:summary "request resource"

             ;:parameters {:body {:code string?}}
             ;:responses {200 {:body {:results string?}}}
             :handler oauth/request-resource}}]]])

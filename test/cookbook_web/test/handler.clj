(ns cookbook-web.test.handler
  (:import [java.io File])
  (:use clojure.test
        ring.mock.request
        cookbook-web.handler)
  (:require [clojure.java.io :as io]))

;(let [req (request :get "/primitive-data/")]
;  (println req)
;  (println (app-routes req)))
(ns cookbook-web.handler
  (:import [java.io File]
           [org.asciidoctor Asciidoctor Asciidoctor$Factory]
           [java.net URL])
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [hiccup.page :as page]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def ^Asciidoctor asciidoctor (Asciidoctor$Factory/create))

(def cookbook-root "cookbook")

(defmulti render class)

(defmethod render File [^File f]
  (when (and f (.exists f))
    (page/html5
      [:head ;(page/include-css "/google-code-prettify/prettify.css")
       (page/include-js
         "/google-code-prettify/prettify.js"
         "/google-code-prettify/lang-clj.js"
         "/js/jquery-1.10.2.min.js"
         "/js/main.js")
       (page/include-css
         "/google-code-prettify/desert.css"
         "/css/screen.css")]
      [:body (.renderFile asciidoctor f {})])))

(defmethod render URL [^URL res]
  (let [^File f (when res (File. (.toURI res)))]
    (render f)))

(defmethod render :default [x] nil)

(defn render-not-found [uri]
  (let [path (str cookbook-root uri)
        res (io/resource path)
        dir (File. (.toURI res))]
    (when (.isDirectory dir)
      (let [fs (filter #(and (.isFile %)
                          (-> % .getName (.endsWith ".asciidoc")))
                 (.listFiles dir))
            f (first fs)]
        (render f)))))

(defroutes app-routes
  (GET "/" []
    (render (io/resource (str cookbook-root "/clojure-cookbook.asciidoc"))))
  (GET "/*/" {:keys [uri] :as req}
    (let [paths (str/split uri #"/")
          path (last paths)
          name (str path ".asciidoc")]
      (or
        (render (io/resource (str cookbook-root uri name)))
        (render-not-found uri)
        (let [paths (drop-last paths)
              uri (str (str/join "/" paths) "/")]
          (resp/redirect uri)))))
  (GET "/*.asciidoc" {:keys [uri] :as req}
    (render (io/resource (str cookbook-root uri))))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
(defproject cookbook-web "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.4"]
                 [org.asciidoctor/asciidoctor-java-integration "0.1.3"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler cookbook-web.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}})

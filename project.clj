(defproject clojure-getting-started "1.0.0-SNAPSHOT"
  :description "Generating itmeJP's calendar"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [environ "1.0.0"]
                 [clj-http "2.3.0"]
                 [clj-time "0.12.0"]
                 [org.clojure/data.json "0.2.6"]
                 [net.sf.biweekly/biweekly "0.6.0"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "itmejp-calendar.jar"
  :profiles {:production {:env {:production true}}})

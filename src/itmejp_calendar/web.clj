(ns itmejp-calendar.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clj-http.client :as client]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]])
  (:import (biweekly Biweekly
                     ICalendar)
           (biweekly.component VEvent)))


(def itmejp-api "http://itmejp.com/api/events/")

(defn start-date-time [event]
  (t/to-time-zone (f/parse (event "event_dt_start")) (t/time-zone-for-id (event "event_dt_timezone"))))

(defn end-date-time [event]
  (if (not (clojure.string/blank? (event "event_dt_end")))
    (t/from-time-zone (f/parse (event "event_dt_end")) (t/time-zone-for-id (event "event_dt_timezone"))))
    (t/plus (start-date-time event) (t/hours 4)))

(defn events [] (->>
  itmejp-api
  client/get
  :body
  json/read-str))

(defn calendar-event [event]
  (let [cevent (VEvent.)]
    (.setSummary cevent (event "event_name"))
    (.setDateStart cevent (c/to-date (start-date-time event)))
    (.setDateEnd cevent (c/to-date (end-date-time event)))
    cevent))

(defn calendar []
  (let [cal (ICalendar.)]
    (doseq [event (events)] (.addEvent cal (calendar-event event)))
    (-> (into-array ICalendar [cal]) Biweekly/write .go)))

(defroutes app
  (GET "/" []
    {:status 200
     ; :headers {"Content-Type" "text/plain"}
     :headers {"Content-Type" "text/calendar"}
     :body (calendar)})
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
; (.stop server)
; (def server (-main))

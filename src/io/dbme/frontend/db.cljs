(ns io.dbme.frontend.db)

(def initial-db
  {:db/initialized? true
   :connected false
   :sound-events.v1/config {:y-scale-factor 1
                            :x-scale-factor 1
                            :grid-lines-seconds 10}})

(ns io.dbme.utils
  (:require [clojure.string :as str]
   #?(:cljs [goog.string :refer [format]])
   #?(:cljs [goog.string.format])))

(defn format-seconds [seconds]
  (let [minutes (quot seconds 60)
        remaining-seconds (mod seconds 60)]
    (str minutes ":" (format "%02d" remaining-seconds))))

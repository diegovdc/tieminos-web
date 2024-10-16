(ns io.dbme.utils
  (:require #?(:cljs [goog.string :refer [format]])
            #?(:cljs [goog.string.format])))

(defn format-seconds [seconds]
  (let [sign (if (neg? seconds) "-" "")    ;; Check if the input is negative
        abs-seconds (Math/abs seconds)     ;; Use absolute value for calculations
        minutes (quot abs-seconds 60)
        remaining-seconds (mod abs-seconds 60)]
    (str sign minutes ":" (format "%02d" remaining-seconds))))

(comment
  (format-seconds -135)  ;; => "-2:15"
  (format-seconds 135)   ;; => "2:15"
  (format-seconds -5)    ;; => "-0:05"
  (format-seconds 0)     ;; => "0:00"
  )

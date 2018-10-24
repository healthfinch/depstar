(ns hf.depstar.jar
  (:require [hf.depstar.uberjar :refer [run]]))

(defn -main
  [destination]
  (run {:dest destination :jar :thin}))

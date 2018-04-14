(ns hf.depstar.thinjar
  (:require [hf.depstar.impl :as depstar])
  (:import (java.nio.file Path)))

(defn run
  [{:keys [prefix output] :as options}]
  (println "Preparing thin jar")
  (let [tmp    (depstar/temp-dir "thinjar")
        dest   (.resolve ^Path prefix output)
        xf     (comp (remove depstar/depstar-itself?)
                     (remove (comp #{::depstar/jar}
                                   depstar/classify)))
        jar-cp (into [] xf (depstar/current-classpath))]
    
    (run! #(depstar/copy-source % tmp options) jar-cp)
    (println "Writing thinjar...")
    (depstar/write-jar tmp dest)))

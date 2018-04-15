(ns hf.depstar.deps
  (:require [clojure.edn :as edn]
            [clojure.java.io :as jio]
            [hf.depstar.impl :as depstar])
  (:import (java.nio.file CopyOption LinkOption OpenOption
                          StandardCopyOption StandardOpenOption
                          FileSystem FileSystems Files
                          FileVisitResult FileVisitor
                          Path)))

(derive ::depstar/jar ::preserve-jar)

(defmethod depstar/copy-source*
  ::preserve-jar
  [src dest options]
  (println "copying from" src)
  (let [source (depstar/path src)
        name   (.getFileName source)
        target (.resolve ^Path dest name)]
    (depstar/copy! src
                   (Files/newInputStream source (make-array OpenOption 0))
                   target)))

(defn run
  [{:keys [prefix output exclude] :as options}]
  (println "Preparing deps ...")
  (prefer-method depstar/copy-source* ::preserve-jar :hf.depstar.uberjar/consume-jar)
  (let [dest   prefix
        xf     (cond-> (comp (remove depstar/depstar-itself?)
                             (filter (comp #{::depstar/jar} depstar/classify)))
                 exclude (comp (remove #(re-find (re-pattern exclude) %))))
        jar-cp (into [] xf (depstar/current-classpath))]

    (println "Copying deps...")
    (run! #(depstar/copy-source % dest options) jar-cp)))

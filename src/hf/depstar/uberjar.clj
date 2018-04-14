(ns hf.depstar.uberjar
  (:require [hf.depstar.impl :as depstar])
  (:import [java.io InputStream OutputStream PushbackReader]
           [java.nio.file CopyOption LinkOption OpenOption
            StandardCopyOption StandardOpenOption
            FileSystem FileSystems Files
            FileVisitResult FileVisitor
            Path]
           [java.nio.file.attribute BasicFileAttributes FileAttribute]
           [java.util.jar JarInputStream JarOutputStream JarEntry]))

;; future:
;; other knobs?
;; clj -M options?
;; look into MANIFEST entries

(derive ::depstar/jar ::consume-jar)

(defmethod depstar/copy-source*
  ::consume-jar
  [src dest options]
  (prn "copy-source* consume-jar" src dest)
  (depstar/consume-jar
   (depstar/path src)
   (fn [inputstream ^JarEntry entry]
     (let [name (.getName entry)
           target (.resolve ^Path dest name)]
       (if (.isDirectory entry)
         (Files/createDirectories target (make-array FileAttribute 0))
         (do (Files/createDirectories (.getParent target) (make-array FileAttribute 0))
             (depstar/copy! name inputstream target)))))))

(defn run
  [{:keys [prefix output] :as options}]
  (prefer-method depstar/copy-source*  ::consume-jar :hf.depstar.deps/preserve-jar)
  (println "Preparing uberjar")
  (let [tmp (depstar/temp-dir "uberjar")
        xf (remove depstar/depstar-itself?)
        cp (into [] xf (depstar/current-classpath))
        dest (-> prefix
                 (.resolve ^Path output))]
    (run! #(depstar/copy-source % tmp options) cp)
    (println "Writing uberjar...")
    (depstar/write-jar tmp dest)))

;; backwards compatibility ...
(defn -main [destination]
  (run {:prefix (depstar/cwd) :output (depstar/path destination)}))

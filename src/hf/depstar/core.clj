(ns hf.depstar.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [hf.depstar.impl :refer [path cwd]]
            [hf.depstar.uberjar :as uberjar]
            [hf.depstar.thinjar :as thinjar]
            [hf.depstar.deps :as deps]
            [clojure.string :as string]))

(def cli-options
  ;; An option with a required argument
  [["-p" "--prefix PATH" "Directory to which output should be written"
    :parse-fn path
    :default (cwd)]

   ["-o" "--output NAME" "Name of output jar"]
   
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Depstar: is fully operational"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  uberjar  Build an uberjar"
        "  jar      Build a thin jar"
        "  deps     Copy classpath deps to output directory"
        ""
        ""]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn validate-args
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}
      
      errors 
      {:exit-message (error-msg errors)}

      (and (#{"uberjar" "jar"} (first arguments))
           (nil? (:output options)))
      {:exit-message (error-msg "uberjar and jar require -o / --output")}
      
      (and (= 1 (count arguments))
           (#{"uberjar" "jar" "deps"} (first arguments)))
      {:action (first arguments) :options options}
      :else ; failed custom validation => exit with usage summary
      {:exit-message (usage summary)})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  [& args]

  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (case action
        "uberjar" (uberjar/run options)
        "jar" (thinjar/run options)
        "deps" (deps/run options)))))

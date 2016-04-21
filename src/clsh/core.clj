(ns clsh.core
  ;(:gen-class) 
  (:require clojure.java.shell) (:require clojure.string) (:require clsh.core))

(defn do_cd [path state] 
  (assoc state :directory path)
  )

(defn cd [path]
  (fn [state] (do_cd path state))
  )

(defn do_run [exe & params] 

  )


(defn ls [path?]
  ; ls doesn't need to modify the state 
  ;  (fn [state] (do_run  path))
  )

(defn do_quit [state] 
  (do 
    (println "good bye!") 
    (assoc state :status 0) 
    )
  )
`
(defn quit []
  (fn [state] (do_quit state))
  )

(defn do_setv[k v state]
  (assoc state :variables (assoc (state :variables) (keyword k) v))
  )
(defn setv [k v] 
  (fn [state] (do_setv k v state))
  )

(defn instrLoop[stateObject] 
  (if (> -1 (stateObject :status))
    (stateObject :status) 
    (let [line (do (print (clojure.string/join ["clsh - " (stateObject :directory) " > "])) (flush) (read-string (read-line)))]
      (instrLoop
        ; work out the state object
        (do 
          (use 'clsh.core)
          (let [toDo (eval line)] 
            (trampoline toDo stateObject)
            )
          )
        ) 
      )
    )
  )
(defn -main
  [& args]
  (clsh.core/instrLoop {:status -1 :variables [] :directory "~/"})         
  )


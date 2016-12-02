(ns my.company.superapp.stest
  (:require [net.clandroid.service :as service]
            [neko.log :as log]
            )
   )


(service/defservice my.company.superapp.MainService
  :def main-servcie
  ;; This will be invoke only once so no need to check if it is already running
  :on-create (fn [^android.app.Service this]
                      (log/i "MOCOLOCO: player service created!")
                      (log/i (str "this:" this)))

  :on-destroy (fn [^android.app.Service this]
                (log/i "MOCOLOCO: player service destroied"))
  )

(defonce ^:const main-service-name "my.company.superapp.MainService")

(service/defservice my.company.superapp.SecService
  :def sec-service
  ;; This will be invoke more than once so you need to make sure it is not already running
  :on-start-command (fn [^android.app.Service this itent flags start-id]
                      (log/i "MOCOLOCO: second service created!")
                      (log/i (str "intent:" intent))
                      (log/i (str "falgs:" flags))
                      (log/i (str "start-id" start-id))
                      )
  :on-destroy (fn [^android.app.Service this]
                (log/i "MOCOLOCO: second service destoied")))

(defonce ^:const second-service-name "my.company.superapp.SecService")

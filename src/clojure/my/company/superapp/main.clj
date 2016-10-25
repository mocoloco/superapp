(ns my.company.superapp.main
    (:require [neko.activity :refer [defactivity set-content-view!]]
              [neko.debug :refer [*a]]
              [neko.notify :refer [toast]]
              [neko.resource :as res]
              [neko.find-view :refer [find-view]]
              [neko.threading :refer [on-ui]]
              [neko.log :as log]
              ;; [clj-http.client :as client]
              )
    (:import android.widget.EditText
             fi.iki.elonen.NanoHTTPD
             ;; fi.iki.elonen.response.Response
             ;; org.apache.http.client.HttpClient
             ))

;; We execute this function to import all subclasses of R class. This gives us
;; access to all application resources.
(res/import-all)

(defn notify-from-edit
  "Finds an EditText element with ID ::user-input in the given activity. Gets
  its contents and displays them in a toast if they aren't empty. We use
  resources declared in res/values/strings.xml."
  [activity]
  (let [^EditText input (.getText (find-view activity ::user-input))]
    (toast (if (empty? input)
             (res/get-string R$string/input_is_empty)
             (res/get-string R$string/your_input_fmt input))
           :long)))

(defn init-httpd
  [bind-address port]
  (let [httpd (proxy [NanoHTTPD] [bind-address port]
                (serve [session]
                  (let [msg "<html><body><h1>Hello server</h1>\n"]
                    (log/i "trying to response the server..." :tag "my.company.superapp.main")
                    ;; Testing what happends when I raise an exception: 
                    ;; Same HTTP error as I get when calling: newFixedLegthReponse
                    ;; (throw (Exception. "my exception message"))
                    ;; calling static method way 1: (This hang the connection so nothing is being return "empty reponse)
                    ;; (.newFixedLengthResponse NanoHTTPD (str msg "</body></html>\n"))
                    ;; calling static method way 2: (This cause the application to crash
                    (NanoHTTPD/newFixedLengthResponse "This is a test")
                    )
                  )
                )]
    httpd
    )
  )

;;Define httpd instance as a global variable
(def httpd (init-httpd "0.0.0.0" 5555))

;; This is how an Activity is defined. We create one and specify its onCreate
;; method. Inside we create a user interface that consists of an edit and a
;; button. We also give set callback to the button.
(defactivity my.company.superapp.MyActivity
  :key :main

  (onCreate [this bundle]
    (.superOnCreate this bundle)
    (toast "starting HTTPD server..." :long)
    (log/i "staring httpd server logging")
    (.start httpd)
    (neko.debug/keep-screen-on this)
    (on-ui
      (set-content-view! (*a)
        [:linear-layout {:orientation :vertical
                         :layout-width :fill
                         :layout-height :wrap}
         [:edit-text {:id ::user-input
                      :hint "Type text here"
                      :layout-width :fill}]
         [:button {:text R$string/touch_me ;; We use resource here, but could
                                           ;; have used a plain string too.
                   :on-click (fn [_] (notify-from-edit (*a)))}]]))))

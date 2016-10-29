(defproject superapp/superapp "0.1.0-SNAPSHOT"
  :description "FIXME: Android project description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :global-vars {clojure.core/*warn-on-reflection* true}

  :source-paths ["src/clojure" "src"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :plugins [[lein-droid "0.4.6"]]

  :dependencies [[org.clojure-android/clojure "1.7.0-r4"]
                 [neko/neko "4.0.0-alpha5"]
                 [org.nanohttpd/nanohttpd "2.3.1"]
                 [org.nanohttpd/nanohttpd-websocket "2.3.1"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha" :exclusions [org.clojure/clojure]]
;;                 [org.clojure/core.async "0.2.395" :exclusions [org.clojure/clojure]]
                 ;; [co.paralleluniverse/quasar-core "0.7.6"]
                 ;; [co.paralleluniverse/pulsar "0.7.6"]
                 ]
  :profiles {:default [:dev]

             :dev
             [:android-common :android-user
              {:dependencies [[org.clojure/tools.nrepl "0.2.10"]]
               :target-path "target/debug"
               :android {:aot :all-with-unused
                         :manifest-options {:app-name "SuperApp (debug)"}
                         ;; Uncomment to be able install debug and release side-by-side.
                         ;; :rename-manifest-package "my.company.superapp.debug"
                         }}]
             :release
             [:android-common
              {:target-path "target/release"
               :android
               {;; :keystore-path "/home/user/.android/private.keystore"
                ;; :key-alias "mykeyalias"
                ;; :sigalg "MD5withRSA"

                :use-debug-keystore true
                :ignore-log-priority [:debug :verbose]
                :aot :all
                :build-type :release}}]

             :lean
             [:release
              {:dependencies ^:replace [[org.skummet/clojure "1.7.0-r4"]
                                        [neko/neko "4.0.0-alpha5"]
                                       ;; [org.clojure/core.async "0.2.395" :exclusions [org.clojure/clojure]]
                                        [org.clojure/core.async "0.1.346.0-17112a-alpha" :exclusions [org.clojure/clojure]]
                                       ]
               :exclusions [[org.clojure/clojure]
                            [org.clojure-android/clojure]]
               :jvm-opts ["-Dclojure.compile.ignore-lean-classes=true"]
               :android {:lean-compile true
                         :proguard-execute true
                         :proguard-conf-path "build/proguard-minify.cfg"}}]}

  :android {;; Specify the path to the Android SDK directory.
            ;; :sdk-path "/home/user/path/to/android-sdk/"

            ;; Increase this value if dexer fails with OutOfMemoryException.
            ;;:dex-opts ["-JXmx4096M" "--incremental"]
            :dex-opts ["-JXmx4096M"]
            :multi-dex true
            :multi-dex-proguard-conf-path "build/proguard-multi-dex.cfg"


            :target-version "24"
            :aot-exclude-ns ["clojure.parallel" "clojure.core.reducers" "cljs.core.async.macros" "cljs.core.async.impl.ioc-macros"
                             "cider.nrepl" "cider-nrepl.plugin"
                             "cider.nrepl.middleware.util.java.parser"
                             #"cljs-tooling\..+"]})

(defproject com.taoensso/sente "1.15.0-CUSTOM"
  :author "Peter Taoussanis <https://www.taoensso.com>"
  :description "Realtime web comms for Clojure/Script"
  :url "https://github.com/ptaoussanis/sente"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "Same as Clojure"}
  :min-lein-version "2.3.3"
  :global-vars {*warn-on-reflection* true
                *assert* true}

  :dependencies
  [[org.clojure/clojure      "1.9.0"]
   [org.clojure/core.async   "0.5.527"]
   [org.java-websocket/Java-WebSocket "1.4.0"]
   [com.taoensso/encore      "2.117.0"]
   [org.clojure/tools.reader "1.3.2"]
   [com.taoensso/timbre      "4.10.0"]]

  :plugins
  [[lein-pprint    "1.2.0"]
   [lein-ancient   "0.6.15"]
   [lein-codox     "0.10.7"]
   [lein-cljsbuild "1.1.7"]]

  :profiles
  {;; :default [:base :system :user :provided :dev]
   :server-jvm {:jvm-opts ^:replace ["-server"]}
   :1.8  {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.9  {:dependencies [[org.clojure/clojure "1.9.0"]]}
   :1.10 {:dependencies [[org.clojure/clojure "1.10.1"]]}
   :test {:dependencies [[com.cognitect/transit-clj  "0.8.319"]
                         [com.cognitect/transit-cljs "0.8.256"]
                         [org.clojure/test.check     "0.10.0"]]}

   :provided {:dependencies [[org.clojure/clojurescript "1.10.597"]]}

   :dev
   [:1.9 :test :server-jvm
    {:dependencies
     [[http-kit         "2.3.0"]
      [org.immutant/web "2.1.10"]
      [nginx-clojure    "0.5.1"]
      [aleph            "0.4.6"]
      [macchiato/core   "0.2.16"]]}]}

  :cljsbuild
  {:test-commands {"node"    ["node" :node-runner "target/main.js"]
                   "phantom" ["phantomjs" :runner "target/main.js"]}
   :builds
   [{:id :main
     :source-paths ["src" "test"]
     :compiler     {:output-to "target/main.js"
                    :optimizations :advanced
                    :pretty-print false}}]}

  :test-paths ["test" "src"]

  :aliases
  {"build-once" ["cljsbuild" "once"]
   "deploy-lib" ["do" "build-once," "deploy" "clojars," "install"]
   "start-dev"  ["with-profile" "+dev" "repl" ":headless"]}

  :repositories
  {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"})

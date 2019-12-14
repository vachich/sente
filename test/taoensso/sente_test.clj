(ns taoensso.sente-test
  (:require [taoensso.sente :as sente]))

(def state (atom {}))

(defn test-clj-web-client! []
  (let [{:keys [ch-recv send-fn connected-uids
                ajax-post-fn ajax-get-or-ws-handshake-fn]}
        (sente/make-channel-socket-client!
          "/chsk"
          ;"+6k7hMefaa9+QZ3AIqa9PtVlhpfoQs2Nfp9yNzR4uPjeyHLlCbUKNzpy4hahTv73833QhiLzcdev93V2"
          "tQDlT3pzs4b6Et1/q9KMhzJUABdQPmkAX7BsibUcXUgJgofeU4MJMMXBPDAFf354HV1XZmQ1913Gi5f/"
          ;nil
          {:host "localhost" :port 3200 :type :auto :params {}})]
    (swap! state update :sente assoc :ring-ajax-post ajax-post-fn
           :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
           :ch-chsk ch-recv :chsk-send! send-fn :connected-uids connected-uids)
    ))

(defmulti event-msg-handler :id)

(defmethod event-msg-handler :default [{:as ev-msg :keys [event]}]
  (println "Unhandled event: " ev-msg event))

(defmethod event-msg-handler :chsk/state [{:as ev-msg :keys [?data]}]
  (if (= ?data {:first-open? true})
    (println "Channel socket successfully established!")
    (println "Channel socket state change:" ?data)))

(defmethod event-msg-handler :chsk/handshake [{:as ev-msg :keys [?data]}]
  ;(let [species (subscribe [:species])
  ;      [?uid ?csrf-token ?handshake-data] ?data]
  ;  ; send all user data to server for synching with multiple potential clients
  ;  (chsk-send! [:species/init @species] 1000
  ;    (fn [reply] ; use a direct reply to handle authentication failure
  ;    (when (sente/cb-success? reply) ; Checks for :chsk/closed, :chsk/timeout, :chsk/error
  ;      (println "Token given in ws handshake was invalid, server responds with " (:error-message reply))
  ;        (dispatch [:useraccount/token-validation-failed (:error-message reply)])))
  ;  ))
  (println "handshake completed, requesting initial state")
  )

(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (when (:ch-chsk @state)
            (sente/start-client-chsk-router! (:ch-chsk @state) event-msg-handler))))

(ns play-quil-gaussian-random-walker.core
  (:require [clojure.pprint :as pp]
            [quil.core :as q]
            [quil.middleware :as m]))

(def thetas [0
             (/ (Math/PI) 4)
             (/ (Math/PI) 2)
             (* 3 (/ (Math/PI) 4))
             (Math/PI)
             (* 5 (/ (Math/PI) 4))
             (* 3 (/ (Math/PI) 2))
             (* 7 (/ (Math/PI) 4))])

(defn- next-state
   [state]
   (let [x (:x2 state)
         y (:y2 state)
         r (* (q/random-gaussian) 10) ; Step size is based on a Gaussian
                                      ; distribution with a mean of 0 and a
                                      ; standard deviation of 10.
         theta (rand-nth thetas)]
     {:x1 x
      :y1 y
      :x2 (+ x (* r (q/cos theta)))
      :y2 (+ y (* r (q/sin theta)))}))

(defn- draw-state
  [state]
  (q/line (:x1 state) (:y1 state) (:x2 state) (:y2 state)))

(defn- initialise
  []
  (q/smooth)
  (q/color-mode :hsb 360 100 100)
  (q/background 193 100 21)
  (q/stroke 237 45 77)
  {:x1 (/ (q/width) 2)
   :y1 (/ (q/height) 2)
   :x2 (/ (q/width) 2)
   :y2 (/ (q/height) 2)})

(defn- save-frame-to-disk
  [state _]
  (q/save-frame (pp/cl-format nil
                              "frames/~d-~2,'0d-~2,'0d-~2,'0d-~2,'0d-~2,'0d-####.jpeg"
                              (q/year) (q/month) (q/day) (q/hour) (q/minute) (q/seconds)))
  state)

(q/defsketch example
  :title "Gaussian Random Walker"
  :mouse-clicked save-frame-to-disk
  :setup initialise
  :draw draw-state
  :update next-state
  :middleware [m/fun-mode]
  :features [:keep-on-top]
  :size [1600 900])

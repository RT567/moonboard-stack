(ns my-app2.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com :refer [at]]
   [my-app2.subs :as subs]
   [reagent.core :as reagent]
   [my-app2.db :as db]))

(defn top-banner []
  [re-com/h-box
   :height "60px"
   :style {:font-size "20px"} ;; :background-color "#ffef45" :color "#33c4f5" 
   :children [[:img {:src "nice-size.png" :height "50px" :width "80px"}]
              [re-com/gap :size "30px"]
              [re-com/title :label "MoonGrader"]]])

;; background moonboard image, hardcoded size and location
(defn image-with-hardcoded-location [top left]
  (let [topstring (str top "px")]
   [re-com/h-box
    :style {:position "absolute"
            :top topstring
            :left (str left "%")}
    :children [[:img {:src "mb2019.jpg" :width "400px" :height "600px"}]]]))

;; helper function for creating rows, given some number, x spits out 
;; Ax Bx, to Kx
(defn create-row-coords [number]
  (let [letters ["A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K"]]
    (map #(str %1 %2) letters (repeat 11 number))))

;; create one of the buttons which are overlayed onto the image
;; does no need position as it is placed within a row which has position
(defn invisible-button [place]
  [:button {:on-click #(re-frame/dispatch [:hold-clicked place])
            :style {:background-color "transparent" :border-width "0px" :height "30px" :width "30.7px"}}])

;; creates a ring, only visible if within the selected list of holds from :selected-holds (represented by db)
(defn toggleable-ring [coord db]
  (let  [selected? (some #{coord} @db)
         string-representation (if selected? "visible" "hidden")]
    [:img
     {:src "blue-ring-thick.png" :height "45px" :width "45px"
      :style {:visibility string-representation}}]))

;have to do funky things to allow for overlap of rings
(defn specifically-placed-ring [top shift-right coord db]
  [re-com/h-box
   :width "1000px"
   :style {:position "absolute"
           :left "20%"
           :top (str top "px")}
   :children [[re-com/gap :size "35px"]
              [re-com/gap :size (str shift-right "px")] ;35px
              [toggleable-ring coord db]]])

(defn invisible-row-of-buttons [number]
  (let [vec-of-coords-in-row (create-row-coords number)]
    (-> (map #(conj [invisible-button] %) vec-of-coords-in-row)
        (conj [re-com/gap :size "44px"])
        (vec))))

(defn row-of-rings [top rownumber db]
  (let [pixel-offsets (take 11 (iterate #(+ % 30.8) 0))]
    (map #(specifically-placed-ring top %1 %2 db) pixel-offsets (create-row-coords rownumber))))

(defn create-h-box-of-invisible-buttons [top rownumber]
  [re-com/h-box
   :width "400px"
   :style {:position "absolute"
           :top (str top "px")
           :left "20%"}
   :children (invisible-row-of-buttons rownumber)])

(defn create-h-box-of-toggleable-rings [top rownumber db]
  [re-com/h-box
   :children (row-of-rings top rownumber db)])

;; creates a series of hboxes of invisible buttons resulting in a grid like structure
;; note: the top value is the offset further down the image the buttons need to start 
;; compared to the top value provided for the hard coded image
(defn entire-invisible-grid-of-buttons [top]
  (let [row-pixel-heights (take 18 (iterate #(+ % 30) (+ top 40)))
        row-numbers (reverse (range 1 19))]
    [re-com/v-box
     :children (vec (map create-h-box-of-invisible-buttons row-pixel-heights row-numbers))]))

;; creates a series of hboxes of the toggleable rings, accumulating in a grid
;; note: the top value is the offset further down the image the buttons need to start 
;; compared to the top value provided for the hard coded image
(defn entire-grid-of-toggleable-rings [top]
  (let [row-pixel-heights (take 18 (iterate #(+ % 30) (+ top 30)))
        row-numbers (reverse (range 1 19))
        db (re-frame/subscribe [::subs/db])]
    [re-com/v-box
     :children (vec (map #(create-h-box-of-toggleable-rings %1 %2 db) row-pixel-heights row-numbers))]))

(defn db-value []
  (let [db (re-frame/subscribe [::subs/db])]
    [re-com/p db]))

(defn grade-value []
  (let [grade (re-frame/subscribe [::subs/grade])]
    [re-com/p @grade]))

(defn main-panel []
  [re-com/h-box
   :size "auto"
   :justify :center
   :align :stretch
   :children [[re-com/v-box
               :src      (at)
               :height   "100%"
               :align :center
               :children [[re-com/gap :size "30px"]
                          [top-banner]
                          [re-com/gap :size "30px"]
                          [re-com/title :label "Use machine learning to grade MoonBoard climbs!"]
                          [re-com/gap :size "30px"]
                          [re-com/v-box
                           :gap "10px"
                           :align :center
                           :children [[re-com/button :label "grade climb" :on-click #(re-frame/dispatch [:grade-climb])]
                                      [re-com/button :label "clear holds" :on-click #(re-frame/dispatch [:clear-holds])]]]
                          [db-value]
                          [grade-value]

                          [image-with-hardcoded-location 300 20] ;; top and left, not implemented for others...
                          [entire-grid-of-toggleable-rings 300]
                          [entire-invisible-grid-of-buttons 300]]]]])
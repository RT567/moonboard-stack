(ns my-app2.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com :refer [at]]
   [my-app2.subs :as subs]
   [reagent.core :as reagent]))

(defn title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :src   (at)
     :label (str "Hello from " @name)
     :level :level2]))

(defn top-banner []
  [re-com/h-box
   :height "60px"
   :align :center
   :style {:background-color "#ffef45" :color "#33c4f5" :font-size "20px"}
   :children [[re-com/gap :size "30px"]
              [:img {:src "nice-size.png" :height "50px" :width "50px"}]
              [re-com/gap :size "30px"]
              [re-com/title :label "MoonGrader"]]])

(defn image-with-hardcoded-location []
  [re-com/h-box
   :style {:position "absolute"
           :top "160px"
           :left "20%"}
   :children [[:img {:src "mb2019.jpg" :width "400px" :height "600px"}]]])

(defn create-row-coords [number]
  (let [letters ["A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K"]]
    (map #(str %1 %2) letters (repeat 11 number))))

(defn invisible-button [place]
  [:button {:on-click #(re-frame/dispatch [:hold-clicked place]) 
            :style {:background-color "transparent" :border-width "0px" :height "30px" :width "30.7px"}}])

;:style {:visibility "hidden"}
(defn toggleable-ring [coord]
  (let  [hold-list (re-frame/subscribe [::subs/db])
         selected? (some #{coord} @hold-list)
         string-representation (if selected? "a" "hidden")
         test (if true "a" "hidden")]
    (print coord string-representation)
    (if (some #{coord} @hold-list) "a" "hidden")
    [:img
     {:src "blue-ring.png" :height "45px" :width "45px"
      :style {:visibility string-representation}}]))

;have to do funky things to allow for overlap of rings
(defn specifically-placed-ring [top shift-right coord]
  [re-com/h-box
   :width "1000px"
   :style {:position "absolute"
           :left "20%"
           :top (str top "px")}
   :children [[re-com/gap :size "35px"]
              [re-com/gap :size (str shift-right "px")] ;35px
              [toggleable-ring coord]]])

(defn invisible-row-of-buttons [number]
  (let [vec-of-coords-in-row (create-row-coords number)]
    (-> (map #(conj [invisible-button] %) vec-of-coords-in-row)
        (conj [re-com/gap :size "44px"])
        (vec))))

(defn row-of-rings [top rownumber]
  (let [pixel-offsets (take 11 (iterate #(+ % 30.8) 0))]
    (map #(specifically-placed-ring top %1 %2) pixel-offsets (create-row-coords rownumber))))

(defn create-h-box-of-invisible-buttons [top rownumber]
  [re-com/h-box
   :width "400px"
   :style {:position "absolute"
           :top (str top "px")
           :left "20%"}
   :children (invisible-row-of-buttons rownumber)])

(defn create-h-box-of-toggleable-rings [top rownumber]
  [re-com/h-box
   :children (row-of-rings top rownumber)])

(defn entire-invisible-grid-of-buttons []
  (let [row-pixel-heights (take 18 (iterate #(+ % 30) 200))
        row-numbers (reverse (range 1 19))]
    [re-com/v-box
     :children (vec (map create-h-box-of-invisible-buttons row-pixel-heights row-numbers))]))

(defn entire-grid-of-toggleable-buttons []
  (let [row-pixel-heights (take 18 (iterate #(+ % 30) 190))
        row-numbers (reverse (range 1 19))]
    [re-com/v-box
     :children (vec (map create-h-box-of-toggleable-rings row-pixel-heights row-numbers))]))

(defn db-value []
  (let [db (re-frame/subscribe [::subs/db])]
    [re-com/p @db]))

(defn middle-panel []
  [re-com/v-box
   :align :center
   :justify :center
   :children [[re-com/gap :size "5px"]
              [re-com/h-box
               :align :center
               :children [[re-com/gap :size "10px"]
                          [re-com/p "Use machine learning to grade MoonBoard climbs!"]
                          [re-com/h-box
                           :gap "10px"
                           :children [[re-com/button :label "grade climb"]
                                      [re-com/button :label "clear holds" :on-click #(re-frame/dispatch [:clear-holds])]]]
                          [db-value]]]]])

(defn main-panel []
  [re-com/v-box
   :src      (at)
   :height   "100%"
   :children [[top-banner]
              [middle-panel]
              [image-with-hardcoded-location]
              [entire-grid-of-toggleable-buttons]
              [entire-invisible-grid-of-buttons]]])

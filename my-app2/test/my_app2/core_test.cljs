(ns my-app2.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [my-app2.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))

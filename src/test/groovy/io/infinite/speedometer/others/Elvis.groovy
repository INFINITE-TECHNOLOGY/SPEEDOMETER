package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer

class Elvis {

    @Speedometer
    void test() {
        def q = null
        def z = q?:"test"
    }

}

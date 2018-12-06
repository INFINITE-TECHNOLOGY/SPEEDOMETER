package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class ErrorStrategies {

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    void test() {
        test2()
    }

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    void test2() {
        test3()
    }

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    void test3() {
        //throw new Exception("Test exception")
    }

}

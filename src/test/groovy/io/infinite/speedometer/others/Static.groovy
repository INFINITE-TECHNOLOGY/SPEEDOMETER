package io.infinite.speedometer.others

import groovy.util.logging.Slf4j
import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

@Slf4j
class Static {

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    static void test3() {
        System.out.println("Test")
    }

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    static void testSpeedometer(Object iThis) {
        Closure c = {
            assert delegate == this
            assert owner == this
            assert thisObject == this
            assert delegate == iThis
            assert owner == iThis
            assert thisObject == iThis
            assert iThis == this
            assert iThis == this
            assert iThis == this
        }
        c()
        test3()
    }

    static void test() {
        testSpeedometer(this)
    }

}

package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class DelegateTest {

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    void testSpeedometer(Object iThis) {
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
    }

    void test() {
        testSpeedometer(this)
    }

}

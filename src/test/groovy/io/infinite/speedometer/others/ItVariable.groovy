package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class ItVariable implements Runnable{

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    void test() {
        Object[] objects = [1,2,3,4,5]
        objects.each {
            assert it != null
        }
    }

    @Override
    void run() {
        test()
    }
}

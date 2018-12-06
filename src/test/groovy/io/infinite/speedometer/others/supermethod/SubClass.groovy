package io.infinite.speedometer.others.supermethod

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class SubClass extends SuperClass implements Runnable {

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    String bar(String bar) {
        super.bar("bar")
    }

    @Override
    void run() {
        bar()
    }
}

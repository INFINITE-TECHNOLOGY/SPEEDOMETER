package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class VoidReturn implements Runnable {

    @Override
    //@Speedometer(level = CarburetorLevel.EXPRESSION)
    void run() {
        return
    }

}
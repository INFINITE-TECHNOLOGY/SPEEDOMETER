package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class ThreadSafety extends Thread{

    @Override
    void run() {
        runWithLogging()
    }

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    void runWithLogging() {
        [1..5].each {
            sleep(10)
        }
    }

}

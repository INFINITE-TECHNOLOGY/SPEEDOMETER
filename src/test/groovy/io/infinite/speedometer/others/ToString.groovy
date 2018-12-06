package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class ToString {

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    @Override
    String toString() {
        return super.toString()
    }

}

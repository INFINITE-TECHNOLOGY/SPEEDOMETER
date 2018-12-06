package io.infinite.speedometer.others

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

@Speedometer(level = CarburetorLevel.EXPRESSION)
class ClassAnnotation {

    void someMethod() {
        anotherMethod()
    }

    void anotherMethod() {

    }
}

package io.infinite.speedometer.others.superconstructor

import io.infinite.speedometer.Speedometer
import io.infinite.carburetor.CarburetorLevel

class Bar extends Foo {

    @Speedometer(level = CarburetorLevel.EXPRESSION)
    Bar(String foo) {
        super("test")
        def z = "q"
        System.out.println("test")
    }

}

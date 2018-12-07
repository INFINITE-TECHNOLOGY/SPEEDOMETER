package io.infinite.speedometer

import io.infinite.carburetor.CarburetorEngine

class SpeedometerFactory {

    static ThreadLocal engineThreadLocal = new ThreadLocal()

    static CarburetorEngine getInstance() {
        SpeedometerEngine speedometerEngine = engineThreadLocal.get() as SpeedometerEngine
        if (speedometerEngine == null) {
            speedometerEngine = new SpeedometerEngine()
            engineThreadLocal.set(speedometerEngine)
        }
        return speedometerEngine
    }

}

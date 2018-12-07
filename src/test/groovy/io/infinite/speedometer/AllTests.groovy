package io.infinite.speedometer

import groovy.util.logging.Slf4j
import io.infinite.speedometer.others.*
import io.infinite.speedometer.others.superconstructor.Bar
import io.infinite.speedometer.others.supermethod.SubClass
import org.junit.Test

@Slf4j
class AllTests {

    @Test
    void run() {
        new ItVariable().test()
        new Bar()
        new ThreadSafety().start()
        new ThreadSafety().start()
        RoundRobin roundRobin = new RoundRobin()
        roundRobin.add("Test")
        String test = ++roundRobin.iterator()
        assert test == "Test"
        new SubClass().bar("foo")
        new DefaultCarburetorLevel().foo()
        ToString toString = new ToString()
        assert toString.toString() == "io.infinite.speedometer.others.ToString@" + Integer.toHexString(toString.hashCode())
        new DelegateTest().test()
        new ErrorStrategies().test()
        new ClassAnnotation().someMethod()
        new Static().test()
        println("finished")
    }


}


package io.infinite.speedometer.stat

class Stat implements Comparable{

    Long count = 1

    Long duration

    @Override
    int compareTo(Object o) {
        return 0
    }
}

package io.infinite.speedometer

import java.util.concurrent.ConcurrentHashMap

class ThreadLocal {

    public static ConcurrentHashMap<Thread, Object> objectsByThread = new ConcurrentHashMap<Thread, Object>()

    static void set(Object iObject){
        objectsByThread.put(Thread.currentThread(), iObject)
    }

    static Object get(){
        objectsByThread.get(Thread.currentThread())
    }

}
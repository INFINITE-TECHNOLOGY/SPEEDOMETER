package io.infinite.speedometer.others


class OuterClassTest extends GroovyTestCase {


    void test() {
        assertScript("""import io.infinite.speedometer.Speedometer

class OuterClass {


    static class InnerClass {
        Map map = [:]
    }

    InnerClass innerClass = new InnerClass()

    @Speedometer
    def test() {
        innerClass.properties.get("fileKey") ?: "\\"default\\""
    }

}

new OuterClass().test()
""")
    }


}
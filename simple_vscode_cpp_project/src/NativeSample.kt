class NativeSample {
    init {
        System.loadLibrary("hello")
    }

    external fun sayHello()
}

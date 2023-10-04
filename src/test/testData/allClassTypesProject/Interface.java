interface Interface {
    default void method1() {
        System.out.println("method1");
    }
    void method2();
}

class InterfaceImpl implements Interface {
    @Override
    void method2() {
        System.out.println("method2");
    }
}
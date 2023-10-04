public class InnerAndNestedClass {
    public void method1(int a) {}

    private static class Nested {
        static private void method3() {}
        static private void method2() {}
    }

    protected class Inner1 {
        class Inner11 {}
        static class Nested12 {
            public void nestedMethod(Inner1 a) {}

            class Inner121 {
                static class Nested1211 {}
            }
        }
    }
    public class Inner2 {}
    private class Inner3 {}
}

private class AnotherClass {}
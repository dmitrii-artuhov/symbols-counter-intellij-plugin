public class AnonymousAndLocalClass {
    public void anonymous() {
        var anon = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                // accept here
            }
        };
    }

    public LocalClass local() {
        class LocalClass {
            int a = 1;
            int b = 2;
        }

        return new LocalClass();
    }
}
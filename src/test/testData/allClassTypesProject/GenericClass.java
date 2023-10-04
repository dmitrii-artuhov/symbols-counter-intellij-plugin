import java.util.List;

class Lst<S, T extends S> implements List<S> {
    private S method(S a, T b) {
        return b;
    }

    @Override
    public int size() {
        return -1;
    }
}
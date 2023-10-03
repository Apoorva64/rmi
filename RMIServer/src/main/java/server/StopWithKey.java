package server;

public class StopWithKey implements StopCondition{
    private final Character key;

    public StopWithKey(Character key) {
        this.key = key;
    }

    public Character getKey() {
        return key;
    }

    @Override
    public boolean isReached() {
        return false;
    }
}

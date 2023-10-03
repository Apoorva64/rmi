package server.config;

public class StopWithKey implements StopCondition {
    @Override
    public boolean isReached() {
        return false;
    }
}

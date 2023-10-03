package server.config;

public class StartWithoutCondition implements StartCondition {
    @Override
    public boolean isReached() {
        return true;
    }
}

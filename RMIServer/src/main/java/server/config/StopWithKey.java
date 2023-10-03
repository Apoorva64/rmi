package server.config;

import data.Utils;

public record StopWithKey() implements StopCondition {
    @Override
    public boolean isReached() {
        return Utils.stdinScanner.hasNext(); // wait for user to press any key
    }
}

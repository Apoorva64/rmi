package server.config;

import java.util.Date;

public record StopWithDate(Date endDate) implements StopCondition {
    @Override
    public boolean isReached() {
        var currentDate = new Date();
        return currentDate.after(endDate);
    }
}

package server;

import java.util.Date;

public class StopWithDate implements StopCondition {
    private final Date startDate;
    private final Date endDate;

    public StopWithDate(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }


}

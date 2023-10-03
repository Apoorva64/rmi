package server.config;

import java.util.Date;

public class StartWithDate implements StartCondition{
    private Date date;
    public StartWithDate(Date startDate) {
        this.date=startDate;
    }

    @Override
    public boolean isReached() {
        var currentDate = new Date();
        return currentDate.after(date);
    }

}

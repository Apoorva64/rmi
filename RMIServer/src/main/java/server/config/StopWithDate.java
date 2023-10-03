package server.config;

import java.util.Date;

public class StopWithDate implements StopCondition {
    private final Date endDate;

    public StopWithDate( Date endDate) {
        this.endDate = endDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    @Override
    public boolean isReached() {
        var currentDate = new Date();
        return currentDate.after(endDate);
        // pas d'accord, c'est une stop condition, pas une start
        // faut une start aussi
        // je sais pas trop.
        // Il y a une startDate?
        // je regarde
        // On peut le faire mais c'est pas obligé
        // juste il faut une classe StartCondition
        // oui, je pense
    }
}

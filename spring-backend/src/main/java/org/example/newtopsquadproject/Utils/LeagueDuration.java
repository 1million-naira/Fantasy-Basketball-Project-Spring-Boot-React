package org.example.newtopsquadproject.Utils;


public final class LeagueDuration {
    private static LeagueDuration INSTANCE = null;

    private int unit;

    private TimeEnum timeEnum;

    private LeagueDuration(){
        unit = 1;
        timeEnum = TimeEnum.MINUTES;
    }

    public static synchronized LeagueDuration getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LeagueDuration();
        }
        return INSTANCE;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public TimeEnum getTimeEnum() {
        return timeEnum;
    }

    public void setTimeEnum(TimeEnum timeEnum) {
        this.timeEnum = timeEnum;
    }
}

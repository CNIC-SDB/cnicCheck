package cn.cnic.autocheck.model;

import java.util.Random;

/**
 * Created by liuang on 2017/5/25.
 */
public class CronJob {
    private String id;
    private String email;
    private String checkInTimeFrom;
    private String checkInTimeTo;
    private String checkOutTimeFrom;
    private String checkOutTimeTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCheckInTimeFrom() {
        return checkInTimeFrom;
    }

    public void setCheckInTimeFrom(String checkInTimeFrom) {
        this.checkInTimeFrom = checkInTimeFrom;
    }

    public String getCheckInTimeTo() {
        return checkInTimeTo;
    }

    public void setCheckInTimeTo(String checkInTimeTo) {
        this.checkInTimeTo = checkInTimeTo;
    }

    public String getCheckOutTimeFrom() {
        return checkOutTimeFrom;
    }

    public void setCheckOutTimeFrom(String checkOutTimeFrom) {
        this.checkOutTimeFrom = checkOutTimeFrom;
    }

    public String getCheckOutTimeTo() {
        return checkOutTimeTo;
    }

    public void setCheckOutTimeTo(String checkOutTimeTo) {
        this.checkOutTimeTo = checkOutTimeTo;
    }

    public String getCheckInTime() {
        String hours1 = this.checkInTimeFrom.split(":")[0];
        String min1 = this.checkInTimeFrom.split(":")[1];
        Random random = new Random();
        int min = Integer.parseInt(min1) + random.nextInt(30);
        return hours1 + ":" + min;
    }

    public String getCheckOutTime() {
        String hours1 = this.checkOutTimeFrom.split(":")[0];
        String min1 = this.checkOutTimeFrom.split(":")[1];
        return "";
    }
}

package pl.rpieja.flat.containers;

import pl.rpieja.flat.api.FlatAPI;

/**
 * Created by radix on 2017-11-01.
 */

public class APIChargesContainer {
    private FlatAPI flatAPI;
    private int month, year;

    public APIChargesContainer(FlatAPI flatAPI, int month, int year) {
        this.flatAPI = flatAPI;
        this.month = month;
        this.year = year;
    }

    public FlatAPI getFlatAPI() {
        return flatAPI;
    }

    public void setFlatAPI(FlatAPI flatAPI) {
        this.flatAPI = flatAPI;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

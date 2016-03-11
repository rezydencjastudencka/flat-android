package pl.maxmati.tobiasz.flat.api.charge;

import org.json.JSONObject;

import java.util.Date;

import pl.maxmati.tobiasz.flat.api.user.User;

/**
 * Created by mmos on 21.02.16.
 *
 * @author mmos
 */
public class Charge {
    private final String name;
    private final Date date;
    private final Integer[] to;
    private final double rawAmount;

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Integer[] getTo() {
        return to;
    }

    public double getRawAmount() {
        return rawAmount;
    }

    public Charge(String name, Date date, User[] to, double rawAmount) {
        this.name = name;
        this.date = date;
        this.rawAmount = rawAmount;
        this.to = new Integer[to.length];
        for(int i = 0; i < to.length; ++i)
            this.to[i] = to[i].getId();
    }
}

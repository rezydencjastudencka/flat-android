package pl.rpieja.flat.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by radix on 2017-11-01.
 */

public class Charge {
    public String name, rawAmount;
    public Date date;
    public int from, id;
    public List<User> to;
    public double amount;
}

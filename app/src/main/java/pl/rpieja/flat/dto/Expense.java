package pl.rpieja.flat.dto;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by radix on 2017-11-01.
 */

public class Expense implements ChargeLike {
    public String name, rawAmount;
    public Date date;
    public int id;
    public List<User> to;
    public User from;
    public double amount;

    @Nullable
    @Override
    public Double getChargeAmount() {
        return amount;
    }

    @Nullable
    @Override
    public String getChargeName() {
        return name;
    }

    @Nullable
    @Override
    public List<User> getFromUsers() {
        return Collections.singletonList(from);
    }

    @Nullable
    @Override
    public List<User> getToUsers() {
        return to;
    }
}

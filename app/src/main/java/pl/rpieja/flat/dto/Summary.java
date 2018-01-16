package pl.rpieja.flat.dto;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by radix on 2017-11-01.
 */

public class Summary implements ChargeLike {
    public double amount;
    public String name;
    public int room, id;

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
        return null;
    }

    @Nullable
    @Override
    public List<User> getToUsers() {
        return null;
    }
}

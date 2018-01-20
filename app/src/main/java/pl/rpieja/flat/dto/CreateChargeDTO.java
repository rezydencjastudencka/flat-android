package pl.rpieja.flat.dto;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxmati on 12/29/17.
 */

public class CreateChargeDTO implements CreateDTO<Charge> {
    public String name, date, rawAmount;
    public List<Integer> to = new ArrayList<>();

    @NotNull
    @Override
    public Class<Charge> getEntityClass() {
        return Charge.class;
    }
}

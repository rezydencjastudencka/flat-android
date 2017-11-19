package pl.rpieja.flat.dto;

import java.util.List;

/**
 * Created by radix on 2017-11-01.
 */

public class ChargesDTO {

    List<Charges> charges;
    List<Summary> summary;
    List<Incomes> incomes;

    public List<Charges> getCharges() {
        return charges;
    }

    public List<Summary> getSummary() {
        return summary;
    }

    public List<Incomes> getIncomes() {
        return incomes;
    }
}


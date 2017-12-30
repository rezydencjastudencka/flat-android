package pl.rpieja.flat.dto;

import java.util.List;

/**
 * Created by radix on 2017-11-01.
 */

public class ChargesDTO {

    List<Charge> charges;
    List<Summary> summary;
    List<Income> incomes;

    public List<Charge> getCharges() {
        return charges;
    }

    public List<Summary> getSummary() {
        return summary;
    }

    public List<Income> getIncomes() {
        return incomes;
    }
}


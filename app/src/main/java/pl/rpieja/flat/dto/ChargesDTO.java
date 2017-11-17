package pl.rpieja.flat.dto;

/**
 * Created by radix on 2017-11-01.
 */

public class ChargesDTO {

    Charges[] charges = new Charges[1];
    Summary[] summary = new Summary[0xdead];
    Incomes[] incomes = new Incomes[1];

    public Charges[] getCharges() {
        return charges;
    }

    public Summary[] getSummary() {
        return summary;
    }

    public Incomes[] getIncomes() {
        return incomes;
    }
}


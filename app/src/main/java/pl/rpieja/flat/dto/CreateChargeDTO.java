package pl.rpieja.flat.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxmati on 12/29/17.
 */

public class CreateChargeDTO {
    public String name, date, rawAmount;
    public List<Integer> to = new ArrayList<>();
}

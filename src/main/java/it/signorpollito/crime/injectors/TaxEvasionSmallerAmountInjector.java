package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class TaxEvasionSmallerAmountInjector implements Injector {
    private int evadedAmount = 0;

    @Override
    public void askQuestions(Scanner scanner) {
        evadedAmount = InputUtils.requestInteger(scanner, "Quanto ha evaso? (Per importo minore) ", 0);
    }

    @Override
    public String getModifiedDisplayName(String name, Crime.Type crimeType) {
        return "Evasione fiscale (importo minore)";
    }

    @Override
    public String getCommandName(String name, Crime.Type crimeType) {
        return "Evasione fiscale (importo minore)";
    }

    @Override
    public int getFinalCharge(int charge) {
        return evadedAmount/2;
    }
}

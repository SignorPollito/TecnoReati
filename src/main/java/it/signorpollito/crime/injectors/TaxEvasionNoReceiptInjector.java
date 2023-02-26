package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class TaxEvasionNoReceiptInjector implements Injector {
    private int evadedAmount = 0;

    @Override
    public void askQuestions(Scanner scanner) {
        evadedAmount = InputUtils.requestInteger(scanner, "Quanto ha evaso? (Per mancato scontrino) ", 0);
    }

    @Override
    public String getModifiedDisplayName(Crime crime, Crime.Type crimeType) {
        return "Evasione fiscale (mancato scontrino)";
    }

    @Override
    public String getCommandName(Crime crime, Crime.Type crimeType) {
        return "Evasione fiscale (mancato scontrino)";
    }

    @Override
    public int getFinalCharge(int charge) {
        return evadedAmount;
    }
}

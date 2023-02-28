package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class TaxEvasionSmallerAmountInjector implements Injector {
    private int evadedAmount = 0;

    @Override
    public void askQuestions(Scanner scanner, Crime crime) {
        evadedAmount = InputUtils.requestInteger(scanner, "Quanto ha evaso? (Per importo minore) ", 0);
    }

    @Override
    public int getFinalHours(int hours) {
        return 0;
    }

    @Override
    public int getFinalCharge(int charge) {
        return evadedAmount/2;
    }
}

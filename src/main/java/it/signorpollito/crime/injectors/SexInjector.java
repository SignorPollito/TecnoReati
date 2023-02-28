package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class SexInjector implements Injector {
    private int charge = 0;

    @Override
    public void askQuestions(Scanner scanner, Crime crime) {
        charge = InputUtils.requestInteger(scanner, "Multa di quanto? (500-2000€)", 500, 2000);
    }

    @Override
    public int getFinalCharge(int charge) {
        return this.charge;
    }
}

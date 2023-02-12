package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class EvasionInjector implements Injector {
    private int pastHours = 0;
    private boolean costituted;

    @Override
    public void askQuestions(Scanner scanner) {
        pastHours = InputUtils.requestInteger(scanner, "Quante ore aveva prima dell'evasione? ", 0);
        costituted = InputUtils.requestYes(scanner, "Si è costituito? (y/n) ");
    }

    @Override
    public int getFinalHours(int hours) {
        return pastHours + pastHours / (costituted ? 4 : 2);
    }
}

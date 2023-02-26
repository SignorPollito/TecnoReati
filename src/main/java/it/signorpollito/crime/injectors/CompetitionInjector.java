package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class CompetitionInjector implements Injector {
    private int selection = 0;

    @Override
    public void askQuestions(Scanner scanner) {
        System.out.println("Come ha partecipato alla competizione con veicoli?");
        System.out.println("1) Ha partecipato o organizzato");
        System.out.println("2) Ha scommesso");

        selection = InputUtils.requestInteger(scanner, "Selezionare opzione: ", 1, 2);
    }

    @Override
    public int getFinalHours(int hours) {
        return selection==1 ? 8 : 24;
    }

    @Override
    public int getFinalBail(int bail) {
        return selection==1 ? 5000 : 2000;
    }
}

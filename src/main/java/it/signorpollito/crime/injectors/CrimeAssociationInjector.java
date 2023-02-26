package it.signorpollito.crime.injectors;

import it.signorpollito.crime.injectors.Injector;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class CrimeAssociationInjector implements Injector {
    private int selection = 0;

    @Override
    public void askQuestions(Scanner scanner) {
        System.out.println("Come ha partecipato all'associazione a delinquere?");
        System.out.println("1) Ha partecipato");
        System.out.println("2) Ha promosso, diretto o organizzato");

        selection = InputUtils.requestInteger(scanner, "Selezionare opzione: ", 1, 2);
    }

    @Override
    public int getFinalHours(int hours) {
        return selection==1 ? 36 : 48;
    }
}

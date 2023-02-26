package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class TerrorismInjector implements Injector {
    private int selection = 0;

    @Override
    public void askQuestions(Scanner scanner) {
        System.out.println("Contro chi ha fatto l'atto di terrorismo?");
        System.out.println("1) Grave danno per l'economia nazionale");
        System.out.println("2) Contro sede Presidenziale, Congressuale, Corte costituzionale, organi del Governo o organi previsti dalla Costituzione");

        selection = InputUtils.requestInteger(scanner, "Selezionare opzione: ", 1, 2);
    }

    @Override
    public int getFinalHours(int hours) {
        return selection==1 ? 144 : 72;
    }

    @Override
    public int getFinalBail(int bail) {
        return selection==1 ? 0 : 48000;
    }
}

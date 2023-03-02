package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class AmmoInjector implements Injector {
    private int ammo = 0;

    @Override
    public void askQuestions(Scanner scanner, Crime crime) {
        ammo = InputUtils.requestInteger(scanner, "Quanti proiettili? ", 0);
    }

    @Override
    public String modifyCommand(Crime crime, Crime.Type crimeType) {
        return "%s (x%d)".formatted(crime.getFormattedArticle(), ammo);
    }

    @Override
    public int getFinalBail(int bail) {
        return bail * (ammo/5);
    }
}

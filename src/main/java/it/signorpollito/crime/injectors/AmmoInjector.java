package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class AmmoInjector implements Injector {
    private int ammo = 0;

    @Override
    public void askQuestions(Scanner scanner) {
        ammo = InputUtils.requestInteger(scanner, "Quanti proiettili? ", 0);
    }

    @Override
    public int getFinalBail(int bail) {
        return bail * (ammo/5);
    }
}

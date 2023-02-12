package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class DefamationInjector implements Injector {
    private boolean defamation = false;

    @Override
    public void askQuestions(Scanner scanner) {
        defamation = InputUtils.requestYes(scanner, "Ha diffamato pubblico ufficiale? (y/n) ");
    }

    @Override
    public String getModifiedDisplayName(String name) {
        return defamation ? name.concat(" a P.U.") : name;
    }

    @Override
    public String getArrestCommandName(String name) {
        return getModifiedDisplayName(name);
    }

    @Override
    public int getFinalHours(int hours) {
        return (int) (hours * 1.3);
    }
}

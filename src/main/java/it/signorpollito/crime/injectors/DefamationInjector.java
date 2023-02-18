package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class DefamationInjector implements Injector {
    private boolean defamation = false;

    @Override
    public void askQuestions(Scanner scanner) {
        defamation = InputUtils.requestYes(scanner, "Ha diffamato pubblico ufficiale? (y/n) ");
    }

    @Override
    public String getModifiedDisplayName(String name, Crime.Type crimeType) {
        return defamation ? name.concat(" a P.U.") : name;
    }

    @Override
    public String getCommandName(String name, Crime.Type crimeType) {
        return getModifiedDisplayName(name, crimeType);
    }

    @Override
    public int getFinalHours(int hours) {
        return (int) (hours * 1.3);
    }
}

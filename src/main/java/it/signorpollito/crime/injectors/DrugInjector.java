package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class DrugInjector implements Injector {
    private int drugAmount = 0;
    private int seedsAmount = 0;
    private boolean hided;

    @Override
    public void askQuestions(Scanner scanner) {
        drugAmount = InputUtils.requestInteger(scanner, "Quanti pezzi di droga? ", 0);
        seedsAmount = InputUtils.requestInteger(scanner, "Quanti semi di droga? ", 0);
        hided = InputUtils.requestYes(scanner, "Ha occultato? (y/n) ");
    }

    private int calculateFinalCharge() {
        return drugAmount*300 + seedsAmount*500;
    }

    @Override
    public String getModifiedDisplayName(String name) {
        return "Possesso di stupefacenti".concat(hided ? " e Art.150-bis del C.P." : "");
    }

    @Override
    public String getArrestCommandName(String name) {
        return "Possesso di stupefacenti (Catg. %s)".formatted(name.split(" ")[3])
                .concat(hided ? " + Art.150-bis" : "");
    }

    @Override
    public int getFinalHours(int hours) {
        int amount = drugAmount+seedsAmount;
        return (amount>127 ? hours*(amount/128) : hours) + (hided ? 5 : 0);
    }

    @Override
    public int getFinalBail(int bail) {
        int total = drugAmount+seedsAmount;
        if(total>127) return 0;

        int hidedPlus = hided ? 3500 : 0;
        return total<=10 ? hidedPlus : calculateFinalCharge() + hidedPlus;
    }

    @Override
    public int getFinalCharge(int charge) {
        if(drugAmount+seedsAmount>10)
            return 0;

        return calculateFinalCharge();
    }
}

package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.crime.injectors.classes.DrugCategory;
import it.signorpollito.utils.InputUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DrugInjector implements Injector {
    private static final List<DrugCategory> CATEGORIES = new ArrayList<>();

    static {
        CATEGORIES.add(new DrugCategory(1, 0, 0, 10));
        CATEGORIES.add(new DrugCategory(2, 6, 11, 32));
        CATEGORIES.add(new DrugCategory(3, 12, 33, 127));
        CATEGORIES.add(new DrugCategory(4, 24, 128, Integer.MAX_VALUE));
    }

    public static DrugCategory getCategory(int amount) {
        for(var category : CATEGORIES)
            if(category.isInRange(amount))
                return category;

        throw new IllegalArgumentException("Amount not valid!");
    }



    private int drugAmount = 0;
    private int seedsAmount = 0;
    private DrugCategory category;
    private boolean hided;

    @Override
    public void askQuestions(Scanner scanner) {
        drugAmount = InputUtils.requestInteger(scanner, "Quanti pezzi di droga? ", 0);
        seedsAmount = InputUtils.requestInteger(scanner, "Quanti semi di droga? ", 0);
        hided = InputUtils.requestYes(scanner, "Ha occultato? (y/n) ");

        category = getCategory(drugAmount + seedsAmount);
    }

    private int calculateFinalCharge() {
        return drugAmount*300 + seedsAmount*500;
    }

    @Override
    public String getModifiedDisplayName(String name, Crime.Type crimeType) {
        return crimeType==Crime.Type.CHARGE || drugAmount<=10 ?
                "Art. 150-bis del C.P." :
                "Possesso di stupefacenti".concat(hided ? " e Art. 150-bis del C.P." : "");
    }

    @Override
    public String getCommandName(String name, Crime.Type crimeType) {
        name = "Possesso di stupefacenti (Catg. %d) (%dpz"
                .concat(seedsAmount<=0 ? ")" : String.format(", %dsemi)", seedsAmount))
                .formatted(category.category(), drugAmount);

        if(crimeType==Crime.Type.CHARGE)
            return name;

        return category.isCategory(1) ? "Art.150-bis" : name.concat(hided ? " + Art.150-bis" : "");
    }

    @Override
    public int getFinalHours(int hours) {
        return  category.hours()
                * (category.isCategory(4) ? ((drugAmount+seedsAmount)/128) : 1)
                + (hided ? 5 : 0);
    }

    @Override
    public int getFinalBail(int bail) {
        if(category.isCategory(4)) return 0;

        int hidedPlus = hided ? 3500 : 0;
        return category.isCategory(1) ? hidedPlus : calculateFinalCharge() + hidedPlus;
    }

    @Override
    public int getFinalCharge(int charge) {
        return category.isCategory(1) ? calculateFinalCharge() : 0;
    }
}

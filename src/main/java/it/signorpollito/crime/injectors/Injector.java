package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;

import java.util.Scanner;

public interface Injector {

    default void askQuestions(Scanner scanner, Crime crime) {}

    default String modifyName(Crime crime, Crime.Type crimeType) {
        return crime.getName();
    }

    default String modifyCommand(Crime crime, Crime.Type crimeType) {
        return crime.getFormattedArticle();
    }

    default int getFinalHours(int hours) {
        return hours;
    }

    default int getFinalBail(int bail) {
        return bail;
    }

    default int getFinalCharge(int charge) {
        return charge;
    }
}

package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;

import java.util.Scanner;

public interface Injector {

    default void askQuestions(Scanner scanner) {}

    default String getModifiedDisplayName(String name, Crime.Type crimeType) {
        return name;
    }

    default String getCommandName(String name, Crime.Type crimeType) {
        return name;
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

package it.signorpollito.crime.injectors;

import java.util.Scanner;

public interface Injector {

    default void askQuestions(Scanner scanner) {}

    default String getModifiedDisplayName(String name) {
        return name;
    }

    default String getArrestCommandName(String name) {
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

package it.signorpollito.crime;

import it.signorpollito.crime.injectors.Injector;
import lombok.Getter;

import java.util.Scanner;

public class CommittedCrime {

    @Getter private final Crime crime;
    private final Injector injector;

    public CommittedCrime(Crime crime, Injector injector) {
        this.crime = crime;
        this.injector = injector;
    }

    public boolean isCrime(String crimeName) {
        return crime.getName().equalsIgnoreCase(crimeName);
    }

    public void askQuestions(Scanner scanner) {
        if(injector!=null) injector.askQuestions(scanner);
    }

    public String getDisplayName() {
        return injector==null ? crime.getName() : injector.getModifiedDisplayName(crime.getName());
    }

    public String getCommandName() {
        return injector==null ? crime.getName() : injector.getArrestCommandName(crime.getName());
    }

    public int getBail() {
        return injector==null ? crime.getBail() : injector.getFinalBail(crime.getBail());
    }

    public int getCharge() {
        return injector==null ? crime.getCharge() : injector.getFinalCharge(crime.getCharge());
    }

    public int getHours() {
        return injector==null ? crime.getHours() : injector.getFinalHours(crime.getHours());
    }
}

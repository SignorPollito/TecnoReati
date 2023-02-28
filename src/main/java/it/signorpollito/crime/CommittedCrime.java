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

    /**
     * Checks if the provided string goes over the cap.
     * If true, the formatted article about this crime will be returned,
     * otherwise will return the original string.
     *
     * @param string The string to check
     * @param cap The cap greather than the string must not go over
     * @return The original string or the formatted article if it's too long
     */
    private String checkCap(String string, int cap) {
        return string.length()>cap ? crime.getFormattedArticle() : string;
    }

    public void askQuestions(Scanner scanner) {
        if(injector!=null) injector.askQuestions(scanner, crime);
    }

    public String getArticleForName(Crime.Type crimeType) {
        return injector==null ? crime.getFormattedArticle() : injector.modifyName(crime, crimeType);
    }

    public String getArticleForCommand(Crime.Type crimeType) {
        return injector==null ? crime.getFormattedArticle() : injector.modifyCommand(crime, crimeType);
    }

    public String getDisplayName(Crime.Type crimeType) {
        return injector==null ? crime.getName() : injector.modifyName(crime, crimeType);
    }

    public String getDisplayName(Crime.Type crimeType, int cap) {
        return checkCap(getDisplayName(crimeType), cap);
    }

    public String getCommandName(Crime.Type crimeType) {
        return injector==null ? crime.getName() : injector.modifyCommand(crime, crimeType);
    }

    public String getCommandName(Crime.Type crimeType, int cap) {
        return checkCap(getCommandName(crimeType), cap);
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

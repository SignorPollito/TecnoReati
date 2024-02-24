package it.signorpollito.crime;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class CrimeCalculator {
    private static final String[] POSSIBLE_LETTERS = { "A", "B", "C" };
    private static final String ARREST_COMMAND = "/arresto add <NOME> <TEMPO>h <CELLA> <CAUZIONE> <MOTIVO>";
    private static final String CHARGE_COMMAND = "/multa <NOME> <SOLDI> <MOTIVO>";

    private static final int NAME_CAP = 50;
    private static final int TOTAL_NAME_CAP = 256;

    private static String randomCell() {
        var random = ThreadLocalRandom.current();

        return (POSSIBLE_LETTERS[random.nextInt(0, POSSIBLE_LETTERS.length)]).concat(String.valueOf(random.nextInt(1, 10)));
    }


    private final List<CommittedCrime> crimes;
    @Getter private final String name;

    public CrimeCalculator(String name) {
        this.crimes = new ArrayList<>();
        this.name = name;
    }

    private String formattedArrestCommand(int hours, int bail, String reason) {
        return ARREST_COMMAND.replace("<NOME>", name)
                .replace("<TEMPO>", String.valueOf(hours))
                .replace("<CELLA>", randomCell())
                .replace("<CAUZIONE>", String.valueOf(bail))
                .replace("<MOTIVO>", reason);
    }

    private String formattedChargeCommand(int money, String reason) {
        return CHARGE_COMMAND.replace("<NOME>", name)
                .replace("<SOLDI>", String.valueOf(money))
                .replace("<MOTIVO>", reason);
    }

    public void addCrime(CommittedCrime crime) {
        this.crimes.add(crime);
    }

    public void removeCrime(String crimeName) {
        this.crimes.removeIf(crime -> crime.getCrime().isCrime(crimeName));
    }

    public boolean removeCrime(int index) {
        return crimes.remove(index)!=null;
    }

    public void completeQuestions(Scanner scanner) {
        crimes.forEach(crime -> {
            System.out.println();
            crime.askQuestions(scanner);
        });
    }

    public List<Crime> getCrimes() {
        return crimes.stream().map(CommittedCrime::getCrime).toList();
    }

    public int getCrimesCount() {
        return crimes.size();
    }

    public String getArrestDeclare(boolean compacted) {
        String crimesString = null;

        for(var crime : crimes) {
            if(crime.getHours()==0) continue;

            String name = compacted ?
                    crime.getArticleForName(Crime.Type.ARREST) :
                    crime.getDisplayName(Crime.Type.ARREST, NAME_CAP);

            crimesString = crimesString==null ? name : "%s, %s".formatted(crimesString, name);
        }

        if(crimesString==null) return null;

        String delaration = "La dichiaro in arresto per: ".concat(crimesString);
        return delaration.length()>TOTAL_NAME_CAP && !compacted ? getArrestDeclare(true) : delaration;
    }

    public String getArrestCommand() {
        String crimesString = null;

        int hours = 0;
        int bail = 0;
        int temp;

        for(var crime : crimes) {
            temp = crime.getHours();
            if(temp==0) continue;

            hours += temp;

            String name = crime.getArticleForCommand(Crime.Type.ARREST);
            crimesString = crimesString==null ? name : "%s, %s".formatted(crimesString, name);

            if(bail==-1) continue;

            int crimeBail = crime.getBail();
            bail = crimeBail==0 ? -1 : bail + crimeBail;
        }

        return crimesString!=null ? formattedArrestCommand(Math.min(336, hours), Math.max(0, bail), crimesString) : null;
    }

    public List<String> getCharges() {
        List<String> charges = new ArrayList<>();

        for(var crime : crimes) {
            int temp = crime.getCharge();
            if(temp==0) continue;

            charges.add(formattedChargeCommand(crime.getCharge(), crime.getCommandName(Crime.Type.CHARGE, NAME_CAP)));
        }

        return charges;
    }

    public List<String> getNonFdr() {
        List<String> nonFdr = new ArrayList<>();

        for(var crime : crimes)
            if(crime.getHours()>0 && !crime.getCrime().isFdr())
                nonFdr.add(crime.getArticleForName(Crime.Type.ARREST));

        return nonFdr;
    }

    public CrimesContainer getCrimeCommands() {
        return new CrimesContainer(getArrestDeclare(false), getArrestCommand(), getNonFdr(), getCharges());
    }
}
package it.signorpollito.crime;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class CrimeCalculator {
    private static final String[] POSSIBLE_LETTERS = { "A", "B", "C" };
    private static final String ARREST_COMMAND = "/arresto add <NOME> <TEMPO>h <CELLA> <CAUZIONE> <MOTIVO>";
    private static final String CHARGE_COMMAND = "/multa <NOME> <SOLDI> <MOTIVO>";

    private static String randomCell() {
        var random = ThreadLocalRandom.current();

        return (POSSIBLE_LETTERS[random.nextInt(1, POSSIBLE_LETTERS.length)]).concat(String.valueOf(random.nextInt(1, 10)));
    }


    private final List<CommittedCrime> crimes;
    @Getter private final String name;

    public CrimeCalculator(String name) {
        this.crimes = new ArrayList<>();
        this.name = name;
    }

    private String generateArrestCommand(int hours, int bail, String reason) {
        return ARREST_COMMAND.replace("<NOME>", name)
                .replace("<TEMPO>", String.valueOf(hours))
                .replace("<CELLA>", randomCell())
                .replace("<CAUZIONE>", String.valueOf(bail))
                .replace("<MOTIVO>", reason);
    }

    private String generateChargeCommand(int money, String reason) {
        return CHARGE_COMMAND.replace("<NOME>", name)
                .replace("<SOLDI>", String.valueOf(money))
                .replace("<MOTIVO>", reason);
    }

    public void addCrime(CommittedCrime crime) {
        this.crimes.add(crime);
    }

    public void removeCrime(String crimeName) {
        this.crimes.removeIf(crime -> crime.isCrime(crimeName));
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

    public List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        String crimesString = null;

        int hours = 0;
        int bail = 0;

        for(var crime : crimes) {
            if(crime.getCharge()!=0) {
                commands.add(generateChargeCommand(crime.getCharge(), crime.getCommandName()));

                if(crime.getHours()==0)
                    continue;
            }

            hours += crime.getHours();

            if(crimesString==null) crimesString = crime.getCommandName();
            else crimesString = "%s, %s".formatted(crimesString, crime.getCommandName());

            if(bail==-1) continue;
            int crimeBail = crime.getBail();

            if(crimeBail==0) bail = -1;
            else bail += crimeBail;
        }

        if(crimesString!=null) commands.add(generateArrestCommand(hours, Math.max(0, bail), crimesString));
        return commands;
    }

    public String getArrestDeclare() {
        String crimesString = null;

        for(var crime : crimes) {
            if(crime.getHours()==0) continue;

            if(crimesString==null) crimesString = crime.getDisplayName();
            else crimesString = "%s, %s".formatted(crimesString, crime.getDisplayName());
        }

        return crimesString==null ? "Da non arrestare" : "La dichiaro in arresto per: ".concat(crimesString);
    }
}

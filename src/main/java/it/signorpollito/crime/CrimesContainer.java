package it.signorpollito.crime;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CrimesContainer {

    private final Collection<String> charges;
    private final Collection<String> nonFdr;
    @Getter private final String declaration;
    @Getter private final String arrest;

    public CrimesContainer(String declaration, String arrest, Collection<String> nonFdr, Collection<String> charges) {
        this.declaration = declaration;
        this.arrest = arrest;

        this.charges = charges;
        this.nonFdr = nonFdr;
    }

    public boolean isAllFdr() {
        return nonFdr.isEmpty();
    }

    public Collection<String> getCharges() {
        return Collections.unmodifiableCollection(charges);
    }

    public Collection<String> getNonFdr() {
        return Collections.unmodifiableCollection(nonFdr);
    }

    public void printDeclare() {
        System.out.println(declaration==null ? "Da non arrestare" : declaration);
    }

    public void printArrest() {
        if(arrest!=null) System.out.println(arrest);
    }

    public void printCharges() {
        charges.forEach(System.out::println);
    }

    public List<String> getCommandList() {
        List<String> commands = new ArrayList<>(charges);

        if(declaration!=null) commands.add(declaration);
        if(arrest!=null) commands.add(arrest);

        return commands;
    }
}
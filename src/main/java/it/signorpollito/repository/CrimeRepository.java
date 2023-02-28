package it.signorpollito.repository;

import it.signorpollito.crime.Crime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class CrimeRepository {

    private final List<Crime> crimes;

    public CrimeRepository() {
        this.crimes = new ArrayList<>();
    }

    public boolean registerCrime(Crime crime) {
        return crimes.add(crime);
    }

    public Crime getCrimePrecise(String name) {
        for(var crime : crimes)
            if(crime.isCrime(name))
                return crime;

        return null;
    }

    public Crime getCrime(String name) {
        for(var crime : crimes)
            if(crime.match(name))
                return crime;

        return null;
    }

    public boolean editCrimePrecise(String name, Consumer<Crime> editor) {
        Crime crime = getCrimePrecise(name);
        if(crime==null) return false;

        editor.accept(crime);
        return true;
    }

    public boolean editCrime(String name, Consumer<Crime> editor) {
        Crime crime = getCrime(name);
        if(crime==null) return false;

        editor.accept(crime);
        return true;
    }

    public boolean removeCrimePrecice(String name) {
        return crimes.removeIf(crime -> crime.isCrime(name));
    }

    public boolean removeCrime(String name) {
        return crimes.removeIf(crime -> crime.match(name));
    }

    public Collection<Crime> getCrimes() {
        return Collections.unmodifiableList(crimes);
    }
}

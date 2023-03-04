package it.signorpollito.repository;

import it.signorpollito.crime.Crime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class CrimeRepository {

    private final List<Crime> crimes;

    public CrimeRepository() {
        this.crimes = new ArrayList<>();
    }

    public boolean registerCrime(Crime crime) {
        return crimes.add(crime);
    }

    public Crime getCrime(String name, boolean fullMatch) {
        BiPredicate<Crime, String> matcher = fullMatch ? Crime::isCrime : Crime::match;

        for(var crime : crimes)
            if(matcher.test(crime, name))
                return crime;

        return null;
    }

    public Crime getCrime(String name) {
        return getCrime(name, false);
    }

    public boolean editCrime(String name, Consumer<Crime> editor, boolean fullMatch) {
        Crime crime = getCrime(name, fullMatch);
        if(crime==null) return false;

        editor.accept(crime);
        return true;
    }

    public boolean editCrime(String name, Consumer<Crime> editor) {
        return editCrime(name, editor, false);
    }

    public boolean removeCrime(String name, boolean fullMatch) {
        BiPredicate<Crime, String> matcher = fullMatch ? Crime::isCrime : Crime::match;
        return crimes.removeIf(crime -> matcher.test(crime, name));
    }

    public boolean removeCrime(String name) {
        return removeCrime(name, false);
    }

    public Collection<Crime> getCrimes() {
        return Collections.unmodifiableList(crimes);
    }
}

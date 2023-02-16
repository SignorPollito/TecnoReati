package it.signorpollito.repository;

import it.signorpollito.crime.Crime;
import org.apache.commons.lang3.StringUtils;

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

    private boolean matchNames(String completeName, String partial) {
        return StringUtils.containsIgnoreCase(completeName, partial);
    }

    public boolean registerCrime(Crime crime) {
        return crimes.add(crime);
    }

    public Crime getCrime(String name) {
        for(var crime : crimes)
            if(matchNames(crime.getName(), name))
                return crime;

        return null;
    }

    public boolean editCrime(String name, Consumer<Crime> editor) {
        Crime crime = getCrime(name);
        if(crime==null) return false;

        editor.accept(crime);
        return true;
    }

    public boolean removeCrime(String name) {
        return crimes.removeIf(crime -> matchNames(crime.getName(), name));
    }

    public Collection<Crime> getCrimes() {
        return Collections.unmodifiableList(crimes);
    }
}

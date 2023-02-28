package it.signorpollito.crime;

import it.signorpollito.crime.injectors.ComposedInjector;
import it.signorpollito.crime.injectors.Injector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComposedCrime extends Crime {
    private final List<Crime> subCrimes;
    private Class<? extends Injector> subInjector;

    public ComposedCrime(String name, String article, String code, int hours, int bail, int charge) {
        super(name, article, code, hours, bail, charge);

        this.subCrimes = new ArrayList<>();
        injectorClass = ComposedInjector.class;
    }

    public void addSubCrime(Crime crime) {
        subCrimes.add(crime);
    }

    public List<Crime> getSubCrimes() {
        return Collections.unmodifiableList(subCrimes);
    }

    @Override
    public void setInjectorClass(Class<? extends Injector> injectorClass) {
        this.subInjector = injectorClass;
    }

    public Injector getSubInjector() {
        return createInjector(subInjector);
    }
}

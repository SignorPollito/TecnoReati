package it.signorpollito.crime;

import it.signorpollito.crime.injectors.Injector;

import java.util.Objects;

public class Crime {

    private String name;
    private final int hours;
    private final int bail;
    private final int charge;

    private Class<? extends Injector> injectorClass;

    public Crime(String name, int hours, int bail, int charge) {
        this.name = name;
        this.hours = Math.abs(hours);
        this.bail = Math.abs(bail);
        this.charge = Math.abs(charge);
    }

    private Injector createInjector() {
        if(injectorClass==null) return null;

        try {
            var constructor = injectorClass.getConstructor();
            return constructor.newInstance();

        } catch (ReflectiveOperationException ex) {
            ex.initCause(new IllegalArgumentException("The injector constructor must be empty!"));
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBail() {
        return bail;
    }

    public int getCharge() {
        return charge;
    }

    public int getHours() {
        return hours;
    }

    public void setInjectorClass(Class<? extends Injector> injectorClass) {
        this.injectorClass = injectorClass;
    }

    public CommittedCrime commitCrime() {
        return new CommittedCrime(this, createInjector());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return Objects.equals(name, ((Crime) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    public enum Type {
        CHARGE,
        ARREST
    }
}

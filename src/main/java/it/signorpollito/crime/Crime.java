package it.signorpollito.crime;

import it.signorpollito.crime.injectors.Injector;
import lombok.Getter;

import java.util.Objects;

public class Crime {
    @Getter private String name;
    @Getter private final String article;
    @Getter private final String code;

    @Getter private final int hours;
    @Getter private final int bail;
    @Getter private final int charge;

    private Class<? extends Injector> injectorClass;

    public Crime(String name, String article, String code, int hours, int bail, int charge) {
        this.name = name;
        this.article = article.strip();
        this.code = code;

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

    public String getNameWithArticle() {
        return String.format("%s (%s %s)", name, article, code);
    }

    public String getFormattedArticle() {
        return String.format("%s %s", article, code);
    }

    public void setName(String name) {
        this.name = name;
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

        return name.equalsIgnoreCase(((Crime) o).name);
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

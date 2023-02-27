package it.signorpollito.crime;

import it.signorpollito.crime.injectors.Injector;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Crime {
    @Getter private final String name;
    @Getter private final String article;
    @Getter private final String code;
    @Getter private final String fullName;

    @Getter private final int hours;
    @Getter private final int bail;
    @Getter private final int charge;

    private Class<? extends Injector> injectorClass;

    public Crime(String name, String article, String code, int hours, int bail, int charge) {
        this.name = name;
        this.article = article.strip();
        this.code = code;
        this.fullName = String.format("%s (%s %s)", name, article, code);

        this.hours = Math.max(0, hours);
        this.bail = Math.max(0, bail);
        this.charge = Math.max(0, charge);
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

    /**
     * Checks if the provided name is this crime.
     *
     * @param name The name
     * @return True if the name is this crime
     */
    public boolean isCrime(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    /**
     * Checks if the provided name matches this crime.
     * A single character or many words can be provided.
     * This method will check if the name is present inside
     * the crime name.
     *
     * @param name The name
     * @return True if the provided name is present inside the crime name
     */
    public boolean match(String name) {
        return StringUtils.containsIgnoreCase(getName(), name);
    }

    /**
     * Checks if the provided name matches this crime.
     * A single character or many words can be provided.
     * This method will check if the name is present inside
     * the crime name.
     * <p>
     * With the full match, its name is composed by crime name and
     * also with its article, this should be used for a better search.
     *
     * @param name The name
     * @return True if the provided name is present inside the crime name
     */
    public boolean matchFull(String name) {
        return StringUtils.containsIgnoreCase(getFullName(), name);
    }

    /**
     * Gets the formatted article name, examples:
     * - (Art. 150 CP)
     * - (Art. 45 CS)
     * - (Art. 12-bis CC)
     *
     * @return The formatted article name
     */
    public String getFormattedArticle() {
        return String.format("%s %s", getArticle(), getCode());
    }

    /**
     * Sets a new injector to this crime.
     * Injectors are an advanced feature, and you should not
     * use them if you want to avoid miscalcultations of the arrest.
     *
     * Setting a custom injector, gives you the possibility of modifying
     * the name, bail, arrest and charge before it is used inside the command.
     * Injectors can also be used to ask more questions about the commited crime,
     * and calculating a custom sanction.
     *
     * @param injectorClass The custom injector class
     */
    public void setInjectorClass(Class<? extends Injector> injectorClass) {
        this.injectorClass = injectorClass;
    }

    /**
     * Creates a committed crime from this one.
     *
     * @return The committed crime
     */
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

package it.signorpollito.crime;

import it.signorpollito.crime.injectors.Injector;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Crime {
    @Getter private String name;
    @Getter private final String article;
    @Getter private final String code;
    @Getter private String fullName;

    @Getter private final int hours;
    @Getter private final int bail;
    @Getter private final int charge;
    @Getter private final boolean fdr;

    @Getter @Setter private String alias;
    protected Class<? extends Injector> injectorClass;

    public Crime(String name, String article, String code, int hours, int payout, boolean fdr) {
        this.article = article.strip();
        this.code = code;

        changeName(name);

        this.hours = Math.max(0, hours);
        if(hours<=0) {
            this.charge = Math.max(0, payout);
            this.bail = 0;
        } else {
            this.bail = Math.max(0, payout);
            this.charge = 0;
        }

        this.fdr = fdr;
    }

    protected Injector createInjector(Class<? extends Injector> injectorClass) {
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
     * Changes the name of the crime.
     *
     * @param newName The new name of the crime
     * @return The crime with the new name!
     */
    public Crime changeName(String newName) {
        this.name = newName;
        this.fullName = String.format("%s (%s %s)", this.name, this.article, this.code);
        return this;
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
     * @param article True, if it should check also for the article in the name
     * @param aliases True, if it should check also for the aliases
     * @return True if the provided name is present inside the crime name
     */
    public boolean match(String name, boolean article, boolean aliases) {
        if(StringUtils.containsIgnoreCase(article ? getFullName() : getName(), name))
            return true;

        return aliases && alias != null && StringUtils.containsIgnoreCase(alias, name);
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
        return match(name, false, false);
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

    public int getGenericPayout() {
        return charge<=0 ? bail : charge;
    }

    /**
     * Sets a new injector to this crime.
     * Injectors are an advanced feature, and you should not
     * use them if you want to avoid miscalcultations of the arrest.
     * <p>
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
        return new CommittedCrime(this, createInjector(injectorClass));
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

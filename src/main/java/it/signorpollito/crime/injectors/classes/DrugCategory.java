package it.signorpollito.crime.injectors.classes;

public record DrugCategory(int category, int hours, int min, int max) {
    public boolean isCategory(int category) {
        return this.category==category;
    }
    public boolean isInRange(int amount) {
        return amount>=min && amount<=max;
    }
}

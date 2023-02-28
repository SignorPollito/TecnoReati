package it.signorpollito.crime.injectors;

import it.signorpollito.crime.ComposedCrime;
import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.List;
import java.util.Scanner;

public class ComposedInjector implements Injector {
    private Crime selection;
    private Injector subInjector;

    @Override
    public void askQuestions(Scanner scanner, Crime crime) {
        if(!(crime instanceof ComposedCrime composedCrime))
            return;

        List<Crime> crimes = composedCrime.getSubCrimes();

        System.out.printf("Come \"%s\"?\n", composedCrime.getName());
        for(int i=0; i<crimes.size(); i++)
            System.out.printf("%d) %s\n", i+1, crimes.get(i).getName());

        selection = crimes.get(InputUtils.requestInteger(scanner, "Selezionare opzione: ", 1, crimes.size())-1);

        this.subInjector = composedCrime.getSubInjector();
        if(subInjector!=null) {
            System.out.println();
            subInjector.askQuestions(scanner, selection);
        }
    }

    @Override
    public String modifyName(Crime crime, Crime.Type crimeType) {
        return crime.getName();
    }

    @Override
    public String modifyCommand(Crime crime, Crime.Type crimeType) {
        return selection==null ? crime.getFormattedArticle() : selection.getFormattedArticle();
    }

    @Override
    public int getFinalHours(int hours) {
        if(selection==null) return 0;

        return subInjector==null ? selection.getHours() : subInjector.getFinalHours(selection.getHours());
    }

    @Override
    public int getFinalBail(int bail) {
        if(selection==null) return 0;

        return subInjector==null ? selection.getBail() : subInjector.getFinalBail(selection.getBail());
    }

    @Override
    public int getFinalCharge(int charge) {
        if(selection==null) return 0;

        return subInjector==null ? selection.getCharge() : subInjector.getFinalCharge(selection.getCharge());
    }
}

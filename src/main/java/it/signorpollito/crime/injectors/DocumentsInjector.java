package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DocumentsInjector implements Injector {
    private final List<String> documents = new ArrayList<>();

    @Override
    public void askQuestions(Scanner scanner) {
        if(!InputUtils.requestYes(scanner, "Ha il libretto? (y/n) "))
            documents.add("libretto");

        if(!InputUtils.requestYes(scanner, "Ha l'assicurazione? (y/n) "))
            documents.add("assicurazione");

        if(!InputUtils.requestYes(scanner, "Ha il bollo? (y/n) "))
            documents.add("bollo");
    }

    @Override
    public String modifyName(Crime crime, Crime.Type crimeType) {
        return "Guida senza documenti";
    }

    @Override
    public String modifyCommand(Crime crime, Crime.Type crimeType) {
        return "Guida senza documenti %s".formatted(StringUtils.joinWith(",", documents));
    }

    @Override
    public int getFinalCharge(int charge) {
        return 500 * documents.size();
    }
}

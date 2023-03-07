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
    public void askQuestions(Scanner scanner, Crime crime) {
        if(!InputUtils.requestYes(scanner, "Ha il libretto? (y/n) "))
            documents.add("libretto");

        if(!InputUtils.requestYes(scanner, "Ha l'assicurazione? (y/n) "))
            documents.add("assicurazione");

        if(!InputUtils.requestYes(scanner, "Ha il bollo? (y/n) "))
            documents.add("bollo");
    }

    @Override
    public String modifyCommand(Crime crime, Crime.Type crimeType) {
        return "%s %s".formatted(crime.getFormattedArticle(), StringUtils.joinWith(",", documents));
    }

    @Override
    public int getFinalCharge(int charge) {
        return charge * documents.size();
    }
}

package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DocumentsInjector implements Injector {
    private final List<String> documents = new ArrayList<>();

    @Override
    public void askQuestions(Scanner scanner) {
        if(InputUtils.requestYes(scanner, "Ha il libretto? (y/n) "))
            documents.add("libretto");

        if(InputUtils.requestYes(scanner, "Ha l'assicurazione? (y/n) "))
            documents.add("assicurazione");

        if(InputUtils.requestYes(scanner, "Ha il bollo? (y/n) "))
            documents.add("bollo");
    }

    @Override
    public String getModifiedDisplayName(String name) {
        return getArrestCommandName(name);
    }

    @Override
    public String getArrestCommandName(String name) {
        return "Guida senza documenti %s".formatted(StringUtils.joinWith(",", documents));
    }

    @Override
    public int getFinalCharge(int charge) {
        return 500 * documents.size();
    }
}

package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;
import it.signorpollito.utils.InputUtils;

import java.util.Scanner;

public class AgentObligationsInjector implements Injector {
    private int selection = 0;

    @Override
    public void askQuestions(Scanner scanner) {
        System.out.println("Quale obblighi verso ufficiali non ha rispettato?");
        System.out.println("1) Non si è fermato al posto di blocco");
        System.out.println("2) Ha proseguito la guida in assenza di documenti");

        selection = InputUtils.requestInteger(scanner, "Selezionare opzione: ", 1, 2);
    }

    @Override
    public String getModifiedDisplayName(Crime crime, Crime.Type crimeType) {
        return getCommandName(crime, crimeType);
    }

    @Override
    public String getCommandName(Crime crime, Crime.Type crimeType) {
        return crime.getFormattedArticle();
    }

    @Override
    public int getFinalCharge(int charge) {
        return selection==1 ? 1000 : 750;
    }
}

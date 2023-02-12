package it.signorpollito.crime.injectors;

import it.signorpollito.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotPaidChargeInjector implements Injector {
    private final List<String> chargeIds = new ArrayList<>();
    private final List<Integer> chargeMoney = new ArrayList<>();
    private int chargesAmount;

    private String formatAmount() {
        return chargesAmount==1 ? "una multa" : String.format("%d multe", chargesAmount);
    }

    @Override
    public void askQuestions(Scanner scanner) {
        chargesAmount = InputUtils.requestInteger(scanner, "Quante multe non pagate? ", 1, 15);

        for (int i = 1; i <= chargesAmount; i++) {
            String id = InputUtils.requestString(scanner, String.format("Inserire id %d° multa: ", i), 5);
            chargeIds.add(id.startsWith("#") ? id : "#".concat(id));

            chargeMoney.add(InputUtils.requestInteger(scanner, String.format("Inserire prezzo %d° multa: ", i), 100));
        }
    }

    @Override
    public String getModifiedDisplayName(String name) {
        return "Mancato pagamento di ".concat(formatAmount());
    }

    @Override
    public String getArrestCommandName(String name) {
        return "Mancato pagamento di %s %s".formatted(formatAmount(), StringUtils.joinWith(",", chargeIds));
    }

    @Override
    public int getFinalHours(int hours) {
        return hours * chargesAmount;
    }

    @Override
    public int getFinalBail(int bail) {
        bail *= chargesAmount;

        for(int amount : chargeMoney)
            bail += amount;

        return bail;
    }
}

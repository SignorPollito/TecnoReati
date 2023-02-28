package it.signorpollito.reader;

import it.signorpollito.crime.ComposedCrime;
import it.signorpollito.crime.Crime;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CSVReader {

    private final File file;

    public CSVReader(File file) {
        this.file = file;
    }

    private String removeBrackets(String name) {
        return name.split("\\(")[0].strip();
    }

    private String getBracketsContent(String name) {
        return name.substring(name.indexOf("(")+1, Math.max(0, name.indexOf(")")));
    }

    private int parseInt(String number) {
        try {
            return Integer.parseInt(number.split(",")[0]);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    private int parseHours(String field) {
        if(field.isBlank()) return 0;

        field = field.split(" ")[0];
        return parseInt(field.substring(0, field.length()-1).strip());
    }

    private int parseMoney(String field) {
        return parseInt(field.replaceAll("[.|+|€]", "").strip().split(" ")[0]);
    }

    private ComposedCrime composeCrime(String name, Crime base, Collection<Crime> subCrimes) {
        ComposedCrime composedCrime = new ComposedCrime(
                name, base.getArticle(),
                base.getCode(), base.getHours(),
                base.getBail(), base.getCharge()
        );

        subCrimes.add(base);
        subCrimes.forEach(crime -> {
            String content = StringUtils.capitalize(getBracketsContent(crime.getName()));

            composedCrime.addSubCrime(new Crime(content.isBlank() ? "Reato normale" : content,
                    crime.getArticle(), crime.getCode(), crime.getHours(),
                    crime.getBail(), crime.getCharge()
            ));
        });

        return composedCrime;
    }

    private Collection<Crime> processCrimes(Collection<Crime> crimes) {
        Set<Crime> finalCrimes = new HashSet<>();

        for(var crime : crimes) {
            String name = removeBrackets(crime.getName());

            List<Crime> subCrimes = new ArrayList<>();
            for(var subCrime : crimes) {
                if(subCrime.isCrime(crime.getName()) || !removeBrackets(subCrime.getName()).equalsIgnoreCase(name))
                    continue;

                subCrimes.add(subCrime);
            }

            finalCrimes.add(subCrimes.isEmpty() ? crime : composeCrime(name, crime, subCrimes));
        }

        return finalCrimes;
    }

    public Collection<Crime> getCrimes() {
        Collection<Crime> tempCrimes = new HashSet<>();

        try(CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {

            boolean firstLine = true;
            for(CSVRecord record : parser) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (record.size() < 5) continue;

                tempCrimes.add(new Crime(record.get(1),
                        record.get(5), record.get(6),
                        parseHours(record.get(3)),
                        parseMoney(record.get(4)),
                        parseMoney(record.get(2))
                ));
            }

        } catch (IOException ignored) {
            return tempCrimes;
        }

        return processCrimes(tempCrimes);
    }
}

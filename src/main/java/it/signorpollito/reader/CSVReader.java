package it.signorpollito.reader;

import it.signorpollito.crime.ComposedCrime;
import it.signorpollito.crime.Crime;
import it.signorpollito.utils.Utils;
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

    private int parseInt(String number) {
        try {
            return Integer.parseInt(number.split(",")[0]);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    private int parseHours(String field) {
        if(field.isBlank()) return 0;

        String[] split = field.split(" ");
        return parseInt(split[0]) * (split.length>=2 && split[1].contains("giorn") ? 24 : 1);
    }

    private int parseMoney(String field) {
        return parseInt(field.replaceAll(",.*", "").replaceAll("[^0-9]+", ""));
    }

    private boolean parseBoolean(String field) {
        return field.equals("TRUE");
    }

    private ComposedCrime composeCrime(String name, Crime base, Collection<Crime> subCrimes) {
        ComposedCrime composedCrime = new ComposedCrime(
                name, base.getArticle(),
                base.getCode(), base.getHours(),
                base.getGenericPayout(), base.isFdr()
        );

        subCrimes.add(base);
        subCrimes.forEach(crime -> {
            String content = StringUtils.capitalize(Utils.getBracketsContent(crime.getName()));

            composedCrime.addSubCrime(new Crime(content.isBlank() ? "Reato normale" : content,
                    crime.getArticle(), crime.getCode(), crime.getHours(),
                    crime.getGenericPayout(), crime.isFdr()
            ));
        });

        return composedCrime;
    }

    private Collection<Crime> processCrimes(Collection<Crime> crimes) {
        Set<Crime> finalCrimes = new HashSet<>();

        for(var crime : crimes) {
            String name = Utils.removeBrackets(crime.getName());

            List<Crime> subCrimes = new ArrayList<>();
            for(var subCrime : crimes) {
                if(subCrime.isCrime(crime.getName()) || !Utils.removeBrackets(subCrime.getName()).equalsIgnoreCase(name))
                    continue;

                subCrimes.add(subCrime);
            }

            finalCrimes.add(subCrimes.isEmpty() ? crime.changeName(name) : composeCrime(name, crime, subCrimes));
        }

        return finalCrimes;
    }

    public Collection<Crime> getCrimes(String code) {
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
                        record.get(0), code,
                        parseHours(record.get(2)),
                        parseMoney(record.get(3)),
                        parseBoolean(record.get(4))
                ));
            }

        } catch (IOException ignored) {
            return tempCrimes;
        }

        return processCrimes(tempCrimes);
    }
}

package it.signorpollito.reader;

import it.signorpollito.crime.Crime;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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

        field = field.split(" ")[0];
        return parseInt(field.substring(0, field.length()-1).strip());
    }

    private int parseMoney(String field) {
        return parseInt(field.replaceAll("[.|+|€]", "").strip().split(" ")[0]);
    }

    public Collection<Crime> getCrimes() {
        Set<Crime> crimes = new HashSet<>();

        try(CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {

            boolean firstLine = true;
            for(CSVRecord record : parser) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (record.size() < 5) continue;

                crimes.add(new Crime(record.get(1).split("\\(")[0].strip(),
                        record.get(5), record.get(6),
                        parseHours(record.get(3)),
                        parseMoney(record.get(4)),
                        parseMoney(record.get(2))
                ));
            }

        } catch (IOException e) {
            return crimes;
        }

        return crimes;
    }
}

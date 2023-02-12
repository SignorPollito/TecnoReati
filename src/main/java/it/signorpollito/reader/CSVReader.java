package it.signorpollito.reader;

import it.signorpollito.crime.Crime;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVReader {

    private final File file;

    public CSVReader(File file) {
        this.file = file;
    }

    private int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private int parseHours(String field) {
        if(field.isBlank()) return 0;

        field = field.split(" ")[0];
        int multiplier = field.endsWith("d") ? 24 : 1;

        return parseInt(field.substring(0, field.length()-1).strip()) * multiplier;
    }

    private int parseMoney(String field) {
        return parseInt(field.split(" ")[0].replaceAll("[.|+|€]", "").strip());
    }

    public Collection<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        try(CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {

            for(CSVRecord record : parser) {
                long length = record.getRecordNumber();

                try {
                    crimes.add(new Crime(record.get(1),
                            length < 1 ? 0 : parseHours(record.get(2)),
                            length < 2 ? 0 : parseMoney(record.get(3)),
                            length < 3 ? 0 : parseMoney(record.get(4))
                    ));

                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }

        } catch (IOException e) {
            return crimes;
        }

        return crimes;
    }
}

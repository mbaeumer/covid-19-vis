package se.mbaeumer.covid19vis;

import java.time.LocalDateTime;

public class CsvRowParser {
    public static CsvDataRow parseCsvString(final String csvData){
        if (csvData == null){
            throw new IllegalArgumentException("Could not parse csv row");
        }

        CsvDataRow row = new CsvDataRow();

        String[] attributes = csvData.split(",");

        if (attributes[0].length() > 0){
            row.setProvince(attributes[0]);
        }

        row.setCountry(attributes[1]);
        row.setLastUpdated(LocalDateTime.parse(attributes[2]));
        row.setConfirmed(Integer.parseInt(attributes[3]));
        row.setDeaths(Integer.parseInt(attributes[4]));
        row.setRecovered(Integer.parseInt(attributes[5]));

        return row;

    }
}

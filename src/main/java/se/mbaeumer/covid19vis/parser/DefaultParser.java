package se.mbaeumer.covid19vis.parser;

import se.mbaeumer.covid19vis.CsvDataRow;

import java.time.LocalDateTime;

public class DefaultParser implements RowParser {
    @Override
    public CsvDataRow parseCsvString(String csvData) {
        if (csvData == null){
            throw new IllegalArgumentException("Could not parse csv row");
        }

        CsvDataRow row = new CsvDataRow();

        String[] attributes = csvData.split(",");

        if (attributes.length == 6) {

            if (attributes[0].length() > 0) {
                row.setProvince(attributes[0]);
            }

            row.setCountry(attributes[1]);
            row.setLastUpdated(LocalDateTime.parse(attributes[2]));
            row.setConfirmed(Integer.parseInt(attributes[3]));
            row.setDeaths(Integer.parseInt(attributes[4]));
            row.setRecovered(Integer.parseInt(attributes[5]));
        }

        return row;

    }
}

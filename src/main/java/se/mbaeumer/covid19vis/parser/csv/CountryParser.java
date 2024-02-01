package se.mbaeumer.covid19vis.parser.csv;

import se.mbaeumer.covid19vis.CsvDataRow;
import se.mbaeumer.covid19vis.parser.date.DateParser;

public class CountryParser implements RowParser {
    private DateParser dateParser = new DateParser();

    @Override
    public CsvDataRow parseCsvString(String csvData) {
        CsvDataRow row = new CsvDataRow();

        String[] attributes = csvData.split(",");

        if (attributes[0].length() > 0) {
            row.setProvince(attributes[0]);
        }

        row.setCountry(attributes[1]);
        row.setLastUpdated(dateParser.parseDateString(attributes[2]));
        row.setConfirmed(getInteger(attributes[3]));
        row.setDeaths(getInteger(attributes[4]));
        row.setRecovered(getInteger(attributes[5]));

        return row;
    }

    public static int getInteger(final String numberString){
        try {
            return numberString == null || numberString.length() == 0 || numberString.contains(".")  ? 0 : Integer.parseInt(numberString);
        }catch (NumberFormatException nfe){
            return 0;
        }
    }
}

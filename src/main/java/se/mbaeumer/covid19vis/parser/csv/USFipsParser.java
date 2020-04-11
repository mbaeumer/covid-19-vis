package se.mbaeumer.covid19vis.parser.csv;

import se.mbaeumer.covid19vis.CsvDataRow;
import se.mbaeumer.covid19vis.parser.date.DateParser;

public class USFipsParser implements RowParser {

    private DateParser dateParser = new DateParser();

    @Override
    public CsvDataRow parseCsvString(String csv) {
        CsvDataRow csvDataRow = new CsvDataRow();

        String[] attributes = csv.split(",");
        csvDataRow.setCountry(attributes[3]);
        csvDataRow.setProvince(handleProvince(attributes));
        csvDataRow.setLastUpdated(dateParser.parseDateString(attributes[4]));
        csvDataRow.setConfirmed(getInteger(attributes[7]));
        csvDataRow.setDeaths(getInteger(attributes[8]));
        csvDataRow.setRecovered(getInteger(attributes[9]));
        return csvDataRow;
    }

    private String handleProvince(final String[] attributes){
        return attributes[1] + ", " + attributes[2];
    }

    private int getInteger(final String numberString){
        return numberString == null || numberString.length() == 0 ? 0 : Integer.parseInt(numberString);
    }
}

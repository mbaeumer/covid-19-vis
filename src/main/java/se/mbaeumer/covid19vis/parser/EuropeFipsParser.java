package se.mbaeumer.covid19vis.parser;

import se.mbaeumer.covid19vis.CsvDataRow;
import se.mbaeumer.covid19vis.DateParser;

public class EuropeFipsParser implements RowParser {

    @Override
    public CsvDataRow parseCsvString(String csvData) {
        DateParser dateParser = new DateParser();
        CsvDataRow csvDataRow = new CsvDataRow();
        //,,,Sweden,3/22/20 23:45,60.128161,18.643501,1931,21,16,1894,Sweden

        final String[] attributes = csvData.split(",");
        csvDataRow.setCountry(attributes[3]);
        csvDataRow.setLastUpdated(dateParser.parseDateString(attributes[4]));
        csvDataRow.setConfirmed(getInteger(attributes[7]));
        csvDataRow.setDeaths(getInteger(attributes[8]));
        csvDataRow.setRecovered(getInteger(attributes[9]));

        return csvDataRow;
    }

    private static int getInteger(final String numberString){
        return numberString == null || numberString.length() == 0 ? 0 : Integer.parseInt(numberString);
    }
}

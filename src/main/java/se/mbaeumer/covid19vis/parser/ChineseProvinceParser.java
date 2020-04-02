package se.mbaeumer.covid19vis.parser;

import se.mbaeumer.covid19vis.CsvDataRow;
import se.mbaeumer.covid19vis.DateParser;

public class ChineseProvinceParser implements RowParser {
    @Override
    public CsvDataRow parseCsvString(String csvData) {
        // Shanghai,Mainland China,1/23/20 17:00,16,,
        CsvDataRow row = new CsvDataRow();
        DateParser dateParser = new DateParser();

        String[] attributes = csvData.split(",");

        if (attributes.length == 4){
            row.setProvince(attributes[0]);
            row.setCountry(attributes[1]);
            row.setLastUpdated(dateParser.parseDateString(attributes[2]));
            row.setConfirmed(getInteger(attributes[3]));
        }

        return row;
    }

    private static int getInteger(final String numberString){
        return numberString == null || numberString.length() == 0 ? 0 : Integer.parseInt(numberString);
    }

}

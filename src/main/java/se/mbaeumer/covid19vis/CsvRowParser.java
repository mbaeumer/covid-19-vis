package se.mbaeumer.covid19vis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CsvRowParser {
    public static CsvDataRow parseCsvString(final String csvData) throws DateTimeParseException {
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
        }else{
            // 45001,Abbeville,South Carolina,US,2020-03-27 22:14:55,34.22333378,-82.46170658,4,0,0,0,"Abbeville, South Carolina, US"
            if (attributes[2].length() > 0) {
                row.setProvince(attributes[2]);
            }
            row.setCountry(attributes[3]);
            row.setLastUpdated(LocalDateTime.parse(attributes[4].replace(" ", "T")));
            row.setConfirmed(Integer.parseInt(attributes[7]));
            row.setDeaths(Integer.parseInt(attributes[8]));
            row.setRecovered(Integer.parseInt(attributes[9]));
        }

        return row;

    }

    public static LocalDateTime parseDateString(final String date){
        //DateTimeFormatter.BASIC_ISO_DATE
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, dtf);
    }
}

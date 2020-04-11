package se.mbaeumer.covid19vis.parser.csv;

import java.util.regex.Pattern;

public class RowParserFactory {
    public static RowParser getParser(final String csvData){
        if (regex(csvData)){
            return new USFipsParser();
        }else if (csvData.contains("Mainland China")){
            return new ChineseProvinceParser();
        }else if (csvData.startsWith(",,,")){
            return new EuropeFipsParser();
        }else if (csvData.startsWith(",")){
            return new CountryParser();
        }

        return new DefaultParser();
    }

    public static boolean regex(final String text){
        String regex = "^\\d{4,5}";
        return Pattern.matches(regex, text);
    }
}

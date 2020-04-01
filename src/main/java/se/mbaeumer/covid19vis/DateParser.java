package se.mbaeumer.covid19vis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateParser {

    public LocalDateTime parseDateString(String date){
        //DateTimeFormatter.BASIC_ISO_DATE
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (date.contains("-") && date.contains(" ")){
            date = date.replace(" ", "T");
        }else if (date.contains("/")){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/dd/yy HH:mm:ss");
            return LocalDateTime.parse(date, dtf);
        }
        return LocalDateTime.parse(date);
    }
}

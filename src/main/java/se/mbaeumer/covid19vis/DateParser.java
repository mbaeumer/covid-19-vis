package se.mbaeumer.covid19vis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class DateParser {

    public LocalDateTime parseDateString(String date){

        if (date.contains("-") && date.contains(" ")){
            date = date.replace(" ", "T");
        }else if (date.contains("/")){
            DateTimeFormatter dtf = null;
            if (matchesYearWithTwoDigits(date)){
                if (containsSeconds(date)){
                    dtf = DateTimeFormatter.ofPattern("M/d/yy HH:mm:ss");
                }else{
                    dtf = DateTimeFormatter.ofPattern("M/d/yy HH:mm");
                }
            }else if (matchesYearWithFourDigits(date)){
                if (containsSeconds(date)){
                    dtf = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss");
                }else{
                    dtf = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");
                }
            }
            return LocalDateTime.parse(date, dtf);
        }
        return LocalDateTime.parse(date);
    }

    private boolean containsSeconds(final String date){
        long colons = date.chars().filter(ch -> ch == ':').count();
        return colons == 2;
    }


    public boolean matchesYearWithTwoDigits(final String text){
        String regex = "^\\d{1,2}\\/\\d{1,2}\\/\\d{2}\\s.+";
        return Pattern.matches(regex, text);
    }

    public boolean matchesYearWithFourDigits(final String text){
        String regex = "^\\d{1,2}\\/\\d{1,2}\\/\\d{4}.+";
        return Pattern.matches(regex, text);
    }

}

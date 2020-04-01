package se.mbaeumer.covid19vis;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;

import static org.junit.Assert.*;

public class DateParserTest {

    @Test
    public void parseDateStringWithDashesAndWhitespace() {

        DateParser dateParser = new DateParser();
        LocalDateTime ldt = dateParser.parseDateString("2020-03-27 22:14:55");
        Assert.assertEquals(27, ldt.getDayOfMonth());
    }

    @Test
    public void parseDateStringWithT() {

        DateParser dateParser = new DateParser();
        LocalDateTime ldt = dateParser.parseDateString("2020-03-27T22:14:55");
        Assert.assertEquals(27, ldt.getDayOfMonth());
    }

    @Test
    public void parseDateStringWithSlashesAndWhitespace() {

        DateParser dateParser = new DateParser();
        LocalDateTime ldt = dateParser.parseDateString("12/27/20 22:14:55");
        Assert.assertEquals(27, ldt.getDayOfMonth());
        Assert.assertEquals(12, ldt.getMonthValue());
        Assert.assertEquals(2020, ldt.getYear());
    }


}
package se.mbaeumer.covid19vis;

import org.junit.Assert;
import org.junit.Test;
import se.mbaeumer.covid19vis.parser.date.DateParser;

import java.time.LocalDateTime;

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

    @Test
    public void parseDateStringWithoutSeconds() {

        DateParser dateParser = new DateParser();
        LocalDateTime ldt = dateParser.parseDateString("12/27/20 22:14");
        Assert.assertEquals(27, ldt.getDayOfMonth());
        Assert.assertEquals(12, ldt.getMonthValue());
        Assert.assertEquals(2020, ldt.getYear());
    }

    @Test
    public void parseDateStringWithFourDigitsYearWithoutSeconds() {

        DateParser dateParser = new DateParser();
        LocalDateTime ldt = dateParser.parseDateString("12/27/2020 22:14");
        Assert.assertEquals(27, ldt.getDayOfMonth());
        Assert.assertEquals(12, ldt.getMonthValue());
        Assert.assertEquals(2020, ldt.getYear());
    }

    @Test
    public void parseDateStringWithSingleDigitsHour() {

        DateParser dateParser = new DateParser();
        LocalDateTime ldt = dateParser.parseDateString("12/27/2020 8:14");
        Assert.assertEquals(27, ldt.getDayOfMonth());
        Assert.assertEquals(12, ldt.getMonthValue());
        Assert.assertEquals(2020, ldt.getYear());
    }
    public void matchesYearWithTwoDigits() {
        DateParser dateParser = new DateParser();
        Assert.assertTrue(dateParser.matchesYearWithTwoDigits("12/27/20 00:00:00"));
    }

    @Test
    public void matchesYearWithFourDigits() {
    }
}
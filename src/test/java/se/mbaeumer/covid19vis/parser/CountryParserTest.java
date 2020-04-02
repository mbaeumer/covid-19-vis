package se.mbaeumer.covid19vis.parser;

import org.junit.Assert;
import org.junit.Test;
import se.mbaeumer.covid19vis.CsvDataRow;

import static org.junit.Assert.*;

public class CountryParserTest {

    @Test
    public void parseCsvString() {
        CountryParser countryParser = new CountryParser();
        CsvDataRow csvDataRow = countryParser.parseCsvString(",Malaysia,1/31/2020 8:15,8,0,0");
        Assert.assertTrue("Malaysia".equals(csvDataRow.getCountry()));
        Assert.assertNull(csvDataRow.getProvince());
        Assert.assertEquals(8, csvDataRow.getConfirmed());
        Assert.assertEquals(0, csvDataRow.getDeaths());
        Assert.assertEquals(0, csvDataRow.getRecovered());
        Assert.assertEquals(1, csvDataRow.getLastUpdated().getMonthValue());
    }

    //,Japan,1/26/20 16:00,4,,1
    @Test
    public void parseCsvString2() {
        CountryParser countryParser = new CountryParser();
        CsvDataRow csvDataRow = countryParser.parseCsvString(",Japan,1/26/20 16:00,4,,1");
        Assert.assertTrue("Japan".equals(csvDataRow.getCountry()));
        Assert.assertNull(csvDataRow.getProvince());
        Assert.assertEquals(4, csvDataRow.getConfirmed());
        Assert.assertEquals(0, csvDataRow.getDeaths());
        Assert.assertEquals(1, csvDataRow.getRecovered());
        Assert.assertEquals(1, csvDataRow.getLastUpdated().getMonthValue());
    }
}
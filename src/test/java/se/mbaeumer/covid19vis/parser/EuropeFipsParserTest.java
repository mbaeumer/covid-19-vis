package se.mbaeumer.covid19vis.parser;

import org.junit.Assert;
import org.junit.Test;
import se.mbaeumer.covid19vis.CsvDataRow;

import static org.junit.Assert.*;

public class EuropeFipsParserTest {

    @Test
    public void parseCsvString() {
        EuropeFipsParser europeFipsParser = new EuropeFipsParser();
        CsvDataRow csvDataRow = europeFipsParser.parseCsvString(",,,Sweden,3/22/20 23:45,60.128161,18.643501,1931,21,16,1894,Sweden");
        Assert.assertTrue("Sweden".equals(csvDataRow.getCountry()));
        Assert.assertNull(csvDataRow.getProvince());
        Assert.assertEquals(1931, csvDataRow.getConfirmed());
        Assert.assertEquals(21, csvDataRow.getDeaths());
        Assert.assertEquals(16, csvDataRow.getRecovered());
        Assert.assertEquals(3, csvDataRow.getLastUpdated().getMonthValue());
    }
}
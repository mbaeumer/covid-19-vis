package se.mbaeumer.covid19vis.parser;

import org.junit.Assert;
import org.junit.Test;
import se.mbaeumer.covid19vis.CsvDataRow;

import static org.junit.Assert.*;

public class ChineseProvinceParserTest {

    @Test
    public void parseCsvString() {
        ChineseProvinceParser chineseProvinceParser = new ChineseProvinceParser();
        CsvDataRow csvDataRow = chineseProvinceParser.parseCsvString("Shanghai,Mainland China,1/23/20 17:00,16,,");
        Assert.assertTrue("Mainland China".equals(csvDataRow.getCountry()));
        Assert.assertTrue("Shanghai".equals(csvDataRow.getProvince()));
        Assert.assertEquals(16, csvDataRow.getConfirmed());
        Assert.assertEquals(0, csvDataRow.getDeaths());
        Assert.assertEquals(0, csvDataRow.getRecovered());
        Assert.assertEquals(1, csvDataRow.getLastUpdated().getMonthValue());
    }

    @Test
    public void parseCsvString2() {
        ChineseProvinceParser chineseProvinceParser = new ChineseProvinceParser();
        CsvDataRow csvDataRow = chineseProvinceParser.parseCsvString("Anhui,Mainland China,1/22/2020 17:00,1,,");
        Assert.assertTrue("Mainland China".equals(csvDataRow.getCountry()));
        Assert.assertTrue("Anhui".equals(csvDataRow.getProvince()));
        Assert.assertEquals(1, csvDataRow.getConfirmed());
        Assert.assertEquals(0, csvDataRow.getDeaths());
        Assert.assertEquals(0, csvDataRow.getRecovered());
        Assert.assertEquals(1, csvDataRow.getLastUpdated().getMonthValue());
    }
}
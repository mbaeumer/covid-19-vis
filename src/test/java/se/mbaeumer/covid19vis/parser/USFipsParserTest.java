package se.mbaeumer.covid19vis.parser;

import org.junit.Assert;
import org.junit.Test;
import se.mbaeumer.covid19vis.CsvDataRow;

import static org.junit.Assert.*;

public class USFipsParserTest {

    @Test
    public void parseCsvString() {
        USFipsParser usFipsParser = new USFipsParser();
        String data = "47083,Houston,Tennessee,US,3/22/20 23:45,36.28872054,-87.71878828,1,0,0,0";
        CsvDataRow csvDataRow = usFipsParser.parseCsvString(data);
        Assert.assertTrue("US".equals(csvDataRow.getCountry()));
        Assert.assertTrue("Houston, Tennessee".equals(csvDataRow.getProvince()));
        Assert.assertEquals(1, csvDataRow.getConfirmed());
        Assert.assertEquals(0, csvDataRow.getDeaths());
        Assert.assertEquals(0, csvDataRow.getRecovered());
        Assert.assertEquals(3, csvDataRow.getLastUpdated().getMonthValue());

    }
}
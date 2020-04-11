package se.mbaeumer.covid19vis.parser;

import org.junit.Assert;
import org.junit.Test;
import se.mbaeumer.covid19vis.parser.csv.RowParser;
import se.mbaeumer.covid19vis.parser.csv.RowParserFactory;

public class RowParserFactoryTest {

    @Test
    public void getUSFipsParser() {
        RowParser rowParser = RowParserFactory.getParser("2000");
        Assert.assertTrue(rowParser.getClass().getName().contains("USFips"));
    }

    @Test
    public void getChineseProvinceParser() {
        RowParser rowParser = RowParserFactory.getParser("Mainland China");
        Assert.assertTrue(rowParser.getClass().getName().contains("ChineseProvince"));
    }

    @Test
    public void getEuropeFipsParser() {
        RowParser rowParser = RowParserFactory.getParser(",,,");
        Assert.assertTrue(rowParser.getClass().getName().contains("EuropeFips"));
    }

    @Test
    public void getCountryParser() {
        RowParser rowParser = RowParserFactory.getParser(",");
        Assert.assertTrue(rowParser.getClass().getName().contains("CountryParser"));
    }

    @Test
    public void regex() {
        Assert.assertTrue(RowParserFactory.regex("20000"));
    }
}
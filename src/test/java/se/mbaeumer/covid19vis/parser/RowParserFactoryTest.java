package se.mbaeumer.covid19vis.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RowParserFactoryTest {

    @Test
    public void getUSFipsParser() {
        RowParser rowParser = RowParserFactory.getParser("2000");
        Assert.assertTrue(rowParser.getClass().getName().contains("USFips"));
    }

    @Test
    public void regex() {
        Assert.assertTrue(RowParserFactory.regex("20000"));
    }
}
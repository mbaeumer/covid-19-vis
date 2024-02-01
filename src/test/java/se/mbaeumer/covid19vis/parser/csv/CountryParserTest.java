package se.mbaeumer.covid19vis.parser.csv;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class CountryParserTest extends TestCase {

    @Test
    public void testSomething(){
        int number = CountryParser.getInteger("7.0");
        Assert.assertEquals(0, number);
    }

}
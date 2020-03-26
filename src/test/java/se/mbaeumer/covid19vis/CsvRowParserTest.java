package se.mbaeumer.covid19vis;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CsvRowParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void parseCsvString() {
        CsvRowParser.parseCsvString(null);
    }

    @Test
    public void shouldGetProvince(){
        /*
        Province/State,Country/Region,Last Update,Confirmed,Deaths,Recovered
Hubei,Mainland China,2020-02-21T13:03:09,62662,2144,11881
         */
        final String provinceData="Hubei,Mainland China,2020-02-21T13:03:09,62662,2144,11881";
        CsvDataRow row = CsvRowParser.parseCsvString(provinceData);
        Assert.assertTrue("Hubei".equals(row.getProvince()));
        Assert.assertTrue("Mainland China".equals(row.getCountry()));
        Assert.assertNotNull(row.getLastUpdated());
        Assert.assertEquals(row.getConfirmed(), 62662);
        Assert.assertEquals(row.getDeaths(), 2144);
        Assert.assertEquals(row.getRecovered(), 11881);

    }

    @Test
    public void shouldGetCountry(){
        final String countryData=",Sweden,2020-02-01T02:13:26,1,0,0";
        CsvDataRow row = CsvRowParser.parseCsvString(countryData);
        Assert.assertNull(row.getProvince());
        Assert.assertTrue("Sweden".equals(row.getCountry()));
        Assert.assertNotNull(row.getLastUpdated());
        Assert.assertEquals(row.getConfirmed(), 1);
        Assert.assertEquals(row.getDeaths(), 0);
        Assert.assertEquals(row.getRecovered(), 0);
    }


}
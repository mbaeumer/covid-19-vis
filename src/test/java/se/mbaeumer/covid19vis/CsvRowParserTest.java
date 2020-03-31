package se.mbaeumer.covid19vis;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

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

    @Test
    public void shouldHandleLongRow(){
        final String countryData="45001,Abbeville,South Carolina,US,2020-03-27 22:14:55,34.22333378,-82.46170658,4,0,0,0,\"Abbeville, South Carolina, US\"";
        CsvDataRow row = CsvRowParser.parseCsvString(countryData);
        Assert.assertNull(row.getProvince());
        Assert.assertTrue("US".equals(row.getCountry()));
        Assert.assertNotNull(row.getLastUpdated());
        Assert.assertEquals(row.getConfirmed(), 4);
        Assert.assertEquals(row.getDeaths(), 0);
        Assert.assertEquals(row.getRecovered(), 0);
    }
    // FIPS,Admin2,Province_State,Country_Region,Last_Update,Lat,Long_,Confirmed,Deaths,Recovered,Active,Combined_Key
    // 45001,Abbeville,South Carolina,US,2020-03-27 22:14:55,34.22333378,-82.46170658,4,0,0,0,"Abbeville, South Carolina, US"

    @Test
    public void testDateParse(){
        LocalDateTime localDateTime = CsvRowParser.parseDateString("2020-03-27 22:14:55");
        System.out.println("test");

    }


}
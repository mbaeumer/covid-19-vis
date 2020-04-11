package se.mbaeumer.covid19vis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataFilterService {

    public List<CsvDataRow> getCountriesWithoutProvinces(final List<CsvDataRow> data){
        return data.stream()
                .filter(csvDataRow -> csvDataRow.getCountry() != null && csvDataRow.getProvince() == null)
                .collect(Collectors.toList());
    }

    public List<CsvDataRow> getDataForCountry(final List<CsvDataRow> data, String country){
        return data.stream()
                .filter(csvDataRow -> country.equals(csvDataRow.getCountry()))
                .collect(Collectors.toList());
    }

    public Set<String> getCountryNames(List<CsvDataRow> data){

        if (data == null){
            Set<String> dummyList = new HashSet<>();
            dummyList.add("No data available");
            return dummyList;
        }
        List<CsvDataRow> rawData = getCountriesWithoutProvinces(data);
        return rawData.stream().map(c -> c.getCountry()).collect(Collectors.toSet());
    }

    public List<CsvDataRow> getDataByDate(final List<CsvDataRow> data, final LocalDateTime date){
        List<CsvDataRow> rawData = getCountriesWithoutProvinces(data);

        return rawData.stream()
                .filter(d -> d.getLastUpdated().getMonthValue() == date.getMonthValue()
                && d.getLastUpdated().getDayOfMonth() == date.getDayOfMonth()
                        && d.getLastUpdated().getYear() == date.getYear())
                .collect(Collectors.toList());
    }


}

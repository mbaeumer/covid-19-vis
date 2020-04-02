package se.mbaeumer.covid19vis;

import java.util.List;
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
}

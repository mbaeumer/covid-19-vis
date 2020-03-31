package se.mbaeumer.covid19vis;

import java.util.List;
import java.util.stream.Collectors;

public class DataFilterService {

    public List<CsvDataRow> getCountriesWithoutProvinces(final List<CsvDataRow> data){
        return data.stream().filter(csvDataRow -> csvDataRow.getProvince() == null).collect(Collectors.toList());
    }
}

package se.mbaeumer.covid19vis.services;

import se.mbaeumer.covid19vis.CsvDataRow;
import se.mbaeumer.covid19vis.MetricsType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataFilterService {

    public List<CsvDataRow> getCountriesWithoutProvinces(final List<CsvDataRow> data){
        return data.stream()
                .filter(csvDataRow -> csvDataRow.getCountry() != null && csvDataRow.getProvince() == null)
                .collect(Collectors.toList());
    }

    public List<DailyCase> getCasesByCountrySortedByDate(final List<CsvDataRow> data, String country){
        List<CsvDataRow> countryData = getDataForCountry(data, country);
        List<DailyCase> dailyCases = new ArrayList<>();
        List<CsvDataRow> sortedList = countryData.stream()
                .sorted(Comparator.comparing(CsvDataRow::getConfirmed))
                .collect(Collectors.toList());

        int i = 1;
        while (i < sortedList.size()){
            int j = i - 1;
            int newCases = sortedList.get(i).getConfirmed() - sortedList.get(j).getConfirmed();
            dailyCases.add(new DailyCase(sortedList.get(i).getLastUpdated(), newCases));
            i++;
        }

        return dailyCases;
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

    public List<CsvDataRow> getDataByDateAndMetricsType(final List<CsvDataRow> data, final LocalDateTime date, final MetricsType metricsType){
        List<CsvDataRow> result = null;
        if (metricsType == MetricsType.CONFIRMED){
            result = getTopConfirmedCasesByDate(data, date);
        }else if (metricsType == MetricsType.RECOVERED){
            result = getTopRecoveredCasesByDate(data, date);
        }else if (metricsType == MetricsType.DEATHS){
            result = getTopDeathCasesByDate(data, date);
        }

        return result;
    }

    public List<CsvDataRow> getTopConfirmedCasesByDate(final List<CsvDataRow> data, final LocalDateTime date){
        List<CsvDataRow> rawData = getDataByDate(data, date);

        return rawData.stream()
                .sorted(Comparator.comparing(CsvDataRow::getConfirmed).reversed())
                .collect(Collectors.toList());
    }

    public List<CsvDataRow> getTopRecoveredCasesByDate(final List<CsvDataRow> data, final LocalDateTime date){
        List<CsvDataRow> rawData = getDataByDate(data, date);

        return rawData.stream()
                .sorted(Comparator.comparing(CsvDataRow::getRecovered).reversed())
                .collect(Collectors.toList());
    }

    public List<CsvDataRow> getTopDeathCasesByDate(final List<CsvDataRow> data, final LocalDateTime date){
        List<CsvDataRow> rawData = getDataByDate(data, date);

        return rawData.stream()
                .sorted(Comparator.comparing(CsvDataRow::getDeaths).reversed())
                .collect(Collectors.toList());
    }
}

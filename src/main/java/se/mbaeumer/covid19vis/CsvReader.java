package se.mbaeumer.covid19vis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {
    private List<CsvDataRow> csvDataRowList = new ArrayList<>();


    private DirectoryService directoryService;

    public Map getDataMap() {
        return dataMap;
    }

    private Map dataMap = new HashMap<String, List<CsvDataRow>>();

    public CsvReader(final DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    public List<CsvDataRow> getCsvDataRowList() {
        return csvDataRowList;
    }

    public void readMultipleCsvFiles(final String path){
        List<String> filnames = directoryService.getAllCsvFilenames(path);

        for (int i=0;i<filnames.size(); i++){
            csvDataRowList = new ArrayList<>();
            readSingleCsvFile(filnames.get(i));
            csvDataRowList = new DataFilterService().getCountriesWithoutProvinces(csvDataRowList);
            dataMap.put(filnames.get(i), csvDataRowList);
        }
    }


    public void readSingleCsvFile(final String filename){
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            while ((line = br.readLine()) != null) {
                try {
                    CsvDataRow row = CsvRowParser.parseCsvString(line);
                    csvDataRowList.add(row);
                }catch(DateTimeParseException e){
                    System.out.println("Parse error: " + line + "," + filename);
                }catch(ArrayIndexOutOfBoundsException ex){
                    System.out.println("Out of bound: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

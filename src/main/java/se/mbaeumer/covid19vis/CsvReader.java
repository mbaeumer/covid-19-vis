package se.mbaeumer.covid19vis;

import se.mbaeumer.covid19vis.parser.RowParser;
import se.mbaeumer.covid19vis.parser.RowParserFactory;

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
    private int parseErrors = 0;

    public CsvReader(final DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    public List<CsvDataRow> getCsvDataRowList() {
        return csvDataRowList;
    }

    public void readMultipleCsvFiles(final String path){
        List<String> filenames = directoryService.getAllCsvFilenames(path);
        csvDataRowList = new ArrayList<>();

        for (int i=0;i<filenames.size(); i++){
            csvDataRowList.addAll(readSingleCsvFile(filenames.get(i)));
        }
        System.out.println("Total parse errors: " + parseErrors);
    }


    public List<CsvDataRow> readSingleCsvFile(final String filename){
        List<CsvDataRow> csvDataRows = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            while ((line = br.readLine()) != null) {
                try {
                    line = line.stripLeading();
                    if (!line.startsWith("Province") && !line.startsWith(" Province")) {
                        RowParser rowParser = RowParserFactory.getParser(line);
                        CsvDataRow row = rowParser.parseCsvString(line);
                        csvDataRows.add(row);
                    }
                }catch(DateTimeParseException e){
                    System.out.println("Parse error: " + line + "," + filename);
                    parseErrors++;
                }catch(ArrayIndexOutOfBoundsException ex){
                    System.out.println("Out of bound: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvDataRows;
    }
}

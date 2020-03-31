package se.mbaeumer.covid19vis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private List<CsvDataRow> csvDataRowList = new ArrayList<>();

    public CsvReader() {
    }

    public List<CsvDataRow> getCsvDataRowList() {
        return csvDataRowList;
    }

    public void readCsvFile(final String filename){
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            while ((line = br.readLine()) != null) {
                try {
                    CsvDataRow row = CsvRowParser.parseCsvString(line);
                    csvDataRowList.add(row);
                }catch(DateTimeParseException e){
                    System.out.println("Parse error");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

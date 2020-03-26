package se.mbaeumer.covid19vis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private List<CsvDataRow> csvDataRowList = new ArrayList<>();

    public CsvReader() {
    }

    public void readCsvFile(final String filename){
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            while ((line = br.readLine()) != null) {
                CsvDataRow row = CsvRowParser.parseCsvString(line);
                csvDataRowList.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

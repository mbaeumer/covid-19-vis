package se.mbaeumer.covid19vis.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryService {
    public static final String DATA_PATH = "/csse_covid_19_data/csse_covid_19_daily_reports/";
    public List<String> getAllCsvFilenames(final String path){
        File file = new File(path);
        List<String> filenames = Arrays.asList(file.listFiles())
                .stream()
                .map(f -> f.getAbsolutePath()).collect(Collectors.toList());

        return filenames.stream()
                .filter(filename -> "csv".equals(filename.substring(filename.lastIndexOf(".") + 1)))
                .collect(Collectors.toList());
    }

    public String getMatchingFilename(final String path, final String match) throws FileNotFoundException {
        List<String> allFilenames = getAllCsvFilenames(path);
        String filename = allFilenames.stream().filter(s -> match.equals(s))
                .findFirst().orElseThrow(FileNotFoundException::new);

        return filename;
    }

    public DirectoryValidationResult validateDirectory(final String baseDir){
        DirectoryValidationResult result = DirectoryValidationResult.CSV_FILES;
        File directory = new File(baseDir + DATA_PATH);
        List<File> files = Arrays.asList(directory.listFiles());
        if (files.size() == 0){
            result = DirectoryValidationResult.EMPTY;
        }else{
            List<String> csvFileNames = getAllCsvFilenames(directory.getAbsolutePath());
            if (csvFileNames.size() == 0){
                result = DirectoryValidationResult.NO_CSV_FILES;
            }
        }

        return result;
    }

}

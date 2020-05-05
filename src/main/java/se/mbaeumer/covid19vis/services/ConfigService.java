package se.mbaeumer.covid19vis.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Properties;

public class ConfigService {

    private String baseDataFolder;

    public String getBaseDataFolder() {
        return baseDataFolder;
    }

    public void setBaseDataFolder(String baseDataFolder) {
        this.baseDataFolder = baseDataFolder;
    }

    public void readConfigFile() throws IOException, InvalidParameterException {
        InputStream inputStream;
        Properties prop = new Properties();
        String propFileName = "config.properties";

        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        baseDataFolder = prop.getProperty("repository.baseDir");

        if (baseDataFolder == null || baseDataFolder.length() == 0){
            throw new InvalidParameterException("Property repository.baseDir is not set");
        }

        inputStream.close();
    }


}

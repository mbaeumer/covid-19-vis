package se.mbaeumer.covid19vis.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DirectoryService.class)
public class DirectoryServiceTest {

    @Test
    public void shouldGetEmpty() throws Exception {
        mockEmptyDirectory();
        DirectoryService directoryService = new DirectoryService();

        DirectoryValidationResult result = directoryService.validateDirectory("");
        Assert.assertTrue(result == DirectoryValidationResult.EMPTY);
    }

    @Test
    public void shouldGetNoCsvFiles() throws Exception {
        mockDirectoryWithNoCsvFiles();
        DirectoryService directoryService = new DirectoryService();

        DirectoryValidationResult result = directoryService.validateDirectory("");
        Assert.assertTrue(result == DirectoryValidationResult.NO_CSV_FILES);
    }

    @Test
    public void shouldGetCsvFile() throws Exception {
        mockDirectoryWithCsvFile();
        DirectoryService directoryService = new DirectoryService();

        DirectoryValidationResult result = directoryService.validateDirectory("");
        Assert.assertTrue(result == DirectoryValidationResult.CSV_FILES);
    }

    private void mockEmptyDirectory() throws Exception {
        File mockedDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(mockedDirectory);
        Mockito.when(mockedDirectory.exists()).thenReturn(true);
        Mockito.when(mockedDirectory.listFiles()).thenReturn(new File[0]);
    }

    private void mockDirectoryWithNoCsvFiles() throws Exception {
        File mockedDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(mockedDirectory);
        Mockito.when(mockedDirectory.exists()).thenReturn(true);
        File[] files = new File[1];
        files[0] = Mockito.mock(File.class);
        Mockito.when(files[0].getAbsolutePath()).thenReturn("test.txt");
        Mockito.when(mockedDirectory.listFiles()).thenReturn(files);
    }

    private void mockDirectoryWithCsvFile() throws Exception {
        File mockedDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(mockedDirectory);
        Mockito.when(mockedDirectory.exists()).thenReturn(true);
        File[] files = new File[1];
        files[0] = Mockito.mock(File.class);
        Mockito.when(files[0].getAbsolutePath()).thenReturn("test.csv");
        Mockito.when(mockedDirectory.listFiles()).thenReturn(files);
    }
}
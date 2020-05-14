package se.mbaeumer.covid19vis.services;

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
        assertTrue(result == DirectoryValidationResult.EMPTY);
    }

    @Test
    public void shouldGetNoCsvFiles() throws Exception {
        mockDirectoryWithNoCsvFiles();
        DirectoryService directoryService = new DirectoryService();

        DirectoryValidationResult result = directoryService.validateDirectory("/baseDir");
        assertTrue(result == DirectoryValidationResult.NO_CSV_FILES);
    }

    @Test
    public void shouldGetNoCsvFilesIfSubDirectoryNotExists() throws Exception {
        mockDirectoryWithNoSubDirectory();
        DirectoryService directoryService = new DirectoryService();

        DirectoryValidationResult result = directoryService.validateDirectory("/baseDir");
        assertTrue(result == DirectoryValidationResult.NO_CSV_FILES);
    }

    @Test
    public void shouldGetCsvFile() throws Exception {
        mockDirectoryWithCsvFile();
        DirectoryService directoryService = new DirectoryService();

        DirectoryValidationResult result = directoryService.validateDirectory("/baseDir");
        assertTrue(result == DirectoryValidationResult.CSV_FILES);
    }

    @Test
    public void shouldGetPathNotFound() throws Exception {
        mockPathNotExists();
        DirectoryService directoryService = new DirectoryService();

        DirectoryValidationResult result = directoryService.validateDirectory("/baseDir");
        assertTrue(result == DirectoryValidationResult.PATH_NOT_FOUND);
    }

    private void mockPathNotExists() throws Exception{
        File mockedDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(mockedDirectory);
        Mockito.when(mockedDirectory.exists()).thenReturn(false);
    }

    private void mockEmptyDirectory() throws Exception {
        File mockedDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(mockedDirectory);
        Mockito.when(mockedDirectory.exists()).thenReturn(true);
        Mockito.when(mockedDirectory.listFiles()).thenReturn(new File[0]);
    }

    private void mockDirectoryWithNoCsvFiles() throws Exception {
        File mockedBaseDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments("/baseDir").thenReturn(mockedBaseDirectory);
        Mockito.when(mockedBaseDirectory.exists()).thenReturn(true);

        File mockedSubDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments("/baseDir" + DirectoryService.DATA_PATH).thenReturn(mockedSubDirectory);
        Mockito.when(mockedSubDirectory.exists()).thenReturn(true);
        Mockito.when(mockedSubDirectory.getAbsolutePath()).thenReturn("/baseDir" + DirectoryService.DATA_PATH);

        File[] contentBaseDirectory = new File[1];
        contentBaseDirectory[0] = mockedSubDirectory;
        Mockito.when(mockedBaseDirectory.listFiles()).thenReturn(contentBaseDirectory);

        File[] contentSubDirectory = new File[1];
        contentSubDirectory[0] = Mockito.mock(File.class);
        Mockito.when(contentSubDirectory[0].getAbsolutePath()).thenReturn("test.txt");
        Mockito.when(mockedSubDirectory.listFiles()).thenReturn(contentSubDirectory);
    }

    private void mockDirectoryWithNoSubDirectory() throws Exception {
        File mockedBaseDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments("/baseDir").thenReturn(mockedBaseDirectory);
        Mockito.when(mockedBaseDirectory.exists()).thenReturn(true);

        File mockedSubDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments("/baseDir" + DirectoryService.DATA_PATH).thenReturn(mockedSubDirectory);
        Mockito.when(mockedSubDirectory.exists()).thenReturn(false);

        File[] contentBaseDirectory = new File[1];
        contentBaseDirectory[0] = mockedSubDirectory;
        Mockito.when(mockedBaseDirectory.listFiles()).thenReturn(contentBaseDirectory);

    }

    private void mockDirectoryWithCsvFile() throws Exception {
        File mockedBaseDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments("/baseDir").thenReturn(mockedBaseDirectory);
        Mockito.when(mockedBaseDirectory.exists()).thenReturn(true);

        File mockedSubDirectory = Mockito.mock(File.class);
        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments("/baseDir" + DirectoryService.DATA_PATH).thenReturn(mockedSubDirectory);
        Mockito.when(mockedSubDirectory.exists()).thenReturn(true);
        Mockito.when(mockedSubDirectory.getAbsolutePath()).thenReturn("/baseDir" + DirectoryService.DATA_PATH);

        File[] contentBaseDirectory = new File[1];
        contentBaseDirectory[0] = mockedSubDirectory;
        Mockito.when(mockedBaseDirectory.listFiles()).thenReturn(contentBaseDirectory);

        File[] files = new File[1];
        files[0] = Mockito.mock(File.class);
        Mockito.when(files[0].getAbsolutePath()).thenReturn("test.csv");
        Mockito.when(mockedSubDirectory.listFiles()).thenReturn(files);
    }
}
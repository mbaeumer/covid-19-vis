package se.mbaeumer.covid19vis;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import se.mbaeumer.covid19vis.services.*;
import se.mbaeumer.covid19vis.ui.ComboBoxAutoComplete;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CovidVisualizer extends Application{

	private static final String DATA_FOLDER_TEXT="Data folder: ";
	private static final String NOT_SET="not set";

	private Scene scene;
	private BorderPane borderPane;
	private FlowPane flowGeneral;
	private FlowPane flowGitCommands;
	private FlowPane flowGitInfo;
	private FlowPane flowRight;
	private Label lblFolder;
	private Button btnChooseFolder;
	private Button btnClone;
	private Button btnPull;
	private FlowPane flowReadCommands;
	private Button btnReadRawData;
	private Label lblGitInfo;
	private Label lblReadInfo;
	private Label lblPlaceholder;
	private FlowPane flowFilter;
	private Label lblFilterHeadingCompareCountries;
	private DatePicker datePicker;
	private ToggleGroup tGroupMetricType;
	private Label lblErrorMessage;
	private ComboBox<String> cmbCountries = new ComboBox<>();
	private Label lblFilterHeadingShowTrendForCountry;
	private ToggleGroup tGroupCountType;
	private RadioButton radCumulative;
	private RadioButton radDistributed;

	private GitService gitService;
	private CsvReader csvReader;
	private DataFilterService dataFilterService;
	private ConfigService configService;
	private DirectoryService directoryService;
	private List<CsvDataRow> rawCsvData;

	public void start(Stage stage) throws IOException {
		this.createBorderPane();
		this.scene = new Scene(this.borderPane, 900, 800, Color.LIGHTGRAY);
		stage.setTitle("COVID-19 visualizer");
		stage.setScene(this.scene);
		stage.show();
		this.initServices();
		this.initLayout();
		this.initConfig();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void initConfig() throws IOException {
		this.configService = new ConfigService();
		try {
			this.configService.readConfigFile();
			validateDirectory(new File(this.configService.getBaseDataFolder()));
		}catch(InvalidParameterException ex){
			lblGitInfo.setText(ex.getMessage());
			deactivateControls();
			lblGitInfo.setTextFill(Color.ORANGERED);
		}
	}

	private void validateDirectory(final File file){
		DirectoryValidationResult result = this.directoryService.validateDirectory(file.getAbsolutePath());
		lblGitInfo.setTextFill(Color.FORESTGREEN);
		if (result == DirectoryValidationResult.EMPTY){
			lblGitInfo.setText("The directory is empty! Continue cloning the repository!");
			deactivateControls();
			btnClone.setDisable(false);
			configService.setBaseDataFolder(file.getAbsolutePath());
			configService.writeConfigFile();
		}else if (result == DirectoryValidationResult.CSV_FILES){
			lblGitInfo.setText("The directory already contains csv files. Continue pulling!");
			activateControls();
			configService.setBaseDataFolder(file.getAbsolutePath());
			configService.writeConfigFile();
		}else{
			lblGitInfo.setText("The directory is not empty and is not suitable for the data");
			deactivateControls();
			lblGitInfo.setTextFill(Color.ORANGERED);
		}
	}

	private void deactivateControls(){
		this.btnClone.setDisable(true);
		this.btnPull.setDisable(true);
		this.btnReadRawData.setDisable(true);
		this.datePicker.setDisable(true);
		this.cmbCountries.setDisable(true);
		this.radCumulative.setDisable(true);
		this.radDistributed.setDisable(true);
	}

	private void activateControls(){
		this.btnClone.setDisable(false);
		this.btnPull.setDisable(false);
		this.btnReadRawData.setDisable(false);
		this.datePicker.setDisable(false);
		this.cmbCountries.setDisable(false);
		this.radCumulative.setDisable(false);
		this.radDistributed.setDisable(false);
	}

	private void initServices(){
		this.gitService = new GitService();
		this.directoryService = new DirectoryService();
		this.csvReader = new CsvReader(this.directoryService);
		this.dataFilterService = new DataFilterService();
	}
	
	public void initLayout(){
		this.createGeneralFlowPane();
		this.createRightFlowPane();
		this.createPlaceHolder();
		this.createGitFlowPane();
		this.createFolderInfoLabel();
		this.createFolderButton();
		this.createCloneButton();
		this.createPullButton();
		this.createGitInfoFlowPane();
		this.createGitInfoLabel();
		this.createReadFlowPane();
		this.createReadRawDataButton();
		this.createReadInfoLabel();
		this.createFilterFlowPane();
		this.createFilterHeadingCompareCountries();
		this.createDatePicker();
		this.createMetricsTypeRadioButtons();
		this.createErrorMessageLabel();
		this.createFilterHeadingTrend();
		this.createCountryCombobox();
		this.createCountTypeRadioButtons();
		this.printSizesAndLocation();
	}

	private void createBorderPane(){
		this.borderPane = new BorderPane();
	}
	
	public void createGeneralFlowPane() {
		this.flowGeneral = new FlowPane();
		this.flowGeneral.setOrientation(Orientation.VERTICAL);
		this.flowGeneral.setPrefWrapLength(700);
		this.flowGeneral.setVgap(10);

		this.flowGeneral.setBackground(createBackground());
		DropShadow dropShadow = new DropShadow(5, Color.GRAY);
		this.flowGeneral.setEffect(dropShadow);

		this.borderPane.setLeft(this.flowGeneral);
	}

	private Background createBackground(){
		Insets bgInsets = new Insets(5);
		BackgroundFill bgFill = new BackgroundFill(Color.WHITESMOKE, null, bgInsets);
		return new Background(bgFill);
	}

	public void createRightFlowPane() {
		this.flowRight = new FlowPane();
		this.flowRight.setOrientation(Orientation.VERTICAL);
		this.flowRight.setPrefWrapLength(700);
		this.flowRight.setVgap(10);

		this.flowRight.setBackground(createBackground());
		DropShadow dropShadow = new DropShadow(5, Color.GRAY);
		this.flowRight.setEffect(dropShadow);
		this.flowRight.prefHeightProperty().bind(this.flowGeneral.heightProperty());
		this.flowRight.prefWidthProperty().bind(this.flowGeneral.widthProperty());

		this.borderPane.setCenter(this.flowRight);
	}

	private void createPlaceHolder(){
		this.lblPlaceholder = new Label("No data available");
		this.lblPlaceholder.setPadding(new Insets(10));
		this.flowRight.getChildren().add(this.lblPlaceholder);
	}

	private void createGitFlowPane(){
		this.flowGitCommands = new FlowPane();
		this.flowGitCommands.setOrientation(Orientation.HORIZONTAL);
		this.flowGitCommands.setHgap(10);
		this.flowGitCommands.setPadding(new Insets(10,5,0,10));
		this.flowGitCommands.setBackground(createBackground());
		DropShadow dropShadow = new DropShadow(5, Color.GRAY);
		this.flowGitCommands.setEffect(dropShadow);
		this.flowGitCommands.setPrefHeight(70);
		this.flowGitCommands.setPrefWidth(890);

		this.borderPane.setTop(this.flowGitCommands);
	}

	private void createFolderInfoLabel(){
		this.lblFolder = new Label();
		this.lblFolder.setText(DATA_FOLDER_TEXT + NOT_SET);
		this.flowGitCommands.getChildren().add(this.lblFolder);
	}

	private void createFolderButton(){
		this.btnChooseFolder = new Button("Select folder");
		this.btnChooseFolder.setOnAction(actionEvent -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File selectedDirectory = directoryChooser.showDialog(flowGitCommands.getScene().getWindow());
			if (selectedDirectory != null){
				lblFolder.setText(DATA_FOLDER_TEXT + selectedDirectory.getAbsolutePath());
				validateDirectory(selectedDirectory);
				printSizesAndLocation();
			}
		});

		this.flowGitCommands.getChildren().add(this.btnChooseFolder);
	}

	private void createCloneButton(){
		this.btnClone = new Button("Clone");
		this.btnClone.setOnAction(this::handleCloneEvent);
		/*
		this.btnClone.setOnAction(actionEvent -> {
			try {
				lblGitInfo.setText("Started cloning...");
				//lblGitInfo.setText("Cloning the repository");
				//btnClone.setDisable(true);
				//gitService.cloneRepository(configService);
				//gitService.cloneRepository2(configService, lblGitInfo);
				gitService.cloneRepository3(configService, lblGitInfo);
				System.out.println("done");
				lblGitInfo.setText("Successfully cloned the repository");
			} catch (GitAPIException | JGitInternalException e) {
				lblGitInfo.setText(e.getMessage());
			}finally {
				btnClone.setDisable(false);
			}
		});
	*/
		this.flowGitCommands.getChildren().add(this.btnClone);
	}

	public void handleCloneEvent(ActionEvent actionEvent){
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					System.out.println("task running " + Thread.currentThread().getName());
					gitService.cloneRepository3(configService, lblGitInfo);
				} catch (GitAPIException | JGitInternalException e) {
					actionEvent.consume();
					try {
						stop();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					return null;
				}

				Platform.runLater(() -> {
					lblGitInfo.setText("Started cloning...");
				});
				return null;
			}
		};

		task.setOnSucceeded(wse -> {
			System.out.println("task succeeded UI " + Thread.currentThread().getName());
			activateControls();
		});

		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();


		/*
		new Thread(()-> {
			try {
				gitService.cloneRepository3(configService, lblGitInfo);
			} catch (GitAPIException | JGitInternalException e) {
				actionEvent.consume();
				try {
					stop();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				return;

			}

			Platform.runLater(() -> {
				lblGitInfo.setText("Started cloning...");
			});
		}).start();
		*/

	}

	private void createPullButton(){
		this.btnPull = new Button("Pull");
		this.btnPull.setOnAction(actionEvent -> {
			try {
				gitService.pull(configService);
				lblGitInfo.setText("Successfully pulled the repository");
			} catch (GitAPIException | JGitInternalException e) {
				lblGitInfo.setText(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		this.flowGitCommands.getChildren().add(this.btnPull);
	}

	private void createGitInfoFlowPane(){
		this.flowGitInfo = new FlowPane();
		this.flowGitInfo.setPadding(new Insets(5, 5, 0, 0));
		this.flowGitInfo.prefWidthProperty().bind(this.flowGitCommands.widthProperty());
		this.flowGitCommands.getChildren().add(this.flowGitInfo);
	}

	private void createGitInfoLabel(){
		this.lblGitInfo = new Label("Info");
		this.flowGitInfo.getChildren().add(lblGitInfo);
	}

	private void createReadFlowPane(){
		this.flowReadCommands = new FlowPane();
		this.flowReadCommands.setOrientation(Orientation.HORIZONTAL);
		this.flowReadCommands.setHgap(10);
		this.flowReadCommands.setPadding(new Insets(10, 5, 0, 10));
		this.flowGeneral.getChildren().add(this.flowReadCommands);
	}

	private void createReadRawDataButton(){
		this.btnReadRawData = new Button("Read raw data");
		this.btnReadRawData.setOnAction(actionEvent -> {
			printSizesAndLocation();
			csvReader.readMultipleCsvFiles(configService.getBaseDataFolder() + GitService.DATA_PATH);
			rawCsvData = csvReader.getCsvDataRowList();
			updateCountryComboBox();
			lblReadInfo.setText("Successfully read data: " + rawCsvData.size());
			printSizesAndLocation();
		});

		this.flowReadCommands.getChildren().add(this.btnReadRawData);
	}

	private void printSizesAndLocation(){
		System.out.println("borderPane width: " + borderPane.widthProperty().getValue());
		System.out.println("flowRight x: " + flowRight.getLayoutX());
		System.out.println("flowRight y: " + flowRight.getLayoutY());
		System.out.println("flowRight w: " + flowRight.widthProperty().getValue());
		System.out.println("flowLeft w: " + flowGeneral.widthProperty().getValue());
		System.out.println("flowGitCommands w: " + flowGitCommands.widthProperty().getValue());
		System.out.println("flowGitInfo w: " + flowGitInfo.widthProperty().getValue());

	}

	private void createReadInfoLabel(){
		this.lblReadInfo = new Label("Info");
		this.flowReadCommands.getChildren().add(lblReadInfo);
	}

	private void createFilterFlowPane(){
		this.flowFilter = new FlowPane();
		this.flowFilter.setOrientation(Orientation.VERTICAL);
		this.flowFilter.setHgap(10);
		this.flowFilter.setPadding(new Insets(5, 5, 0, 10));
		this.flowGeneral.getChildren().add(this.flowFilter);
	}

	private void createFilterHeadingCompareCountries(){
		this.lblFilterHeadingCompareCountries = new Label("Compare countries by date");
		this.lblFilterHeadingCompareCountries.setPadding(new Insets(5, 0, 5, 0));
		this.flowFilter.getChildren().add(this.lblFilterHeadingCompareCountries);
	}

	private void createMetricsTypeRadioButtons(){
		this.tGroupMetricType = new ToggleGroup();

		RadioButton radConfirmed = new RadioButton("confirmed");
		radConfirmed.setToggleGroup(tGroupMetricType);
		radConfirmed.setPadding(new Insets(5, 5, 5, 0));

		RadioButton radRecovered = new RadioButton("recovered");
		radRecovered.setToggleGroup(tGroupMetricType);
		radRecovered.setPadding(new Insets(0, 0, 5, 0));

		RadioButton radDeaths = new RadioButton("deaths");
		radDeaths.setToggleGroup(tGroupMetricType);
		radRecovered.setPadding(new Insets(0, 0, 5, 0));

		tGroupMetricType.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov,
								Toggle old_toggle, Toggle new_toggle) {
				if (tGroupMetricType.getSelectedToggle() != null) {
					LocalTime lt = LocalTime.MAX;
					if (datePicker.getValue() != null) {
						LocalDateTime ldt = LocalDateTime.of(datePicker.getValue(), lt);
						MetricsType metricsType = getMetricsType(new_toggle.toString());
						List<CsvDataRow> data = dataFilterService.getDataByDateAndMetricsType(rawCsvData, ldt, metricsType);
						createCountryGraph(data, metricsType);
					}
				}
			}
		});

		this.flowFilter.getChildren().addAll(radConfirmed, radRecovered, radDeaths);

	}

	private MetricsType getMetricsType(final String toggleValue){
		MetricsType metricsType = MetricsType.CONFIRMED;
		if (toggleValue.contains("recovered")){
			metricsType = MetricsType.RECOVERED;
		}else if (toggleValue.contains("death")){
			metricsType = MetricsType.DEATHS;
		}

		return metricsType;
	}

	private void createDatePicker(){
		this.datePicker = new DatePicker();

		this.datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
				if (rawCsvData != null) {
					LocalTime lt = LocalTime.MAX;
					LocalDateTime ldt = LocalDateTime.of(t1, lt);
					List<CsvDataRow> data = null;
					if (tGroupMetricType.getSelectedToggle() != null) {
						MetricsType metricsType = getMetricsType(tGroupMetricType.getSelectedToggle().toString());
						data = dataFilterService.getDataByDateAndMetricsType(rawCsvData, ldt, metricsType);
						createCountryGraph(data, metricsType);
					}else{
						data = dataFilterService.getDataByDateAndMetricsType(rawCsvData, ldt, MetricsType.CONFIRMED);
						createCountryGraph(data, MetricsType.CONFIRMED);
					}
					removeErrorMessage();
				}else{
					showErrorMessage();
				}

			}
		});

		this.flowFilter.getChildren().add(this.datePicker);
	}

	private void createErrorMessageLabel(){
		this.lblErrorMessage = new Label("");
	}

	private void showErrorMessage(){
		this.lblErrorMessage.setText("No data available!");
		this.flowFilter.getChildren().add(this.lblErrorMessage);
	}

	private void removeErrorMessage(){
		this.flowFilter.getChildren().remove(this.lblErrorMessage);
	}

	private void createCountryCombobox(){
		cmbCountries.setTooltip(new Tooltip());
		cmbCountries.getItems().addAll(dataFilterService.getCountryNames(rawCsvData));
		cmbCountries.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
				if (cmbCountries.getSelectionModel().getSelectedItem() != null) {
					System.out.println("create a graph for " + cmbCountries.getSelectionModel().getSelectedItem());
					if (radDistributed.isSelected()) {
						List<DailyCase> dailyCases = dataFilterService.getCasesByCountrySortedByDate(rawCsvData, cmbCountries.getSelectionModel().getSelectedItem());
						createDistributedTrendGraph(dailyCases);
					}else{
						List<CsvDataRow> data = dataFilterService.getDataForCountry(rawCsvData, cmbCountries.getSelectionModel().getSelectedItem());
						createTrendGraph(data);
					}
				}
			}
		});

		this.flowFilter.getChildren().add(this.cmbCountries);
		new ComboBoxAutoComplete<String>(cmbCountries);
	}

	private void createFilterHeadingTrend(){
		this.lblFilterHeadingShowTrendForCountry = new Label("Trend by country");
		this.lblFilterHeadingShowTrendForCountry.setPadding(new Insets(5, 5, 5, 0));
		this.flowFilter.getChildren().add(this.lblFilterHeadingShowTrendForCountry);
	}

	private void updateCountryComboBox(){
		cmbCountries.getItems().clear();
		cmbCountries.getItems().addAll(dataFilterService.getCountryNames(rawCsvData));
		new ComboBoxAutoComplete<String>(cmbCountries);
		System.out.println("selected item: " + cmbCountries.getSelectionModel().getSelectedItem());
		lblReadInfo.setText(cmbCountries.getSelectionModel().getSelectedItem());
	}

	private void createCountTypeRadioButtons(){
		this.tGroupCountType = new ToggleGroup();

		radCumulative = new RadioButton("cumulative");
		radCumulative.setToggleGroup(tGroupCountType);
		radCumulative.setPadding(new Insets(5, 5, 5, 0));

		radDistributed = new RadioButton("distributed");
		radDistributed.setToggleGroup(tGroupCountType);
		radDistributed.setPadding(new Insets(0, 0, 5, 0));

		tGroupCountType.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov,
								Toggle old_toggle, Toggle new_toggle) {
				if (radCumulative.isSelected()) {
					List<CsvDataRow> data = dataFilterService.getDataForCountry(rawCsvData, cmbCountries.getSelectionModel().getSelectedItem());
					createTrendGraph(data);
				}else if (radDistributed.isSelected()){
					createDistributedTrendGraph(dataFilterService.getCasesByCountrySortedByDate(rawCsvData, cmbCountries.getValue()));
				}
			}
		});

		this.flowFilter.getChildren().addAll(radCumulative, radDistributed);
	}

	private void createTrendGraph(final List<CsvDataRow> csvDataRows){
		if (csvDataRows.size() == 0){
			handleEmptyData();
			return;
		}

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<String,Number> bc =
				new LineChart<String,Number>(xAxis,yAxis);

		XYChart.Series series1 = new XYChart.Series();

		for (int i=0; i<csvDataRows.size(); i++){
			series1.getData().add(new XYChart.Data(getDateString(csvDataRows.get(i).getLastUpdated()), csvDataRows.get(i).getConfirmed()));
		}
		series1.setName("Confirmed cases - trend " + cmbCountries.getSelectionModel().getSelectedItem());
		bc.getData().addAll(series1);

		this.flowRight.getChildren().clear();
		this.flowRight.getChildren().add(bc);
	}

	private void createDistributedTrendGraph(final List<DailyCase> csvDataRows){
		if (csvDataRows.size() == 0){
			handleEmptyData();
			return;
		}

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String,Number> bc =
				new BarChart<String,Number>(xAxis,yAxis);

		XYChart.Series series1 = new XYChart.Series();

		for (int i=0; i<csvDataRows.size(); i++){
			series1.getData().add(new XYChart.Data(getDateString(csvDataRows.get(i).getDateTime()), csvDataRows.get(i).getNumber()));
		}
		series1.setName("Number of new cases/day in " + cmbCountries.getSelectionModel().getSelectedItem());
		bc.getData().addAll(series1);

		this.flowRight.getChildren().clear();
		this.flowRight.getChildren().add(bc);
	}

	private String getDateString(final LocalDateTime localDateTime){
		return localDateTime.format(DateTimeFormatter.ISO_DATE);
	}

	private void createCountryGraph(final List<CsvDataRow> csvDataRows, final MetricsType metricsType){
		if (csvDataRows.size() == 0){
			handleEmptyData();
			return;
		}
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String,Number> bc =
				new BarChart<String,Number>(xAxis,yAxis);

		XYChart.Series series1 = new XYChart.Series();
		//bc.prefWidthProperty().bind(this.flowRight.widthProperty());

		series1 = createDataSeries(csvDataRows, metricsType);
		bc.getData().clear();
		bc.getData().addAll(series1);

		this.flowRight.getChildren().clear();
		this.flowRight.getChildren().add(bc);
	}

	private XYChart.Series createDataSeries(final List<CsvDataRow> csvDataRows, final MetricsType metricsType){
		XYChart.Series series1 = new XYChart.Series();
		final int maxBars = 20;
		if (metricsType == MetricsType.CONFIRMED) {
			for (int i = 0; i < maxBars; i++) {
				series1.getData().add(new XYChart.Data(csvDataRows.get(i).getCountry(), csvDataRows.get(i).getConfirmed()));
			}
			series1.setName("Confirmed cases - " + datePicker.getValue());
		}else if (metricsType == MetricsType.RECOVERED){
			for (int i = 0; i < maxBars; i++) {
				series1.getData().add(new XYChart.Data(csvDataRows.get(i).getCountry(), csvDataRows.get(i).getRecovered()));
			}
			series1.setName("Recovered cases - " + datePicker.getValue());
		}else if (metricsType == MetricsType.DEATHS){
			for (int i = 0; i < maxBars; i++) {
				series1.getData().add(new XYChart.Data(csvDataRows.get(i).getCountry(), csvDataRows.get(i).getDeaths()));
			}
			series1.setName("Death cases - " + datePicker.getValue());
		}
		return series1;

	}

	private void handleEmptyData(){
		this.flowRight.getChildren().clear();
		this.flowRight.getChildren().add(this.lblPlaceholder);
	}
}
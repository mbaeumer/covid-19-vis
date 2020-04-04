package se.mbaeumer.covid19vis;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;

import java.io.IOException;
import java.util.List;

public class FXLink extends Application{

	private Group root = new Group();
	private Scene scene;
	private FlowPane flowGeneral;
	private FlowPane flowGitCommands;
	private Button btnClone;
	private Button btnPull;
	private FlowPane flowReadCommands;
	private Button btnReadRawData;
	private Label lblGitInfo;
	private Label lblReadInfo;

	private GitService gitService;
	private CsvReader csvReader;
	private DataFilterService dataFilterService;

	public void start(Stage stage) {
		this.scene = new Scene(this.root, 1100, 700, Color.WHITESMOKE);
		stage.setTitle("COVID-19 visualizer");
		stage.setScene(this.scene);
		stage.show();
		this.initLayout();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void initLayout() {
		this.gitService = new GitService();
		this.csvReader = new CsvReader(new DirectoryService());
		this.dataFilterService = new DataFilterService();
		this.createGeneralFlowPane();
		this.createGitFlowPane();
		this.createCloneButton();
		this.createPullButton();
		this.createGitInfoLabel();
		this.createReadFlowPane();
		this.createReadRawDataButton();
		this.createReadInfoLabel();
	}
	
	public void createGeneralFlowPane() {
		this.flowGeneral = new FlowPane();
		this.flowGeneral.setOrientation(Orientation.VERTICAL);
		this.flowGeneral.setPrefWrapLength(700);
		this.flowGeneral.setVgap(10);
		this.root.getChildren().add(this.flowGeneral);
	}

	private void createGitFlowPane(){
		this.flowGitCommands = new FlowPane();
		this.flowGitCommands.setOrientation(Orientation.HORIZONTAL);
		this.flowGitCommands.setHgap(10);
		this.flowGeneral.getChildren().add(this.flowGitCommands);
	}

	private void createCloneButton(){
		this.btnClone = new Button("Clone");
		this.btnClone.setOnAction(actionEvent -> {
			try {
				System.out.println("before...");
				gitService.cloneRepository();
				lblGitInfo.setText("Successfully cloned the repository");
			} catch (GitAPIException | JGitInternalException e) {
				lblGitInfo.setText(e.getMessage());
			}
		});

		this.flowGitCommands.getChildren().add(this.btnClone);
	}

	private void createPullButton(){
		this.btnPull = new Button("Pull");
		this.btnPull.setOnAction(actionEvent -> {
			try {
				gitService.pull();
				lblGitInfo.setText("Successfully pulled the repository");
			} catch (GitAPIException | JGitInternalException e) {
				lblGitInfo.setText(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		this.flowGitCommands.getChildren().add(this.btnPull);
	}

	private void createGitInfoLabel(){
		this.lblGitInfo = new Label("Info");
		this.flowGitCommands.getChildren().add(lblGitInfo);
	}

	private void createReadFlowPane(){
		this.flowReadCommands = new FlowPane();
		this.flowReadCommands.setOrientation(Orientation.HORIZONTAL);
		this.flowReadCommands.setHgap(10);
		this.flowGeneral.getChildren().add(this.flowReadCommands);
	}

	private void createReadRawDataButton(){
		this.btnReadRawData = new Button("Read raw data");
		this.btnReadRawData.setOnAction(actionEvent -> {
			csvReader.readMultipleCsvFiles(GitService.LOCAL_PATH + GitService.DATA_PATH);
			//Map dataMap = csvReader.getDataMap();
			//lblGitInfo.setText("Successfully read data: " + dataMap.size());
			/*
			csvReader.readSingleCsvFile(GitService.LOCAL_PATH + GitService.DATA_PATH
					 + "03-27-2020.csv");
					 */
			List<CsvDataRow> dataRows = csvReader.getCsvDataRowList();
			dataRows = dataFilterService.getDataForCountry(dataRows, "Sweden");//getCountriesWithoutProvinces(dataRows);
			createGraph(dataRows);
			lblReadInfo.setText("Successfully read data: " + dataRows.size());
		});

		this.flowReadCommands.getChildren().add(this.btnReadRawData);
	}

	private void createReadInfoLabel(){
		this.lblReadInfo = new Label("Info");
		this.flowReadCommands.getChildren().add(lblReadInfo);
	}

	private void createGraph(final List<CsvDataRow> csvDataRows){
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String,Number> bc =
				new BarChart<String,Number>(xAxis,yAxis);

		XYChart.Series series1 = new XYChart.Series();

		for (int i=0; i<csvDataRows.size(); i++){
			series1.getData().add(new XYChart.Data(csvDataRows.get(i).getLastUpdated().toString(), csvDataRows.get(i).getConfirmed()));
		}
		series1.setName("Confirmed COVID-19 cases 2020-03-27");
		/*
		series1.getData().add(new XYChart.Data(csvDataRows.get(0).getCountry(), csvDataRows.get(0).getConfirmed()));
		series1.getData().add(new XYChart.Data(csvDataRows.get(1).getCountry(), csvDataRows.get(1).getConfirmed()));
		series1.getData().add(new XYChart.Data(csvDataRows.get(2).getCountry(), csvDataRows.get(2).getConfirmed()));
		series1.getData().add(new XYChart.Data(csvDataRows.get(3).getCountry(), csvDataRows.get(3).getConfirmed()));
		series1.getData().add(new XYChart.Data(csvDataRows.get(4).getCountry(), csvDataRows.get(4).getConfirmed()));
		*/
		bc.getData().addAll(series1);

		this.flowGeneral.getChildren().add(bc);
	}

	

}
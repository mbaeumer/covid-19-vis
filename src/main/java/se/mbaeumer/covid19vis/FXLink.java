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
	private Button btnCreateItem;
	private Button btnPull;
	private Button btnVisualize;
	private Label lblInfo;

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
		this.csvReader = new CsvReader();
		this.dataFilterService = new DataFilterService();
		this.createGeneralFlowPane();
		this.createCloneButton();
		this.createPullButton();
		this.createInfoLabel();
		this.createVisualizeButton();
	}
	
	public void createGeneralFlowPane() {
		this.flowGeneral = new FlowPane();
		this.flowGeneral.setOrientation(Orientation.VERTICAL);
		this.flowGeneral.setPrefWrapLength(700);
		this.flowGeneral.setVgap(10);
		this.root.getChildren().add(this.flowGeneral);
	}

	private void createCloneButton(){
		this.btnCreateItem = new Button("Clone");
		this.btnCreateItem.setOnAction(actionEvent -> {
			try {
				System.out.println("before...");
				gitService.cloneRepository();
				lblInfo.setText("Successfully cloned the repository");
			} catch (GitAPIException | JGitInternalException e) {
				lblInfo.setText(e.getMessage());
			}
		});

		this.flowGeneral.getChildren().add(this.btnCreateItem);
	}

	private void createPullButton(){
		this.btnPull = new Button("Pull");
		this.btnPull.setOnAction(actionEvent -> {
			try {
				System.out.println("before...");
				gitService.pull();
				lblInfo.setText("Successfully pulled the repository");
			} catch (GitAPIException | JGitInternalException e) {
				lblInfo.setText(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		this.flowGeneral.getChildren().add(this.btnPull);
	}

	private void createInfoLabel(){
		this.lblInfo = new Label("Info");
		this.flowGeneral.getChildren().add(lblInfo);
	}

	private void createVisualizeButton(){
		this.btnVisualize = new Button("Visualize");
		this.btnVisualize.setOnAction(actionEvent -> {
			csvReader.readCsvFile(GitService.LOCAL_PATH + "/"
					+ "csse_covid_19_data/csse_covid_19_daily_reports/" + "03-27-2020.csv");
			List<CsvDataRow> dataRows = csvReader.getCsvDataRowList();
			dataRows = dataFilterService.getCountriesWithoutProvinces(dataRows);
			createGraph(dataRows);
			lblInfo.setText("Successfully read data: " + dataRows.size());
		});
		//final LineChart
		this.flowGeneral.getChildren().add(this.btnVisualize);

	}

	private void createGraph(final List<CsvDataRow> csvDataRows){
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String,Number> bc =
				new BarChart<String,Number>(xAxis,yAxis);

		XYChart.Series series1 = new XYChart.Series();

		for (int i=0; i<30; i++){
			series1.getData().add(new XYChart.Data(csvDataRows.get(i).getCountry(), csvDataRows.get(i).getConfirmed()));
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
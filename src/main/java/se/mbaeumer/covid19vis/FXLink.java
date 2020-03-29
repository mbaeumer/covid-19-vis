package se.mbaeumer.covid19vis;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;

import java.io.IOException;

public class FXLink extends Application{

	private Group root = new Group();
	private Scene scene;
	private FlowPane flowGeneral;
	private Button btnCreateItem;
	private Button btnPull;
	private Label lblInfo;

	private GitService gitService;

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
		this.createGeneralFlowPane();
		this.createCloneButton();
		this.createPullButton();
		this.createInfoLabel();
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


	

}
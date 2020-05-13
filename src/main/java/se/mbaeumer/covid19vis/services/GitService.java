package se.mbaeumer.covid19vis.services;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.lib.ThreadSafeProgressMonitor;

import java.io.File;
import java.io.IOException;

public class GitService {

    private Git git;

    private CloneCommand cloneCommand;

    public static final String DATA_PATH = "/csse_covid_19_data/csse_covid_19_daily_reports/";

    public void cloneRepository(final ConfigService configService) throws GitAPIException {
        git = Git.cloneRepository()
                .setURI("https://github.com/CSSEGISandData/COVID-19")
                .setDirectory(new File(configService.getBaseDataFolder()))
                .call();

    }

    public void cloneRepository3(final ConfigService configService, Label label) throws GitAPIException {
        cloneCommand = Git.cloneRepository().setURI("https://github.com/CSSEGISandData/COVID-19")
                .setDirectory(new File(configService.getBaseDataFolder()));
        Git git = cloneCommand
                .setProgressMonitor(new CustomMonitor(label))
                .call();
    }

    public void cloneRepository2(final ConfigService configService, final Label label) throws GitAPIException {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // Set the total number of steps in our process
                int steps = 1000;
                System.out.println("step 1");
                updateMessage("cloning...");
                Git git = Git.cloneRepository()
                        .setURI("https://github.com/CSSEGISandData/COVID-19")
                        .setDirectory(new File(configService.getBaseDataFolder()))
                        .setProgressMonitor(new TextProgressMonitor())
                        .call();

                System.out.println("step 2");

                Thread.sleep(10);

                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnFailed(wse -> {
            wse.getSource().getException().printStackTrace();
        });

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            System.out.println("succeeded");
            label.textProperty().unbind();
            label.setText("Done");
        });

        // Before starting our task, we need to bind our UI values to the properties on the task
        //progressBar.progressProperty().bind(task.progressProperty());
        label.textProperty().bind(task.messageProperty());

        // Now, start the task on a background thread
        new Thread(task).start();

    }

    public void pull(final ConfigService configService) throws IOException, GitAPIException {
        git = Git.open(new File(configService.getBaseDataFolder()));
        git.pull().call();
    }
}

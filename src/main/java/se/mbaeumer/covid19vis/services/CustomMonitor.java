package se.mbaeumer.covid19vis.services;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.eclipse.jgit.lib.BatchingProgressMonitor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomMonitor extends BatchingProgressMonitor {
    private Label label;
    private Button button;

    public CustomMonitor(final Label label, final Button button) {
        this.label = label;
        this.button = button;
    }

    @Override
    protected void onUpdate(String s, int i) {

    }

    @Override
    protected void onEndTask(String s, int i) {

    }

    @Override
    protected void onUpdate(String s, int i, int i1, int i2) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                now = LocalDateTime.now();

                String message = "Status: " + s + " " + i + "/" + i1 + " (" + i2 + "%)";

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run(){
                        label.setText(message);
                    }
                });
                return null;
            }

        };

        task.setOnSucceeded(wse -> {
            label.textProperty().unbind();
            if (i2 == 100) {
                label.setText("Successfully cloned the repository");
                button.setDisable(false);
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void onEndTask(String s, int i, int i1, int i2) {
    }
}

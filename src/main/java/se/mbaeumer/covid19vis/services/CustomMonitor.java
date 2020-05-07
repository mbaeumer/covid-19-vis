package se.mbaeumer.covid19vis.services;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import org.eclipse.jgit.lib.BatchingProgressMonitor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomMonitor extends BatchingProgressMonitor {
    private Label label;
    private int count;

    public CustomMonitor(Label label) {
        this.label = label;
        count = 0;
    }

    @Override
    protected void onUpdate(String s, int i) {
        System.out.println("testasync" + s + i);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        label.setText("testasync " + s + i + " " +now.format(formatter) );
    }

    @Override
    protected void onEndTask(String s, int i) {
        System.out.println("done" + s + i);
    }

    @Override
    protected void onUpdate(String s, int i, int i1, int i2) {
        //System.out.println("testasync 4: " + "," + s + "," + i + "," + i1 +  "," + i2);
        count++;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //Thread.sleep(1);
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                System.out.println("in task " + now.format(formatter) + " " + count);
                now = LocalDateTime.now();

                String formattedDateTime = now.format(formatter);
                String message = "Status: " + s + " " + i2 + "%" +formattedDateTime + " " + count;
                System.out.println(message);
                //updateMessage(message);

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //now = LocalDateTime.now();
                //System.out.println("Time: " + now.format(formatter) + " " + count);
                //label.setText(message);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run(){
                        /*
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        */
                        label.setText(message);
                    }
                });
                return null;
            }

        };

        task.setOnSucceeded(wse -> {
            label.textProperty().unbind();
            //label.setText("Done");
        });

        task.setOnScheduled(workerStateEvent -> {
            label.setText("Started cloning...");
        });

        //label.textProperty().bind(task.messageProperty());
        // Now, start the task on a background thread
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        //new Thread(task).start();
    }

    @Override
    protected void onEndTask(String s, int i, int i1, int i2) {
        System.out.println("done: " + "," + s + "," + i + "," + i1 +  "," + i2);
    }
}

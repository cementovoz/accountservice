package com.bitbucket.pelenthium.accountservice.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cementovoz on 22.08.14.
 */
public class Launcher extends Application {

    private static final Logger LOG = LogManager.getLogger(Launcher.class);
    private ClientController controller;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = createGui();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Account Client");
        scheduleLoadStats();
    }

    @Override
    public void stop() throws Exception {
        LOG.info("Shutdown executor");
        scheduler.shutdownNow();
    }

    private void scheduleLoadStats() {
        scheduler.scheduleAtFixedRate(ClientTask.getLoadStatTask(controller), 0, 30, TimeUnit.SECONDS);
    }

    private BorderPane createGui() throws Exception {
        controller = new ClientController();
        try {
            return controller.getRoot();
        } catch (Exception exc) {
            System.out.println("Ooops. Cann't create client " + exc.getMessage());
            throw exc;
        }
    }

    public static void main(String[] args) {
        launch(Launcher.class);
    }
}

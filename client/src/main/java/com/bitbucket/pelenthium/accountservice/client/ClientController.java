package com.bitbucket.pelenthium.accountservice.client;

import com.bitbucket.pelenthium.accountservice.client.data.Statistic;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by cementovoz on 22.08.14.
 */
public class ClientController implements Initializable {
    private static final String MAIN_FXML = "/com/bitbucket/pelenthium/accountservice/client/main.fxml";
    private static final Logger LOG = LogManager.getLogger(ClientController.class);
    private static final NumberStringConverter CONVERTER = new SafeNumberStringConverter();
    private SimpleBooleanProperty status = new SimpleBooleanProperty();
    private ClientHandlers handlers;

    @FXML
    private Slider readerSlider;
    @FXML
    private Slider writerSlider;
    @FXML
    private TextField readerField;
    @FXML
    private TextField writerField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField ids;
    @FXML
    private ToggleButton startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button clearButton;
    @FXML
    private Label countAddAll;
    @FXML
    private Label countAddPer;
    @FXML
    private Label countGetAll;
    @FXML
    private Label countGetPer;
    @FXML
    private Shape statusShape;
    @FXML
    private Label statusLine;
    @FXML
    private ProgressBar progressBar;


    public ClientController() {
        handlers  = new ClientHandlers(this);
    }

    public BorderPane getRoot () throws Exception  {
        LOG.debug("Load fxml from " + MAIN_FXML);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_FXML));
        loader.setController(this);
        return (BorderPane) loader.load();
    }

    @Override
    public void initialize(URL location,  ResourceBundle resources) {
        LOG.debug("Initialize fxml");
        Bindings.bindBidirectional(readerField.textProperty(), readerSlider.valueProperty(), CONVERTER);
        Bindings.bindBidirectional(writerField.textProperty(), writerSlider.valueProperty(), CONVERTER);
        startButton.disableProperty().bind(new BooleanBinding() {
            {
                bind(status);
                bind(startButton.selectedProperty());
            }
            @Override
            protected boolean computeValue() {
                if (status.get() && !startButton.isSelected()) {
                    return false;
                }
                return true;
            }
        });
        clearButton.disableProperty().bind(getStatusBinding());
        stopButton.disableProperty().bind(new BooleanBinding() {
            {
                bind(startButton.selectedProperty());
            }
            @Override
            protected boolean computeValue() {
                return !startButton.isSelected();
            }
        });
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (startButton.isSelected()) {
                    startButton.setSelected(false);
                }
            }
        });
        status.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,  Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    statusShape.setFill(Color.GREEN);
                } else {
                    statusShape.setFill(Color.RED);
                }
            }
        });
        refreshButton.setOnAction(handlers.getRefreshHandler());
        startButton.setOnAction(handlers.getStartHandler());
        stopButton.setOnAction(handlers.getStopHandler());
        clearButton.setOnAction(handlers.getClearHandler());
    }

    private BooleanBinding getStatusBinding() {
        return new BooleanBinding() {
            {
                bind(status);
            }
            @Override
            protected boolean computeValue() {
                return !status.get();
            }
        };
    }

    public void setStatistic(Statistic statistic) {
        countGetAll.setText(String.valueOf(statistic.getCountGet()));
        countAddAll.setText(String.valueOf(statistic.getCountAdd()));
        long minutes = statistic.getTime() / 1000000000 / 60;
        minutes = minutes <= 0 ? 1 : minutes;
        LOG.info("Server run " + minutes + " min.");
        countAddPer.setText(String.valueOf(statistic.getCountAdd() / minutes) + "/мин");
        countGetPer.setText(String.valueOf(statistic.getCountGet() / minutes) + "/мин");
    }

    public void showProgress() {
        progressBar.setVisible(true);
    }

    public void hideProgress() {
        progressBar.setVisible(false);
    }

    public void setStartIsActive() {
        startButton.setSelected(false);
    }

    public Long getAmount() {
        Long aLong = null;
        try {
            aLong = Long.valueOf(amountField.getText());
        } catch (NumberFormatException e) {
            aLong = 0L;
        }
        return aLong;
    }

    private static class SafeNumberStringConverter extends NumberStringConverter {
        @Override
        public Number fromString( String value) {
            Number numberValue = super.fromString(value);
            if (numberValue == null) {
                return 0;
            }
            return numberValue;
        }
        @Override
        public String toString( Number value) {
            return super.toString(value.intValue());
        }
    }

    public SimpleBooleanProperty getStatusProperty() {
        return status;
    }

    public void setStatus (String title) {
        statusLine.setText(title);
    }

    public void clearStatus () {
        statusLine.setText("");
    }

    public int getReaderThreadCount () {
        return readerSlider.valueProperty().intValue();
    }

    public int getWriterThreadCount () {
        return writerSlider.valueProperty().intValue();
    }

    public int[] getIds() {
        String strIds = ids.getText().trim();
        String[] arrIds = strIds.split(",");
        int[] results = new int[arrIds.length];
        for (int i = 0; i < arrIds.length; i++) {
            try {
                results[i] = Integer.parseInt(arrIds[i].trim());
            } catch (Exception exc) {
                results[i] = 0;
            }
        }
        return results;
    }
}

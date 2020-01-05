package com.vg.myredis.view;

import com.vg.myredis.service.RedisService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Redis details view shows keys and redis server stats
 */
public class RedisDetailsView extends VBox implements Initializable {
    @FXML
    Parent root;

    @FXML
    private Label leftPanelTitle;

    @FXML
    private ListView<String> redisKeysTree;

    @FXML
    private TextField redisKey;

    @FXML
    private TextField lookUpField;

    @FXML
    private TextArea keyValue;

    @FXML
    private TextField keyfield;

    @FXML
    private TextField valuefield;



    @FXML
    private SplitPane splitView;

    private RedisService redisService;

    public RedisDetailsView(Parent root, RedisService redisService) throws IOException {
        this.redisService = redisService;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../../redis_details_view.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        Pane view = fxmlLoader.load();
        root.getScene().setRoot(view);
    }

    /**
     * File > New connection menu
     *
     * @param actionEvent
     */
    public void showMainScreen(ActionEvent actionEvent) throws IOException {
        redisService.close(); // We need to close connection before opening a new one
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../../main_screen.fxml"));
        Pane view = fxmlLoader.load();
        root.getScene().setRoot(view);
    }

    /**
     * File > Exit
     *
     * @param actionEvent
     */
    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * View redis stats
     *
     * @param actionEvent
     */
//    public void showServerInfo(ActionEvent actionEvent) {
//        String info = redisService.getInfo("server");
//        redisServerInfo.setText(info);
//    }
//
//    public void showClusterInfo(ActionEvent actionEvent) {
//        String info = redisService.getInfo("cluster");
//        redisServerInfo.setText(info);
//    }
//
//    public void showClientInfo(ActionEvent actionEvent) {
//        String info = redisService.getInfo("clients");
//        redisServerInfo.setText(info);
//    }
//
//    public void showStatsInfo(ActionEvent actionEvent) {
//        String info = redisService.getInfo("stats");
//        redisServerInfo.setText(info);
//    }

    /**
     * About menu
     *
     * @param actionEvent
     */
    public void showAboutDialog(ActionEvent actionEvent) {
        Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setTitle("myRedis - About");
        aboutDialog.setHeaderText(null);
        aboutDialog.setContentText("Simple Redis UI client\nAuthor: Memphisvl github.com/memphisvl");
        aboutDialog.show();
    }

    /**
     * Button handlers
     *
     * @param actionEvent
     */
    public void refreshKeys(ActionEvent actionEvent) {
        loadRedisKeys();
    }

    public void initialize(URL location, ResourceBundle resources) {
        splitView.setDividerPosition(0, .35d);
        redisKey.setText("Select key...");

        loadRedisKeys();
//        showServerInfo(null);
    }

    private void loadRedisKeys() {
        String keysInfo = redisService.getKeysInfo();
        Set<String> allKeys = redisService.getAllKeys();
        ObservableList<String> redisKeys = FXCollections.observableArrayList();

        redisKeys.add("DB0 - " + keysInfo);
        for (String key : allKeys) {
            redisKeys.add(key);
        }
        redisKeysTree.setItems(redisKeys);
        redisKeysTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    String value = redisService.getKey(newValue);
                    if (value != null && !"".equals(value)) {
                        redisKey.setText(newValue);
                        keyValue.setText(value);
                    }
                }
            }
        });
    }
    public void addKeyValue(){
        String key = keyfield.getText();
        String value = valuefield.getText();
        redisService.setKey(key,value);
        this.loadRedisKeys();
    }
    public void LookUpKey() {
        String key = lookUpField.getText();
        String value = redisService.getKey(key);
        if (value != "") keyValue.setText(value);
        else keyValue.setText("Key Value Doesn't Exist !");

    }
}

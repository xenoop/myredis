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
    /**
     * l'instance des differentes composantes de la page (coté graphique avec javafx)
     */
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
    private TextArea valuefield;


    @FXML
    private SplitPane splitView;

    private RedisService redisService;

    /**
     * l'instance de la page avec le service de redis
     * @param root
     * @param redisService
     * @throws IOException
     */
    public RedisDetailsView(Parent root, RedisService redisService) throws IOException {
        this.redisService = redisService;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../../redis_details_view.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        Pane view = fxmlLoader.load();
        root.getScene().setRoot(view);
    }
    /**
     * Button handlers
     *
     * @param actionEvent
     */
    public void refreshKeys(ActionEvent actionEvent) {
        loadRedisKeys();
    }
    /**
     * les instructions à executer lors du loading de la page , on va appeler les clés existantes dans la base redis
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        splitView.setDividerPosition(0, .35d);
        redisKey.setText("Select key...");
        loadRedisKeys();
//        showServerInfo(null);
    }
    /**
     * l'appel des methodes qui se trouve dans la base redis
     */
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

    /**
     * l'ajout d'un key value dans la base
     */

    public void addKeyValue() {
        String key = keyfield.getText();
        String value = valuefield.getText();
        redisService.setKey(key, value);
        this.loadRedisKeys();
    }
    /**
     * La recherche d'une clé valeur
     */    public void LookUpKey() {
        String key = lookUpField.getText();
        String value = redisService.getKey(key);
        if (value != "") keyValue.setText(value);
        else keyValue.setText("Key Value Doesn't Exist !");

    }

    /**
     * La supression d'une clé valeur
     */
    public void deleteKey() {
        String key = redisKeysTree.getSelectionModel().getSelectedItem().toString();
        redisService.deleteKey(key);
        this.loadRedisKeys();
    }
}

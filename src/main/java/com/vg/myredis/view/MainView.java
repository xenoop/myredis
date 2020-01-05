package com.vg.myredis.view;


import com.vg.myredis.service.RedisService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is main application view
 */
public class MainView extends GridPane implements Initializable {
    @FXML
    Parent root;

    @FXML
    TextFlow appTitle;

    @FXML
    TextField redisHost;

    @FXML
    TextField redisPort;

    @FXML
    Label errorMsg;

    private RedisService redisService;

    public void connectToRedis(ActionEvent event) throws IOException {
        String hostname = redisHost.getText();
        String port = redisPort.getText();
        port = StringUtils.isNumeric(port) ? port : "6379";

        redisService = new RedisService(hostname, Integer.valueOf(port));
        try {
            if (redisService.ping() != null) {
                new RedisDetailsView(root, redisService);
            }
        } catch (JedisException ex) {
            errorMsg.setTextFill(Color.valueOf("#D82C27"));
            errorMsg.setVisible(true);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        initTitle();
    }

    private void initTitle() {
        Text part1 = new Text("Welcome to ");
        Text part2 = new Text("myRedis");

        part1.setFill(Color.valueOf("#555"));
        part2.setFill(Color.valueOf("#D82C27"));

        appTitle.getChildren().addAll(part1, part2);
        appTitle.setPadding(new Insets(0, 0, 10, 0));
    }
}

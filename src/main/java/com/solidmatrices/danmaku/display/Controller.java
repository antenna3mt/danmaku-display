package com.solidmatrices.danmaku.display;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    @FXML
    public TextField serverText;
    @FXML
    public TextField tokenText;
    @FXML
    public TextField danmakuText;
    @FXML
    public TextField fontSizeText;
    @FXML
    public TextField durationText;
    @FXML
    public TextArea statusTextArea;
    @FXML
    public ToggleButton fetchToggleButton;
    @FXML
    public MenuButton screenMenu;

    @FXML
    public void connect() {
        if (!Model.INSTANCE.connect(serverText.getText(), tokenText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Connect Failed");
            alert.showAndWait();
        }
    }


    @FXML
    public void toggleFetch() {
        Model.INSTANCE.Fetching = fetchToggleButton.isSelected();
    }

    @FXML
    public void clearDisplay() {
        Model.INSTANCE.ClearDanmaku();
    }

    @FXML
    public void test() {
        Model.INSTANCE.AddDanmaku(new Danmaku("Test"));
    }

    @FXML
    public void updateSettings() {
        Model.INSTANCE.FontSize = Double.parseDouble(fontSizeText.getText());
        Model.INSTANCE.Duration = Double.parseDouble(durationText.getText());
    }

    @FXML
    public void push() {
        String text = danmakuText.getText();
        Model.INSTANCE.AddDanmaku(new Danmaku(text));
        danmakuText.setText("");
    }

    @FXML
    public void refreshScreens() {
        screenMenu.getItems().clear();
        for (String s : Model.INSTANCE.window.getScreens()) {
            MenuItem mi = new MenuItem(s);
            mi.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Model.INSTANCE.window.setScreen(s);
                }
            });
            screenMenu.getItems().add(mi);
        }
    }

    public void WriteStatus(String s) {
        //statusTextArea.getParagraphs().add(s);
    }

    public void Sync() {
        serverText.setText(Model.INSTANCE.ServerUrl);
        refreshScreens();
        fontSizeText.setText(Double.toString(Model.INSTANCE.FontSize));
        durationText.setText(Double.toString(Model.INSTANCE.Duration));
        fetchToggleButton.setSelected(Model.INSTANCE.Fetching);

    }
}

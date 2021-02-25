package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;

public class HomeStage {
    @FXML public Label lb_username;
    @FXML public Button btn_newGame;
    @FXML public Button btn_gameHistory;
    @FXML public Button btn_avt;
    @FXML public Button btn_logout;
    @FXML public ImageView imgAvt;
    @FXML public Label lbUser;
    @FXML public Label lbEmail;
    @FXML public Label lbNumOfWin;
    @FXML public Label lbNumOfLoss;

    public void initComponent(){
        lb_username.setText(Main_login.user);
        String avtDir = getAvatarDir(Main_login.avt);
        System.out.println(avtDir);

        Image img = new Image("img/game.png");
        ImageView newGameView = new ImageView(img);
        newGameView.setFitHeight(120);
        newGameView.setPreserveRatio(true);
        btn_newGame.setGraphic(newGameView);

        img = new Image("img/cham.png");
        ImageView gameHistoryView = new ImageView(img);
        gameHistoryView.setFitHeight(120);
        gameHistoryView.setPreserveRatio(true);
        btn_gameHistory.setGraphic(gameHistoryView);

        img = new Image("img/logout.png");
        ImageView logoutView = new ImageView(img);
        logoutView.setFitHeight(70);
        logoutView.setPreserveRatio(true);
        btn_logout.setGraphic(logoutView);

        img = new Image(avtDir);
        ImageView avtView = new ImageView(img);
        avtView.setFitHeight(60);
        avtView.setPreserveRatio(true);
        btn_avt.setGraphic(avtView);

        initProfile(img);
        setListener();
    }

    private String getAvatarDir(int avt_id){
        return "img/avatar/"
                + ((avt_id < 10)? "0" + avt_id : "" + avt_id) + ".png";
    }

    private void initProfile(Image img){
        imgAvt.setImage(img);
        lbUser.setText(Main_login.user);
        lbEmail.setText(Main_login.email);
    }

    public void setListener(){
        btn_newGame.setOnAction(actionEvent -> {
            try {
                Main_login.gotoGameLobby();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn_logout.setOnAction(actionEvent -> logout());
    }

    public void logout(){
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Bạn muốn đăng xuất?",
                    ButtonType.YES,
                    ButtonType.NO);
            confirm.showAndWait();

            if(confirm.getResult() == ButtonType.YES){
                FileWriter fw = new FileWriter(Main_login.dir
                                + "/Model/myLocalCookie.csv");
                fw.write("");
                fw.close();
                Main_login.gotoLoginStage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize(MouseEvent e){
        ((Stage)((Label)e.getSource()).getScene().getWindow()).setIconified(true);
    }
}

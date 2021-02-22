package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        String avtDir = "img/avt.png";
        try {
            avtDir = "img/avatar/Avatars Set Flat Style-"
                    + LocalCookieControll.getAvt() + ".png";
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void initProfile(Image img){
        imgAvt.setImage(img);

        lbUser.setText(Main_login.user);
        lbEmail.setText(Main_login.email);
    }

    public void setListener(){
        btn_newGame.setOnAction(actionEvent -> {
//            String url = "http://0974b25300c9.ngrok.io";
//            url = Main_login.hostUrl;
//            System.out.println("#" + num + ". send data");
//            IO.Options options = IO.Options.builder().build();
//            Socket socket = IO.socket(URI.create(url), options);
//            socket.connect();
//            num++;
//            socket.emit("testEmit", "Hellooooo num#" + num);
            try {
                Main_login.gotoGameLobby();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn_logout.setOnAction(actionEvent -> {
            try {
                FileWriter fw =
                        new FileWriter(Main_login.dir
                                + "/Model/myLocalCookie.csv");
                fw.write("");
                fw.close();
                Main_login.gotoLoginStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        System.exit(0);
    }

    public void minimize(MouseEvent e){
        ((Stage)((Label)e.getSource()).getScene().getWindow()).setIconified(true);
    }
}

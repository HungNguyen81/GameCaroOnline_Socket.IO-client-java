package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;

public class Main_login extends Application {
    public static double xOffset = 0;
    public static double yOffset = 0;
    public static String user;
    public static String email;
    public static String dir = System.getProperty("user.dir");
    public static String hostUrl = "http://localhost:3000/";
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        dir = dir + "/src/main/java";
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginStage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);

        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("CaroH Login");

        String data = LocalCookieControll.getCookie();
        if(data != null) {
            String[] cookie = data.split(",");
            System.out.println(cookie[0] + "," + cookie[1]);
            String res = new ConnectServer().createConnect(hostUrl + "login", cookie[0], cookie[1]);
            if (res.contains("Login OK")) {
                user = cookie[0];
                gotoHomeStage();
                return;
            } else {
                LocalCookieControll.setCookie(); // Wipe cookie
            }
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void gotoHomeStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main_login.class.getResource("/homeState.fxml"));
        Parent root = loader.load();

        //Make stage draggable
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);

        HomeStage cc = loader.getController();
        cc.initComponent();
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("CaroH Home");

        stage.show();
    }

    public static void gotoSignupStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main_login.class.getResource("/signupStage.fxml"));
        Parent root = loader.load();

        SignupStage ss = loader.getController();
        ss.init();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("CaroH Signup");
    }
    public static void gotoLoginStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main_login.class.getResource("/loginStage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("CaroH Login");
    }

    public static void gotoGameLobby() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main_login.class.getResource("/gameLobby.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("CaroH Lobby");
    }
    public static void main(String[] args) {
        launch(args);
    }
}

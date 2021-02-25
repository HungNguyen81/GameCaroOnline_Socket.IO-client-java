package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;

public class Main_login extends Application {
    public static double xOffset = 0;
    public static double yOffset = 0;
    public static String user;
    public static int userNumber;
    public static int avt;
    public static String email;
    public static String dir = System.getProperty("user.dir");
    public static String hostUrl = "http://localhost:3000/";
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        dir = dir + "/src/main/java";
        stage = new Stage();
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/loginStage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("CaroH Login");

        String data = LocalCookieController.getCookie();

        // if have cookie
        if(data != null) {
            String[] cookie = data.split(",");
            // Log
            System.out.println(cookie[LocalCookieController.COOKIE_USER_NAME] + ","
                             + cookie[LocalCookieController.COOKIE_PASSWORD]);

            // Connect server to login
            String res = new ConnectServer()
                    .createConnect(hostUrl + "login",
                            cookie[LocalCookieController.COOKIE_USER_NAME],
                            cookie[LocalCookieController.COOKIE_PASSWORD]);

            if (res.contains("Login OK")) {
                user = cookie[LocalCookieController.COOKIE_USER_NAME];
                avt = Integer.parseInt(cookie[LocalCookieController.COOKIE_AVATAR]);
                gotoHomeStage();
                return;
            } else {
                LocalCookieController.setCookie(); // Wipe cookie
            }
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void gotoHomeStage() throws IOException {
        FXMLLoader loader =
                new FXMLLoader(Main_login.class.
                        getResource("/homeState.fxml"));
        Parent root = loader.load();

        //Make stage draggable
        makeDraggable(root);
        Scene scene = new Scene(root);
        HomeStage cc = loader.getController();
        cc.initComponent();
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("CaroH - Home");
        stage.show();
    }

    public static void gotoSignupStage() throws IOException {
        FXMLLoader loader =
                new FXMLLoader(Main_login.class.
                        getResource("/signupStage.fxml"));
        Parent root = loader.load();

        SignupStage ss = loader.getController();
        ss.init();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("CaroH - Signup");
    }
    public static void gotoLoginStage() throws IOException {
        FXMLLoader loader =
                new FXMLLoader(Main_login.class.
                        getResource("/loginStage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setTitle("CaroH - Login");
    }

    public static void gotoGameLobby() throws IOException {
        FXMLLoader loader =
                new FXMLLoader(Main_login.class.
                        getResource("/gameLobby.fxml"));
        try{
            Parent root = loader.load();
            makeDraggable(root);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.setTitle("CaroH - Game Lobby");
        } catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Something went wrong!",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void makeDraggable(Parent root){
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

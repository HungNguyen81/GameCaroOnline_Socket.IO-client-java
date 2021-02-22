package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginStage {
    @FXML TextField txtUsername;
    @FXML PasswordField txtPassword;
    @FXML Button btnLogin;
    @FXML Label lbForgotPwd;

    public void Login() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        System.out.println(username + ", " + password);
        String url = Main_login.hostUrl + "login";
        String res = new ConnectServer().createConnect(url, username, password);
        if(res.contains("Login OK")){
            try {
                String[] str = res.split(",");
                Main_login.user = username;
                Main_login.email = str[2];
                LocalCookieControll.setCookie(username, password, str[1]);
            } catch (Exception ex){
                ex.printStackTrace();
            }

            System.out.println("Goto Home");
            Main_login.gotoHomeStage();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Tên đăng nhập hoặc mật khẩu không đúng !",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void signup() {
        try {
            Main_login.gotoSignupStage();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void forgotPasswd(){

    }

    public void close(MouseEvent e) {
        ((Label)e.getSource()).getScene().getWindow().hide();
    }

    public void minimize(MouseEvent e){
        ((Stage)((Label)e.getSource()).getScene().getWindow()).setIconified(true);
    }
}

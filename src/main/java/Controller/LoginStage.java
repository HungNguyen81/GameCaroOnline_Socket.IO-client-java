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
    @FXML CheckBox cbRememberLogin;

    public void Login() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        System.out.println(username + ", " + password);
        String url = Main_login.hostUrl + "login";
        String res = new ConnectServer().createConnect(url, username, password);
        if(res.contains("Login OK")){
            try {
                String[] str = res.split(",");
                String avt_id = str[1];
                String email = (str.length < 3)? "no-email":str[2];
                Main_login.user = username;
                Main_login.avt = Integer.parseInt(avt_id);
                Main_login.email = email;
                if(cbRememberLogin.isSelected())
                    LocalCookieController.setCookie(username, password, str[1]);
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
        if(!txtUsername.getText().equals("") || !txtPassword.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn muốn đăng ký tài khoản mới?",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() != ButtonType.YES) return;
        }
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

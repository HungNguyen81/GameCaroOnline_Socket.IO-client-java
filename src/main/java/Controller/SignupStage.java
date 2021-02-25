package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.*;
import java.util.Random;

public class SignupStage {
    @FXML TextField txtUsername;
    @FXML PasswordField txtPassword;
    @FXML PasswordField txtPasswordConfirm;
    @FXML TextField txtEmail;
    @FXML Button btnLogin;
    @FXML Button btnSignup;
    @FXML ImageView imgAdd;
    @FXML ImageView btnNext;
    @FXML ImageView btnPrev;

    public static int num = 6;
    final int MAX_NUM = 50;
    final String imgPrefix = "";

    public void init(){
        Random rand = new Random();
        num = rand.nextInt(50) + 1;
        setimg(num);
    }

    public void signup() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String cpassword = txtPasswordConfirm.getText();
        String email = txtEmail.getText();

        if(username.equals("") || password.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa nhập Tài khoản hoặc mật khẩu", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(!password.equals(cpassword)){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Mật khẩu nhập lại không khớp.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String url = Main_login.hostUrl + "signup";
        System.out.println("sign up : " + username + ", " + password);
        String res = new ConnectServer().createConnect(url, username, password, email, num);
        if(res.equals("Signup OK")){
            try {
                LocalCookieController.setCookie(username, password, num+"");
            } catch (IOException err){
                err.printStackTrace();
            }
            Main_login.user = username;
            Main_login.email = email;
            Main_login.gotoHomeStage();
        } else if(res.equals("400code:username")){
            new Alert(Alert.AlertType.WARNING,
                    "Tài khoản đã tồn tại\nVui lòng sử dụng tài khoản khác",
                    ButtonType.OK).showAndWait();
        }
    }

    private void setimg(int num){
        String imgName = imgPrefix + ((num < 10)? "0"+num:num) + ".png";

        File f = new File("src/main/resources/img/avatar/" + imgName);
        Image i = new Image(f.toURI().toString());

        imgAdd.setImage(i);
    }
    public void next(){
        num = (num < MAX_NUM)? num + 1: 1;
        setimg(num);
    }
    public void prev(){
        num = (num > 1)? num - 1: MAX_NUM;
        setimg(num);
    }

    public void login(){
        try {
            Main_login.gotoLoginStage();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize(MouseEvent e){
        ((Stage)((Label)e.getSource()).getScene().getWindow()).setIconified(true);
    }
}

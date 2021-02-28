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
    final String USERNAME_PATTERN = "([a-zA-Z.@#0-9_-]+)$";//"^(?=.*[A-Za-z@$!%*#?&0-9])[A-Za-z0-9@$!%*#?&]{1,20}$";
    final String EMAIL_PATTERN = "^([a-zA-Z.0-9]+)@([a-z.0-9]+.+[a-z])$";

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

        Alert alert;
        if(username.equals("") || password.equals("")){
            alert = new Alert(Alert.AlertType.WARNING,
                    "Bạn chưa nhập Tài khoản hoặc mật khẩu",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(!password.equals(cpassword)){
            alert = new Alert(Alert.AlertType.WARNING,
                    "Mật khẩu nhập lại không khớp.",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(!username.matches(USERNAME_PATTERN)){
            alert = new Alert(Alert.AlertType.INFORMATION,
                    """
                            Tên đăng nhập không hợp lệ!\s
                            Chỉ được chứa các kí tự a-z, A-Z, 0-9
                            và một vài kí tự đặc biệt như -, _, ., @, # """,
                    ButtonType.OK);
            alert.showAndWait();
            txtUsername.setText("");
            return;
        } else if(password.length() < 6){
            alert = new Alert(Alert.AlertType.INFORMATION,
                    "Mật khẩu phải dài ít nhất 6 kí tự!",
                    ButtonType.OK);
            alert.showAndWait();
            txtPassword.setText("");
            txtPasswordConfirm.setText("");
            return;
        }
        else if(!email.matches(EMAIL_PATTERN)){
            alert = new Alert(Alert.AlertType.INFORMATION,
                    """
                            Email không hợp lệ!\s
                            Vui lòng sử dụng một email hợp lệ. """,
                    ButtonType.OK);
            alert.showAndWait();
            txtEmail.setText("");
            return;
        }

        String url = Main_login.hostUrl + "signup";
        System.out.println("sign up : " + username + ", " + password);
        String res = new ConnectServer().createConnect(url, username, password, email, num);
        if(res.equals("Signup OK")){
            Main_login.user = username;
            Main_login.email = email;
            Main_login.avt = num;
            new Alert(Alert.AlertType.INFORMATION,
                    "Đăng ký thành công! " +
                            "\n- Tài khoản: " + username +
                            "\n- Email: " + email,
                    ButtonType.OK).showAndWait();
            Main_login.gotoHomeStage();
        } else if(res.equals("400code:username")){
            new Alert(Alert.AlertType.WARNING,
                    "Tài khoản đã tồn tại\n"
                            + "Vui lòng sử dụng tài khoản khác",
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

package Controller;

import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class GameLobby {
    @FXML Button btnCreate;
    @FXML Button btnJoin;
    @FXML TextField txtRoomID;
    @FXML Label lbRoomID;
    @FXML Button btnSend;
    @FXML TextField txtMessageInput;
    @FXML TextArea txtChat;
    @FXML Button btnBack;
    @FXML ImageView imgPlayerOne;
    @FXML ImageView imgPlayerTwo;
    @FXML Label lblPlayerOneName;
    @FXML Label lblPlayerTwoName;
    @FXML Label lblUsername;

    private static Socket socket;
    private static StringBuilder chat;
    public static String roomID;
    private static boolean isConnect = false;

    private void initSocket(){
        IO.Options options = IO.Options.builder().build();
        socket = IO.socket(URI.create(Main_login.hostUrl), options);
    }

    public void initGameLobbyComponent(){
        lblUsername.setText(Main_login.user);
        int avt_id = Main_login.avt;
        imgPlayerOne.setImage(
                new Image("img/avatar/"
                        + ((avt_id < 10)? "0" + avt_id : "" + avt_id) + ".png"));
    }

    public void CreateRoom(){
        Main_login.userNumber = 1;
        Random rand = new Random();

        // create room id with 6 random number letters
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 6; i++){
            sb.append(rand.nextInt(10));
        }
        roomID = sb.toString();
        lbRoomID.setText(roomID);
        chat = new StringBuilder();
        System.out.println(roomID);

        if(isConnect){
            socket.disconnect();
        }
        initSocket();
        socket.connect();
        pressEnterToSendMsg();
        isConnect = true;

        System.out.println("create room " + roomID);
        socket.emit("createRoom",
                roomID, Main_login.user, Main_login.avt);

        btnSend.setDisable(false);
        setMessageListener(socket);
        socket.on("roomFull", args -> {
            String username = args[0].toString();
            int avt = Integer.parseInt(args[1].toString());

            imgPlayerTwo.setImage(
                    new Image("img/avatar/"
                            + ((avt < 10)? "0" + avt : "" + avt) + ".png"));
            lblPlayerTwoName.setText(username);
        });
    }

    public void JoinRoom(){
        Main_login.userNumber = 2;
        roomID = txtRoomID.getText();
        lbRoomID.setText(roomID);
        System.out.println("join room " + roomID);

        if(isConnect){
            socket.disconnect();
        }
        initSocket();
        socket.connect();
        pressEnterToSendMsg();
        isConnect = true;
        chat = new StringBuilder();

        socket.emit("joinRoom",
                roomID, Main_login.user, Main_login.avt);
        socket.on("confirmJoin", args -> {
            String res = args[0].toString();
            int avt_id = Integer.parseInt(args[1].toString());

            imgPlayerTwo.setImage(
                    new Image("img/avatar/"
                            + ((avt_id < 10)? "0" + avt_id : "" + avt_id) + ".png"));
            if(res.equals("1")){
                System.out.println("ok");
                btnSend.setDisable(false);
            } else {
                System.out.println("failed");
            }
        });
        setMessageListener(socket);
    }

    public void sendMsg(){
        String msg = txtMessageInput.getText();
        txtMessageInput.setText("");
        if(!msg.equals("")){
            makeChatLine(Main_login.user, msg);
            socket.emit("sendMsg", roomID, Main_login.userNumber, msg);
        }
    }
    private void setMessageListener(Socket socket){
        socket.on("newMsg", args -> {
            String username = args[0].toString();
            String replymsg = args[1].toString();

            System.out.println(username);
            makeChatLine(username, replymsg);
        });
    }

    public void pressEnterToSendMsg(){
        txtMessageInput.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                sendMsg();
            }
        });
    }

    private void makeChatLine(String username, String msg){
        chat.append(username)
                .append(": - ")
                .append(msg)
                .append("\n");
        txtChat.setText(chat.toString());
    }

    public void backToHomeStage() throws IOException {
        if(isConnect){
            socket.disconnect();
            isConnect = false;
        }
        Main_login.gotoHomeStage();
    }
}

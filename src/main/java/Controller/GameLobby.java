package Controller;

import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class GameLobby {
    @FXML Button btnCreate;
    @FXML Button btnJoin;
    @FXML TextField txtRoomID;
    @FXML Label lbRoomID;
    @FXML Button btnSend;
    @FXML TextArea txtMessageInput;
    @FXML TextArea txtChat;
    @FXML Label lblBack;

    Socket socket;
    String url;
    String chat;
    private static boolean isJoined = false;

    public static String roomID;
    private static boolean isConnect = false;

    public void CreateRoom(){
        Random rand = new Random();

        // create room id with 6 random number letters
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 6; i++){
            sb.append(rand.nextInt(10));
        }
        roomID = sb.toString();
        lbRoomID.setText(roomID);
        txtChat.setText(chat);
        System.out.println(roomID);

        IO.Options options = IO.Options.builder().build();
        if(isConnect) {
            socket.disconnect();
            isConnect = false;
        }
        socket = IO.socket(URI.create(Main_login.hostUrl), options);
        if(!isConnect) {
            socket.connect();
            isConnect = true;
        }
        txtChat.setText(chat);
        System.out.println("create room " + roomID);
        socket.emit("createRoom", roomID, Main_login.user, Main_login.avt);

        btnSend.setVisible(true);
    }

    public void JoinRoom(){
        roomID = txtRoomID.getText();

        System.out.println("join room " + roomID);
        IO.Options options = IO.Options.builder().build();
        socket = IO.socket(URI.create(Main_login.hostUrl), options);
        socket.connect();
        txtChat.setText(chat);

        socket.emit("joinRoom", roomID, Main_login.user, Main_login.avt);
        socket.on("confirmJoin", args -> {
            Alert alert;
            String res = args[0].toString();
            if(res.equals("1")){
                System.out.println("ok");
                isJoined = true;
                btnSend.setVisible(true);

            } else {
                isJoined = false;
            }
        });
    }

    public void sendMsg(){
        IO.Options options = IO.Options.builder().build();
        socket = IO.socket(URI.create(url), options);
        socket.connect();

        String msg = txtMessageInput.getText();
        chat += msg+"\n";
        txtChat.setText(chat);
        socket.emit("sendMsg", msg);
        socket.on("newMsg", args -> {
        });
    }

    public void backToHomeStage() throws IOException {
        Main_login.gotoHomeStage();
    }
}

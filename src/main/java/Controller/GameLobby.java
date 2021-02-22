package Controller;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URI;
import java.util.Arrays;
import java.util.Random;

public class GameLobby {
    @FXML Button btnCreate;
    @FXML Button btnJoin;
    @FXML TextField txtRoomID;
    @FXML Label lbRoomID;
    @FXML Button btnSend;
    @FXML TextArea txtMessageInput;
    @FXML TextArea txtChat;

    Socket socket;
    String url;
    String doiThu;
    String chat = "--- CHAT ---\n";

    public static String roomID;

    public void CreateRoom(){
        Random rand = new Random();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 6; i++){
            sb.append(rand.nextInt(10));
        }
        roomID = sb.toString();
        lbRoomID.setText(roomID);
        txtChat.setText(chat);
        System.out.println(roomID);

        IO.Options options = IO.Options.builder().build();
        socket = IO.socket(URI.create(Main_login.hostUrl), options);
        socket.connect();
        txtChat.setText(chat);
        System.out.println("create room " + roomID);

        socket.emit("createRoom", roomID, Main_login.user);


    }

    public void JoinRoom(){
        roomID = txtRoomID.getText();

        System.out.println("join room " + roomID);
        IO.Options options = IO.Options.builder().build();
        socket = IO.socket(URI.create(url), options);
        socket.connect();
        txtChat.setText(chat);

        socket.emit("join", roomID);
        socket.on("join-reply", new Emitter.Listener() {
            @Override
            public void call(Object... args){
                doiThu = args[0].toString();
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
        socket.on("replyMsg", args -> {

        });
    }
    public String makeMsgJson(String id, String msg, String from, String to){
        return "{"
                + "\"id\":\"" + roomID + "\","
                + "\"msg\":\"" + msg + "\","
                + "\"from\":\"" + from + "\","
                + "\"to\":\"" + to + "\""
                + "}";
    }
}

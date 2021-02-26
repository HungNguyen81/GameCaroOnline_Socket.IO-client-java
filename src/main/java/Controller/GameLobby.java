package Controller;

import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.application.Platform;
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

    private void setupImageViewImg(ImageView imgV, int avt_id){
        imgV.setImage(
                new Image("img/avatar/"
                        + ((avt_id < 10)? "0" + avt_id : "" + avt_id) + ".png"));
    }

    public void initGameLobbyComponent(){
        lblUsername.setText(Main_login.user);
        int avt_id = Main_login.avt;
        setupImageViewImg(imgPlayerOne, avt_id);
    }

    public void CreateRoom(){
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
        setRoomFullListener(socket);
    }

    public void JoinRoom(){
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
        btnSend.setDisable(false);
        setRoomFullListener(socket);
        setMessageListener(socket);
    }

    private void setupP2info(String username, int avt_id){
        setupImageViewImg(imgPlayerTwo, avt_id);
        lblPlayerTwoName.setText(username);
    }

    public void sendMsg(){
        String msg = txtMessageInput.getText();
        txtMessageInput.setText("");
        if(!msg.equals("")){
            makeChatLine(Main_login.user, msg);
            socket.emit("sendMsg", roomID, Main_login.user, msg);
        }
    }

    private void setRoomFullListener(Socket socket){
        socket.on("roomFull", args -> {
            String username = args[0].toString();
            try {
                int avt_id = Integer.parseInt(args[1].toString());
                Platform.runLater(() -> setupP2info(username, avt_id));
            } catch (Exception e){
                e.printStackTrace();
            }
        });
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

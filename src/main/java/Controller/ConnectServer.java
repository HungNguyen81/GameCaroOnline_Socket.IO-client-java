package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ConnectServer {
    private URL url;
    private HttpURLConnection con;

    // connect for login action
    public String createConnect(String _url,
                                String username,
                                String password) throws IOException {
        url = new URL(_url);
        con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);

        Map<String,String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);
        return conn(arguments, con);
    }

    // connect for sign in action
    public String createConnect(String _url,
                                String username,
                                String password,
                                String email,
                                int avtNum) throws IOException {
        url = new URL(_url);
        con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);

        Map<String,String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);
        arguments.put("email", email);
        arguments.put("avt", avtNum + "");
        return conn(arguments, con);
    }

    public String conn(Map<String, String> arguments, HttpURLConnection con) throws IOException {
        StringJoiner sj = new StringJoiner("&");
        for(Map.Entry<String,String> entry : arguments.entrySet())
            sj.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "="
                    + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        con.setFixedLengthStreamingMode(length);
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.connect();
        try(OutputStream os = con.getOutputStream()) {
            os.write(out);
        }

        //wait for response
        StringBuilder response = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        } catch (Exception e){
            System.out.println("login/signin error");
        }
        return response.toString();
    }
}

package Controller;

import java.io.*;

public class LocalCookieController {
    public static final int COOKIE_USER_NAME = 0;
    public static final int COOKIE_PASSWORD = 1;
    public static final int COOKIE_AVATAR = 2;

    private static String cookie = "";
    private static final String filename = "/Model/myLocalCookie.csv";

    public static void setCookie(String _user, String _pass, String _avt) throws IOException {
        FileWriter fw = new FileWriter(Main_login.dir + filename);
        cookie = _user + "," + _pass + "," + _avt;
        fw.write(cookie);
        fw.close();
    }
    public static void setCookie() throws IOException {
        FileWriter fw = new FileWriter(Main_login.dir + filename);
        fw.write("");
        fw.close();
    }

    public static String getCookie() throws IOException {
        BufferedReader br =
                new BufferedReader(new FileReader
                        (Main_login.dir + filename));
        cookie = br.readLine();
        return cookie;
    }

    public static String getUser() throws IOException {
        if(cookie.equals("")) cookie = getCookie();
        return cookie.split(",")[COOKIE_USER_NAME];
    }

    public static String getAvt() throws IOException {
        if(cookie.equals("")) cookie = getCookie();
        int num = Integer.parseInt(cookie.split(",")[COOKIE_AVATAR]);
        return (num < 10)? "0" + num: "" + num;
    }
}

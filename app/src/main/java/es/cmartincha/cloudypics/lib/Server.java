package es.cmartincha.cloudypics.lib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Carlos on 06/06/2015.
 */
public class Server {
    protected static final String SERVER_URL = "http://www.albertoymaribel.es/api";
    protected static final String SERVER_LOGIN_URL = SERVER_URL + "/login";
    protected static final int READ_TIMEOUT_MS = 5000;
    protected static final int CONNECT_TIMEOUT_MS = 5000;

    public static LoginResponse login(String username, String password, String key) throws Exception {
        String postParameters = "username=" + username + "&password=" + password + "&key=" + key;
        URL url = new URL(SERVER_LOGIN_URL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.print(postParameters);
        out.close();

        int statusCode = connection.getResponseCode();

        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new Exception();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String input;
        StringBuilder inputBuilder = new StringBuilder();

        while ((input = reader.readLine()) != null) {
            inputBuilder.append(input);
        }

        reader.close();

        return new LoginResponse(inputBuilder.toString());
    }
}

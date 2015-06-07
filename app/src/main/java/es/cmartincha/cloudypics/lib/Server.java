package es.cmartincha.cloudypics.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    protected static final String SERVER_PICTURE_COLLECTION_URL = SERVER_URL + "/eventCollection";

    protected static final int READ_TIMEOUT_MS = 5000;
    protected static final int CONNECT_TIMEOUT_MS = 5000;

    public static Login login(String username, String password, String key) throws Exception {
        String postParameters = "username=" + username + "&password=" + password + "&key=" + key;
        HttpURLConnection connection = setUpConnection(SERVER_LOGIN_URL, "POST", postParameters);
        int statusCode = connection.getResponseCode();

        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new Exception();
        }

        String response = readResponse(connection);

        return new Login(response);
    }

    public static PictureCollection getPictures(int index) throws Exception {
        HttpURLConnection connection = setUpConnection(SERVER_PICTURE_COLLECTION_URL, "GET", "index=" + index);
        int statusCode = connection.getResponseCode();

        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new Exception();
        }

        String response = readResponse(connection);

        return new PictureCollection(response);
    }

    public static Bitmap getPictureBitmap(URL imageUrl) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setDoInput(true);

        int statusCode = connection.getResponseCode();

        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new Exception();
        }

        InputStream inputStream = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        inputStream.close();
        connection.disconnect();

        return bitmap;
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String input;
        StringBuilder inputBuilder = new StringBuilder();

        while ((input = reader.readLine()) != null) {
            inputBuilder.append(input);
        }

        reader.close();
        connection.disconnect();

        return inputBuilder.toString();
    }

    private static HttpURLConnection setUpConnection(String urlString, String requestMethod, String requestParameters) throws IOException {
        URL url;

        if (requestMethod.equals("GET") && requestParameters != null) {
            url = new URL(urlString + "?" + requestParameters);
        } else {
            url = new URL(urlString);
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setRequestMethod(requestMethod);
        connection.setDoInput(true);

        if (requestMethod.equals("POST") && requestParameters != null) {
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());

            out.print(requestParameters);
            out.close();
        }

        return connection;
    }
}

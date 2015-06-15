package es.cmartincha.cloudypics.lib;

import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    protected static final String SERVER_PICTURE_UPLOAD_URL = SERVER_URL + "/uploadMedia";

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

    public static File getPicture(URL imageUrl, File directory) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setDoInput(true);

        int statusCode = connection.getResponseCode();

        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new Exception();
        }

        File picture = writeFilePicture(imageUrl, directory, connection.getInputStream());

        return picture;
    }

    private static File writeFilePicture(URL imageUrl, File directory, InputStream in) throws IOException {
        File picture = new File(directory, Uri.parse(imageUrl.toString()).getLastPathSegment());
        FileOutputStream out = new FileOutputStream(picture);
        byte[] buffer = new byte[4 * 1024];
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

        out.flush();
        out.close();

        in.close();

        return picture;
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

    private static HttpURLConnection setUpUploadConnection(String urlString, File picture, String token) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(urlString).openConnection());
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.setRequestProperty("token", token);
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=1234abcd");

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());

        request.writeBytes("--1234abcd\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"type\"\r\n");
        request.writeBytes("\r\n");
        request.writeBytes("jpg");
        request.writeBytes("\r\n");

        request.writeBytes("--1234abcd\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"filename\"\r\n");
        request.writeBytes("\r\n");
        request.writeBytes(picture.getName());
        request.writeBytes("\r\n");

        request.writeBytes("--1234abcd\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + picture.getName() + "\"\r\n");
        request.writeBytes("Content-Type: image/jpg\r\n");
        request.writeBytes("\r\n");

        byte[] bytes = readFileBytes(picture);

        request.write(bytes);
        request.writeBytes("\r\n");

        request.writeBytes("--1234abcd--\r\n");

        request.flush();
        request.close();

        return connection;
    }

    private static byte[] readFileBytes(File picture) throws IOException {
        byte[] bytes = new byte[(int) picture.length()];
        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(picture));

        buf.read(bytes, 0, bytes.length);
        buf.close();

        return bytes;
    }

    public static void uploadPicture(File picture, String token) throws Exception {
        HttpURLConnection connection = setUpUploadConnection(SERVER_PICTURE_UPLOAD_URL, picture, token);
        int statusCode = connection.getResponseCode();

        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new Exception();
        }

        JSONObject response = new JSONObject(readResponse(connection));

        if (!response.getBoolean("success")) {
            throw new Exception();
        }
    }
}

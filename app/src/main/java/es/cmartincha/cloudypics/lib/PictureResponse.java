package es.cmartincha.cloudypics.lib;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Carlos on 07/06/2015.
 */
public class PictureResponse extends JSONObject {
    public PictureResponse(String json) throws JSONException {
        super(json);
    }

    public String getType() {
        try {
            return getString("type");
        } catch (JSONException ignored) {
        }

        return null;
    }

    public String getUsername() {
        try {
            return getString("username");
        } catch (JSONException ignored) {
        }

        return null;
    }

    public Date getDate() {
        return null;
    }

    public URL getUrl() {
        try {
            return new URL(getString("url"));
        } catch (MalformedURLException | JSONException ignored) {
        }

        return null;
    }

    public URL getThumbnail() {
        try {
            return new URL(getString("thumbnail"));
        } catch (MalformedURLException | JSONException ignored) {
        }

        return null;
    }
}

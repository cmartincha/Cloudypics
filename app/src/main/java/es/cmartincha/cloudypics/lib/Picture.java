package es.cmartincha.cloudypics.lib;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Carlos on 07/06/2015.
 */
public class Picture {
    String mUsername = "";
    Date mDate = null;
    URL mUrl = null;
    URL mThumbnail = null;

    public static Picture fromJSON(String json) {
        Picture picture = new Picture();

        try {
            JSONObject jsonObject = new JSONObject(json);

            picture.mUsername = jsonObject.getString("username");
            picture.mDate = null;
            picture.mUrl = new URL(jsonObject.getString("url"));
            picture.mThumbnail = new URL(jsonObject.getString("thumbnail"));
        } catch (JSONException | MalformedURLException ignored) {
        }

        return picture;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public URL getUrl() {
        return mUrl;
    }

    public void setUrl(URL mUrl) {
        this.mUrl = mUrl;
    }

    public URL getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(URL mThumnail) {
        this.mThumbnail = mThumnail;
    }

    public Picture() {
    }
}

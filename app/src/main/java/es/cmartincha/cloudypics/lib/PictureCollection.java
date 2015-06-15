package es.cmartincha.cloudypics.lib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Carlos on 07/06/2015.
 */
public class PictureCollection {
    ArrayList<Picture> mPicturesArray = new ArrayList<>();
    boolean mSuccess = false;

    public static PictureCollection fromJSON(String json) {
        PictureCollection pictureCollection = new PictureCollection();

        try {
            JSONObject jsonObject = new JSONObject(json);
            pictureCollection.mSuccess = jsonObject.getBoolean("success");

            JSONArray pictures = jsonObject.getJSONArray("event");
            int length = pictures.length();

            for (int index = 0; index < length; index++) {
                pictureCollection.mPicturesArray.add(Picture.fromJSON(pictures.getString(index)));
            }
        } catch (JSONException ignored) {
        }

        return pictureCollection;
    }

    public PictureCollection() {
    }

    public boolean isOk() {
        return mSuccess;
    }

    public ArrayList<Picture> getPicturesArray() {
        return mPicturesArray;
    }

    public void setPicturesArray(ArrayList<Picture> mPicturesArray) {
        this.mPicturesArray = mPicturesArray;
    }
}

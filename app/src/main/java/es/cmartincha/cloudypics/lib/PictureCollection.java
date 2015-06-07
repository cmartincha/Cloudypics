package es.cmartincha.cloudypics.lib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Carlos on 07/06/2015.
 */
public class PictureCollection extends JSONObject {
    public PictureCollection(String json) throws JSONException {
        super(json);
    }

    public boolean isOk() {
        try {
            return getBoolean("success");
        } catch (JSONException e) {
        }

        return false;
    }

    public ArrayList<Picture> getPicturesArray() {
        try {
            JSONArray pictures = getJSONArray("event");
            int length = pictures.length();
            ArrayList<Picture> picturesArray = new ArrayList<>();

            for (int index = 0; index < length; index++) {
                picturesArray.add(new Picture(pictures.getString(index)));
            }

            return picturesArray;
        } catch (JSONException ignored) {
        }

        return null;
    }
}

package es.cmartincha.cloudypics.lib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Carlos on 07/06/2015.
 */
public class PictureCollectionResponse extends JSONObject {
    public PictureCollectionResponse(String json) throws JSONException {
        super(json);
    }

    public boolean isOk() {
        try {
            return getBoolean("success");
        } catch (JSONException e) {
        }

        return false;
    }

    public ArrayList<PictureResponse> getPicturesArray() {
        try {
            JSONArray pictures = getJSONArray("event");
            int length = pictures.length();
            ArrayList<PictureResponse> picturesArray = new ArrayList<>();

            for (int index = 0; index < length; index++) {
                picturesArray.add(new PictureResponse(pictures.getString(index)));
            }

            return picturesArray;
        } catch (JSONException ignored) {
        }

        return null;
    }
}

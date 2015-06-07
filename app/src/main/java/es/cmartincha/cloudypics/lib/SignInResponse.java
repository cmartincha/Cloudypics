package es.cmartincha.cloudypics.lib;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlos on 07/06/2015.
 */
public class SignInResponse extends JSONObject {
    public SignInResponse(String json) throws JSONException {
        super(json);
    }

    public boolean isOk() {
        try {
            return getBoolean("success");
        } catch (JSONException e) {
        }

        return false;
    }

    public String getToken() {
        try {
            return getString("token");
        } catch (JSONException e) {
        }

        return null;
    }
}

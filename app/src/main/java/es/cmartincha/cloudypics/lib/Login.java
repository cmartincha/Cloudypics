package es.cmartincha.cloudypics.lib;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlos on 07/06/2015.
 */
public class Login extends JSONObject {
    public Login(String json) throws JSONException {
        super(json);
    }

    public boolean isOk() {
        try {
            return getBoolean("success");
        } catch (JSONException ignored) {
        }

        return false;
    }

    public String getToken() {
        try {
            return getString("token");
        } catch (JSONException ignored) {
        }

        return null;
    }
}

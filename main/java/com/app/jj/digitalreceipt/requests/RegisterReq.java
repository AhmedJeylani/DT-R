package com.app.jj.digitalreceipt.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jey on 07/06/2016.
 */

public class RegisterReq extends StringRequest {
    private static final String registerServerURL = "http://digitalreceipt.esy.es/Register.php";
    private Map<String, String> serverData;

    public RegisterReq(String username, String password, String city, Response.Listener<String> listener) {
        super(Method.POST, registerServerURL, listener, null);
        /*
        these basically post the values onto the sql table in the server
        using the URL of my server
         */

        serverData = new HashMap<>();
        serverData.put("username", username);
        serverData.put("password", password);
        serverData.put("city",city);
    }

    @Override
    public Map<String, String> getParams() {
        return serverData;
    }
}

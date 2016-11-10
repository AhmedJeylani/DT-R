package com.app.jj.digitalreceipt.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jey on 07/06/2016.
 */

public class LoginReq extends StringRequest {
    private static final String loginServerURL = "http://digitalreceipt.esy.es/Login.php";
    private Map<String, String> serverData;

    public LoginReq(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, loginServerURL, listener, null);
        /*
        these basically post the values onto the sql table in the server
        using the URL of my server
         */

        serverData = new HashMap<>();
        serverData.put("username", username);
        serverData.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return serverData;
    }
}

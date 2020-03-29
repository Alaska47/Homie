package com.example.homie.utils;


import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anees on 11/21/2017.
 */

public class BackendUtils {
    public static String TAG = "BackendUtils";

    private static String IP = "https://homiest.herokuapp.com/";

    private static String result = "";

    public static void doGetRequest(String address, Map<String, String> parameters, final VolleyCallback callback, final Context context, final Activity activity) {

        Map<String, String> params = parameters;

        String request = IP + address + "?";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            request += entry.getKey() + "=" + entry.getValue() + "&";
        }
        request = request.substring(0, request.length()-1);

        Log.d(TAG, request);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "got a response in return");
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = "";
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }

                        Log.e(TAG, body + "\n");
                        callback.onError(error);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }



        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(context)
                .getRequestQueue().add(stringRequest);
    }

    public static void doCustomGetRequest(String address, Map<String, String> parameters, final VolleyCallback callback, final Context context, final Activity activity) {
        String request = address + "?";
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            request += entry.getKey() + "=" + entry.getValue() + "&";
        }

        Log.d("location", request);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "received callback");
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = "";
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }

                        Log.e(TAG, body + "\n");
                        callback.onError(error);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }



        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(context)
                .getRequestQueue().add(stringRequest);
    }

    public static void doPostRequest(String address, final Map<String, String> parameters, final VolleyCallback callback, final Context context, final Activity activity) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                IP + address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                String body = "";
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }

                Log.e(TAG, body + "\n");
                callback.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                String hello = "";
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    hello += entry.getKey() + "=" + entry.getValue() + "&";
                }
                Log.d(TAG, "original parameter: " +  hello);
                Map<String, String> params = parameters;

                return params;
            }

            /*@Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }*/



        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(context)
                .getRequestQueue().add(stringRequest);
    }
}
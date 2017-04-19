package com.vondear.tools.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/19.
 */

public interface OnRxHttp {
    void onSuccess(JSONObject jsonObject) throws JSONException;

    void onError(Call call, Response response, Exception e);
}

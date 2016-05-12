package com.github.rain.net.converters;

import com.android.volley.VolleyError;
import com.github.rain.net.RestLog;
import com.github.rain.net.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * data format is {"resultCode":0,"data":"","message":"","extras":""}
 *
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 13:44
 */
public class DefaultResponseConverter extends BaseResponseConverter {
    private static final String KEY_RESULT_CODE = "result_code";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATA = "data";
    private static final String KEY_EXTRAS = "extras";

    @Override
    public Result convertToResult(Type type, String data) throws VolleyError {
        Result result = new Result();
        try {
            JSONObject jsonObject = new JSONObject(data);

            if(jsonObject.has(KEY_RESULT_CODE)){
                result.setCode(jsonObject.getString(KEY_RESULT_CODE));
            }


            if(jsonObject.has(KEY_MESSAGE)){
                result.setMessage(jsonObject.getString(KEY_MESSAGE));
            }

            if(jsonObject.has(KEY_DATA)){
                result.setData(parseString(type, jsonObject.getString(KEY_DATA)));
            }

            if(jsonObject.has(KEY_EXTRAS)){
                result.setExtras(jsonObject.getString(KEY_EXTRAS));
            }

        } catch (JSONException e) {

            RestLog restLog = new RestLog().Line("parse error")
                    .Line("error message:%s", e.toString())
                    .Line("data:%s", data);

            restLog.d();

            throw new VolleyError(restLog.toString());
        }


        return result;
    }

    @Override
    public VolleyError convertError(VolleyError error) {
        return error;
    }
}

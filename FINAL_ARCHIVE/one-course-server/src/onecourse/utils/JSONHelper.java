package onecourse.utils;

import onecourse.customenmu.BaseStatus;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONHelper {
    /**
     * 将单个jsonObject用jsonArray包装起来
     */
    public static JSONArray getJSONArray(JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        return jsonArray;
    }

    public static JSONArray getJSONArray(JSONObject... jsonObjects) {
        JSONArray jsonArray = new JSONArray();
        for (JSONObject obj: jsonObjects)
            jsonArray.put(obj);
        return jsonArray;
    }

    /**
     * 将一对key, value包装为jsonObject，再将其用jsonArray包装起来
     */
    public static JSONArray getJSONArray(String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return JSONHelper.getJSONArray(jsonObject);
    }

    public static JSONObject getSingleJSONObject(JSONObject jsonObject) {
        return jsonObject.getJSONArray("content").getJSONObject(0);
    }

    public static JSONArray getStatusContent(BaseStatus status) {
        return status.getJsonObject().getJSONArray("content");
    }
}

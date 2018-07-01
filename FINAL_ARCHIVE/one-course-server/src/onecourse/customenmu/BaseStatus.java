package onecourse.customenmu;

import org.json.*;

public class BaseStatus {
    private int code;
    private String msg;
    private JSONArray content;
    private JSONObject json = new JSONObject();


    public BaseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.content = null;
        initJson();
    }

    public BaseStatus(int code, String msg, JSONArray content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
        initJson();
    }

    public BaseStatus(BaseStatus status, JSONArray content) {
        this.code = status.code;
        this.msg = status.msg;
        this.content = content;
        initJson();
    }


    public JSONObject getJsonObject() {
        return this.json;
    }

    public String toJsonString() {
        return this.json.toString();
    }

    public boolean equals(BaseStatus s) {
        if (this == s)  return true;
        return this.code == s.code;
    }

    private void initJson() {
        json.put("code", code);
        json.put("msg", msg);
        if (content != null)
            json.put("content", content);
    }

}

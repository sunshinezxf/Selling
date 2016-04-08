package selling.sunshine.utils;

/**
 * Created by sunshine on 4/8/16.
 */
public class ResultData {
    private ResponseCode responseCode;
    private Object data;
    private String description;

    public ResultData() {
        this.responseCode = ResponseCode.RESPONSE_OK;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

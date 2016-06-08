package selling.sunshine.pagination;

import java.util.Map;

/**
 * Created by sunshine on 5/6/16.
 */
public class MobilePageParam {
    private int start;
    private int length;
    private Map params;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public int getPageNum() {
        int num = 0;
        if (getStart() != 0) {
            num = getStart() / getLength();
            return num;
        }
        return num;
    }
}

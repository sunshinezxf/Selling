package selling.sunshine.pagination;

import java.util.ArrayList;

import java.util.List;

/**
 * Created by sunshine on 4/20/16.
 */
public class DataTablePage<T> {
    /*total records, before filtering*/
    private int iTotalRecords;

    /*total records, after filtering*/
    private int iTotalDisplayRecords;

    /*an unaltered copy of sEcho sent from the client side*/
    private String sEcho;

    private List<T> data;

    public DataTablePage() {
        data = new ArrayList<T>();
    }

    public DataTablePage(DataTableParam param) {
        this();
        this.sEcho = param.getsEcho();
    }

    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

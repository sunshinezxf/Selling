package selling.sunshine.pagination;

/**
 * Created by sunshine on 4/20/16.
 */
public class DataTableParam {
    /*display start point in the current data set*/
    private int iDisplayStart;

    /*number of records that the table can display in the current draw*/
    private int iDisplayLength;

    /*global search field*/
    private String sSearch;

    /*information for DataTables to use for rendering*/
    private String sEcho;

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public String getsSearch() {
        return sSearch;
    }

    public void setsSearch(String sSearch) {
        this.sSearch = sSearch;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public int getPageNum() {
        int num = 0;
        if (getiDisplayStart() != 0) {
            num = getiDisplayStart() / getiDisplayLength();
            return num;
        }
        return num;
    }
}

/**
 * Created by sunshine on 2016/12/9.
 */
function not_null(item) {
    if (item == null || item == "" || item.length <= 0) {
        return false;
    }
    return true;
}

import java.util.HashMap;

/**
 * Created by weng on 2017/4/5.
 */
public class QueryTFEntity {
    private HashMap<String, Float> queryTF;

    public QueryTFEntity() {
        queryTF = new HashMap<>();
    }

    public HashMap<String, Float> getQueryTF() {
        return queryTF;
    }

    public void setQueryTF(String term, Float TFValue) {
        queryTF.put(term, TFValue);
    }
}


import java.util.HashMap;

/**
 * Created by weng on 2017/4/5.
 */
public class QueryTFIDFEntity {
    private HashMap<String, HashMap<String, Float>> queryTFIDF;

    public QueryTFIDFEntity() {
        queryTFIDF = new HashMap<>();
    }

    public HashMap<String, HashMap<String, Float>> getQueryTFIDF() {
        return queryTFIDF;
    }

    public void setQueryTFIDF(String queryID, HashMap<String, Float> termToDocTFIDF) {
        queryTFIDF.put(queryID, termToDocTFIDF);
    }
}


import java.util.HashMap;

/**
 * Created by weng on 2017/4/6.
 */
public class QueryAfterRocciTFIDFEntity {
    private HashMap<String, HashMap<String, Float>> queryAfRocciTFIDF;

    public QueryAfterRocciTFIDFEntity() {
        queryAfRocciTFIDF = new HashMap<>();
    }

    public HashMap<String, HashMap<String, Float>> getQueryAfRocciTFIDF() {
        return queryAfRocciTFIDF;
    }

    public void setQueryAfRocciTFIDF(String queryID, HashMap<String, Float> termToDocTFIDF) {
        queryAfRocciTFIDF.put(queryID, termToDocTFIDF);
    }
}

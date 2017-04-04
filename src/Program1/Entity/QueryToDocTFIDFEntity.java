package Program1.Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by weng on 2017/4/5.
 */
public class QueryToDocTFIDFEntity {
    HashMap<String, HashMap<String, ArrayList<Float>>> queryToDocTFIDF;

    public QueryToDocTFIDFEntity() {
        queryToDocTFIDF = new HashMap<>();
    }

    public HashMap<String, HashMap<String, ArrayList<Float>>> getQueryToDocTFIDF() {
        return queryToDocTFIDF;
    }

    public void setQueryToDocTFIDF(String queryID, HashMap<String, ArrayList<Float>> termToDocTFIDF) {
        queryToDocTFIDF.put(queryID, termToDocTFIDF);
    }
}


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by weng on 2017/4/5.
 */
public class DocTFIDFEntity {
    private HashMap<String, HashMap<String, ArrayList<Float>>> docTFIDF;

    public DocTFIDFEntity() {
        docTFIDF = new HashMap<>();
    }

    public HashMap<String, HashMap<String, ArrayList<Float>>> getDocTFIDF() {
        return docTFIDF;
    }

    public void setDocTFIDF(String queryID, HashMap<String, ArrayList<Float>> termToDocTFIDF) {
        docTFIDF.put(queryID, termToDocTFIDF);
    }
}

package Program1.Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by weng on 2017/4/5.
 */
public class DocTFIDFEntity {
    HashMap<String, ArrayList<Float>> docTFIDF;

    public DocTFIDFEntity() {
        docTFIDF = new HashMap<>();
    }

    public HashMap<String, ArrayList<Float>> getDocTFIDF() {
        return docTFIDF;
    }

    public void setDocTFIDF(String term, ArrayList<Float> docToTFIDFValue) {
        docTFIDF.put(term, docToTFIDFValue);
    }
}

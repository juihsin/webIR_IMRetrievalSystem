
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by weng on 2017/4/4.
 */
public class DocTFEntity {
    private HashMap<String, ArrayList<Float>> docTF;

    public DocTFEntity() {
        docTF = new HashMap<>();
    }

    public HashMap<String, ArrayList<Float>> getDocTF() {
        return docTF;
    }

    public void setDocTF(String term, ArrayList<Float> docToTFValue) {
        docTF.put(term, docToTFValue);
    }
}

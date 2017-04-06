
import java.util.HashMap;

/**
 * Created by weng on 2017/4/5.
 */
public class IDFEntity {
    HashMap<String, Float> IDF;

    public IDFEntity() {
        IDF = new HashMap<>();
    }

    public HashMap<String, Float> getIDF() {
        return IDF;
    }

    public void setIDF(String term, Float IDFValue) {
        IDF.put(term, IDFValue);
    }
}

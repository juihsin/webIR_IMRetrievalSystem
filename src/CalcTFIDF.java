
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by weng on 2017/4/4.
 */
public class CalcTFIDF {
    public CalcTFIDF() {
    }

    public HashMap<String, ArrayList<Float>> calcDocTFIDF(DocTFEntity docTFEntity, IDFEntity idfEntity) {
        HashMap<String, ArrayList<Float>> docTFIDF = new HashMap<>();
        for (String word : docTFEntity.getDocTF().keySet()) {
            ArrayList<Float> docToTFIDFValue = new ArrayList<>();
            float idf = idfEntity.getIDF().get(word);
            for (float tf : docTFEntity.getDocTF().get(word)) {
                float tfidf = tf * idf;
                tfidf = decimalFormat(tfidf); // 小數點後第15位
                docToTFIDFValue.add(tfidf);
            }
            docTFIDF.put(word, docToTFIDFValue);
        }
        return docTFIDF;
    }

    public float calcQueryTFDenominator(QueryEntity queryEntity) {
        return (float) queryEntity.getTermsNum();
    }

    public float calcQueryTFNumerator(HashMap<String, Integer> queryToFre, String queryID) {
        return (float) queryToFre.get(queryID);
    }

    public float calcDocTFDenominator(DocumentEntity documentEntity) {
        return (float) documentEntity.getTermsNum();
    }

    public float calcDocTFNumerator(HashMap<String, Integer> docToFre, String docID) {
        return (float) docToFre.get(docID);
    }

    public float calcIDFNumerator(List<DocumentEntity> documents) {
        return (float) documents.size();
    }

    public float calcIDFDenominator(String word, HashMap<String, HashMap<String, Integer>> termFrequency) {
        float IDFDenominator; // IDF分母：有出現term的DOC數
        if (!termFrequency.keySet().contains(word)) { // 所有DOC裡沒有這個word
            IDFDenominator = 1;
        } else { // DOC有這個word
            IDFDenominator = 1 + (termFrequency.get(word).keySet().size());
        }

        return IDFDenominator;
    }

    public static float decimalFormat(float value) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(15); // 小數點後第15位
        value = Float.parseFloat(df.format(value));
        return value;
    }
}

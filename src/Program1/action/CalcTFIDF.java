package Program1.action;

import Program1.Entity.DocumentEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by weng on 2017/4/4.
 */
public class CalcTFIDF {
    public CalcTFIDF() {
    }

    public float calcTFDenominator(DocumentEntity documentEntity) {
        float TFDenominator = documentEntity.getTermsNum(); // TF分母：DOCi的詞總數
        return TFDenominator;
    }

    public float calcTFNumerator(HashMap<String, Integer> docToFre, String docID) {
        float TFNumerator = docToFre.get(docID); // TF分子：一個term在DOCi裡面的出現次數
        return TFNumerator;
    }

    public float calcIDFNumerator(List<DocumentEntity> documents) {
        float IDFNumerator = documents.size(); // IDF分子：DOC總數
        return IDFNumerator;
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
}

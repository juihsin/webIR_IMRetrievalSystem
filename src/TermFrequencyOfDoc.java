
import java.util.HashMap;
import java.util.List;

/**
 * Created by weng on 2017/4/4.
 */
public class TermFrequencyOfDoc {
    public TermFrequencyOfDoc() {
    }

    public void calcTermFreEachDoc(List<DocumentEntity> documents, DocTermFrequencyEntity docTermFrequencyEntity) {
        for (DocumentEntity documentEntity : documents) {
            String docID = documentEntity.getId();
            String docSegContent = documentEntity.getContentAfterSeg();
            String[] words = docSegContent.split("\\|");
            for (String word : words) {
                HashMap<String, Integer> docIDToTermFre = new HashMap<>(); // (檔案ID,詞頻)
                HashMap<String, HashMap<String, Integer>> termFrequency = docTermFrequencyEntity.getTermFrequency();

                if (!termFrequency.keySet().contains(word)) {
                    docIDToTermFre.put(docID, 1);
                } else {
                    docIDToTermFre = termFrequency.get(word);
                    if (docIDToTermFre.keySet().contains(docID)) {
                        docIDToTermFre.put(docID, docIDToTermFre.get(docID) + 1);
                    } else {
                        docIDToTermFre.put(docID, 1);
                    }
                }
                docTermFrequencyEntity.setTermFrequency(word, docIDToTermFre); // 哪個字
            }
        }
    }

    public void calcTotalTermEachDoc(List<DocumentEntity> documents) {
        for (DocumentEntity documentEntity : documents) {
            String docSegContent = documentEntity.getContentAfterSeg();
            String[] words = docSegContent.split("\\|");
            documentEntity.setTermsNum(words.length); // 檔案的總詞頻
        }
    }
}

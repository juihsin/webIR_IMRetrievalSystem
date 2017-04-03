package Program1;

import Program1.Entity.DocumentEntity;
import Program1.Entity.QueryEntity;
import Program1.action.CalcTFIDF;
import Program1.action.ChineseMMSeg;
import Program1.action.XMLParser;

import java.util.*;

/**
 * Created by weng on 2017/4/3.
 */
public class IMRetrievalSystem {
    public static void main(String argv[]) {
        // Parse document
        XMLParser xMLParser = new XMLParser();
        List<String> filePaths = xMLParser.listFilesForFolder( // TODO 檔案改回原本的！
                "/Users/weng/Desktop/program1/CIRB010-briefToTest");
        System.out.println(filePaths.size());
        List<DocumentEntity> documents = xMLParser.parserDocFile(filePaths);
        System.out.println(documents.size());

        // Parse train query
        // TODO 檔案改回原本的！
        String trainFile = "/Users/weng/Desktop/program1/queries/query-train-briefToTest.xml";
        List<QueryEntity> trainQuerys = xMLParser.parserQueryFile(trainFile);

        // Segment document
        ChineseMMSeg chineseMMSeg = new ChineseMMSeg();
        chineseMMSeg.segDocuments(documents);

        // 儲存詞出現在哪個檔案，以及出現的頻率
        for (DocumentEntity documentEntity : documents) {
            String docID = documentEntity.getId();
            String docSegContent = documentEntity.getContentAfterSeg();
            String[] words = docSegContent.split("\\|");
            for (String word : words) {
                HashMap<String, Integer> docIDToTermFre; // (檔案ID,詞頻)
                HashMap<String, HashMap<String, Integer>> termFrequency = documentEntity.getTermFrequency();

                if (termFrequency.keySet().contains(word)) {
                    if (termFrequency.get(word).keySet().contains(docID)) {
                        documentEntity.setDocIDToTermFre(docID, termFrequency.get(word).get(docID) + 1);
                    }
                } else {
                    documentEntity.setDocIDToTermFre(docID, 1);
                }
                documentEntity.setTermFrequency(word, documentEntity.getDocIDToTermFre()); // 哪個字
                System.out.println(word); // TODO Test
                System.out.println(documentEntity.getTermFrequency().get(word)); // TODO Test
                System.out.println(documentEntity.getTermFrequency().get(word).get(docID)); // TODO Test}
            }
        }

        // 儲存每個檔案的總詞數
        for (DocumentEntity documentEntity : documents) {
            String docSegContent = documentEntity.getContentAfterSeg();
            String[] words = docSegContent.split("\\|");
            documentEntity.setTermsNum(words.length); // 檔案的總詞頻
        }

        // Segment train query
        chineseMMSeg.segAllQueries(trainQuerys);

        // TODO calc TF-IDF
        CalcTFIDF calcTFIDF = new CalcTFIDF();

        // TODO Evaluation

        // TODO Result
        // Parse test query
        // TODO 檔案改回原本的！
        String testFile = "/Users/weng/Desktop/program1/queries/query-test-briefToTest.xml";
        List<QueryEntity> testQuerys = xMLParser.parserQueryFile(testFile);

        // Segment test query
        chineseMMSeg.segAllQueries(testQuerys);

        // TODO Rocchio Feedback

    }
}

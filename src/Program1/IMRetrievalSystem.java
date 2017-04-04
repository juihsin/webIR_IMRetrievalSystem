package Program1;

import Program1.Entity.DocumentEntity;
import Program1.Entity.QueryEntity;
import Program1.action.*;

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
        String trainFile = "/Users/weng/Desktop/program1/queries/query-train.xml";
        List<QueryEntity> trainQuerys = xMLParser.parserQueryFile(trainFile);

        // Segment document
        ChineseMMSeg chineseMMSeg = new ChineseMMSeg();
        chineseMMSeg.segDocuments(documents);

        // Calc doc term frequency
        TermFrequencyOfDoc termFrequencyOfDoc = new TermFrequencyOfDoc();
        termFrequencyOfDoc.calcTermFreEachDoc(documents); // 儲存詞出現在哪個檔案，以及出現的頻率
        termFrequencyOfDoc.calcTotalTermEachDoc(documents); // 儲存每個檔案的總詞數

        // Segment train query
        chineseMMSeg.segAllQueries(trainQuerys);

        // Calc query term frequency
        TermFrequencyOfQuery termFrequencyOfQuery = new TermFrequencyOfQuery();
        termFrequencyOfQuery.calcTermFreEachQuery(trainQuerys); // 儲存詞出現在哪個query，以及出現的頻率
        termFrequencyOfQuery.calcTotalTermEachQuery(trainQuerys); // 儲存每個檔案的總詞數

        // TODO calcTermFreEachQuery TF-IDF
        CalcTFIDF calcTFIDF = new CalcTFIDF();
        int TFNumerator = ; // 分子
        int TFDenominator = ; // 分母

        // TODO Evaluation

        // TODO Result
        // Parse test query
        String testFile = "/Users/weng/Desktop/program1/queries/query-test.xml";
        List<QueryEntity> testQuerys = xMLParser.parserQueryFile(testFile);

        // Segment test query
        chineseMMSeg.segAllQueries(testQuerys);

        // TODO Rocchio Feedback

    }
}

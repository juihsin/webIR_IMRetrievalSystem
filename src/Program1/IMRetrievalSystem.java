package Program1;

import Program1.Entity.*;
import Program1.action.*;

import java.text.DecimalFormat;
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

        // Segment document
        ChineseMMSeg chineseMMSeg = new ChineseMMSeg();
        chineseMMSeg.segDocuments(documents);

        // Calc doc term frequency
        TermFrequencyOfDoc termFrequencyOfDoc = new TermFrequencyOfDoc();
        DocTermFrequencyEntity docTermFrequencyEntity = new DocTermFrequencyEntity();
        termFrequencyOfDoc.calcTermFreEachDoc(documents, docTermFrequencyEntity); // 儲存詞出現在哪個檔案，以及出現的頻率
        termFrequencyOfDoc.calcTotalTermEachDoc(documents); // 儲存每個檔案的總詞數

        // Parse train query
        String trainFile = "/Users/weng/Desktop/program1/queries/query-train.xml";
        List<QueryEntity> trainQuerys = xMLParser.parserQueryFile(trainFile);

        // Segment train query
        chineseMMSeg.segAllQueries(trainQuerys);

        // Calc query term frequency
        TermFrequencyOfQuery termFrequencyOfQuery = new TermFrequencyOfQuery();
        QueryTermFrequencyEntity queryTermFrequencyEntity = new QueryTermFrequencyEntity();
        termFrequencyOfQuery.calcTermFreEachQuery(trainQuerys, queryTermFrequencyEntity); // 儲存詞出現在哪個query，以及出現的頻率
        termFrequencyOfQuery.calcTotalTermEachQuery(trainQuerys); // 儲存每個檔案的總詞數

        // TODO Calc doc TF-IDF
        CalcTFIDF calcTFIDF = new CalcTFIDF();
        QueryToDocTFIDFEntity queryToDocTFIDFEntity = new QueryToDocTFIDFEntity();
        for (QueryEntity queryEntity : trainQuerys) { // Choose a query
            String queryContent = queryEntity.getContentAfterSeg();
            String[] words = queryContent.split("\\|");

            DocTFEntity docTFEntity = new DocTFEntity();
            IDFEntity idfEntity = new IDFEntity();
            for (String word : words) { // words of query

                // Calc TF value
                ArrayList<Float> docToTFValue = new ArrayList<>();
                HashMap<String, HashMap<String, Integer>> termFrequency = docTermFrequencyEntity.getTermFrequency();

                if (!termFrequency.keySet().contains(word)) { // 所有DOC裡沒有這個word
                    for (DocumentEntity documentEntity : documents) {
                        docToTFValue.add(0f);
                    }
                } else { // DOC有這個word
                    for (DocumentEntity documentEntity : documents) {
                        String docID = documentEntity.getId();
                        HashMap<String, Integer> docToFre = docTermFrequencyEntity.getTermFrequency().get(word);
                        if (!docToFre.keySet().contains(docID)) { // word沒有出現在這個ID的DOC
                            docToTFValue.add(0f);
                        } else { // word有出現在這個ID的DOC
                            float TFNumerator = calcTFIDF.calcTFNumerator(docToFre, docID); // TF分子：一個term在DOCi裡面的出現次數
                            float TFDenominator = calcTFIDF.calcTFDenominator(documentEntity); // TF分母：DOCi的詞總數
                            float TFValue = TFNumerator / TFDenominator;
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(10); // 小數點後第10位
                            TFValue = Float.parseFloat(df.format(TFValue));
                            docToTFValue.add(TFValue);
                        }
                    }
                }
                docTFEntity.setDocTF(word, docToTFValue);

                // Calc IDF Value
                float IDFDenominator = calcTFIDF.calcIDFDenominator(word, termFrequency); // IDF分母：有出現term的DOC數
                float IDFNumerator = calcTFIDF.calcIDFNumerator(documents); // IDF分子：DOC總數
                float IDFValue = (float) Math.log(IDFNumerator / IDFDenominator);
                idfEntity.setIDF(word, IDFValue);
            }

            // TF*IDF
            DocTFIDFEntity docTFIDFEntity = new DocTFIDFEntity();
            for (String word : docTFEntity.getDocTF().keySet()) {
                ArrayList<Float> docToTFIDFValue = new ArrayList<>();
                for (float tf : docTFEntity.getDocTF().get(word)) {
                    float idf = idfEntity.getIDF().get(word);
                    float tfidf = tf * idf;
                    docToTFIDFValue.add(tfidf);
                }
                docTFIDFEntity.setDocTFIDF(word, docToTFIDFValue);
            }
            String queryID = queryEntity.getNumber();
            queryToDocTFIDFEntity.setQueryToDocTFIDF(queryID, docTFIDFEntity.getDocTFIDF());
        }

        // TODO Calc query TF-IDF


        // TODO Evaluation

        // TODO Result
//        // Parse test query
//        String testFile = "/Users/weng/Desktop/program1/queries/query-test.xml";
//        List<QueryEntity> testQuerys = xMLParser.parserQueryFile(testFile);
//
//        // Segment test query
//        chineseMMSeg.segAllQueries(testQuerys);

        // TODO Rocchio Feedback

    }
}

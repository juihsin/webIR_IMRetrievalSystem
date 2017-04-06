
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by weng on 2017/4/3.
 */
public class IMRetrievalSystem {
    public static void main(String argv[]) {
        // Parse document
        XMLParser xMLParser = new XMLParser();
        List<String> filePaths = xMLParser.listFilesForFolder("../data/CIRB010");
        List<DocumentEntity> documents = xMLParser.parserDocFile(filePaths);

        // Segment document
        ChineseMMSeg chineseMMSeg = new ChineseMMSeg();
        chineseMMSeg.segDocuments(documents);

        // Calc doc term frequency
        TermFrequencyOfDoc termFrequencyOfDoc = new TermFrequencyOfDoc();
        DocTermFrequencyEntity docTermFrequencyEntity = new DocTermFrequencyEntity();
        termFrequencyOfDoc.calcTermFreEachDoc(documents, docTermFrequencyEntity); // 儲存詞出現在哪個檔案，以及出現的頻率
        termFrequencyOfDoc.calcTotalTermEachDoc(documents); // 儲存每個檔案的總詞數
        System.out.println("store doc TermFre finish!"); // TODO Test

        // Parse query
        String queryFile = "../data/queries/query-test.xml";
        List<QueryEntity> querys = xMLParser.parserQueryFile(queryFile);

        // Segment query
        chineseMMSeg.segAllQueries(querys);

        // Calc query term frequency
        TermFrequencyOfQuery termFrequencyOfQuery = new TermFrequencyOfQuery();
        QueryTermFrequencyEntity queryTermFrequencyEntity = new QueryTermFrequencyEntity();
        termFrequencyOfQuery.calcTermFreEachQuery(querys, queryTermFrequencyEntity); // 儲存詞出現在哪個query，以及出現的頻率
        termFrequencyOfQuery.calcTotalTermEachQuery(querys); // 儲存每個檔案的總詞數
        System.out.println("store query TermFre finish!"); // TODO Test

        // Calc TFIDF
        CalcTFIDF calcTFIDF = new CalcTFIDF();
        DocTFIDFEntity docTFIDFEntity = new DocTFIDFEntity();
        QueryTFIDFEntity queryTFIDFEntity = new QueryTFIDFEntity();
        for (QueryEntity queryEntity : querys) { // Choose a query
            System.out.println("deal TFIDF in Query " + queryEntity.getNumber()); // TODO Test
            String queryContent = queryEntity.getContentAfterSeg();
            String[] words = queryContent.split("\\|");
            String queryID = queryEntity.getNumber();

            DocTFEntity docTFEntity = new DocTFEntity();
            IDFEntity idfEntity = new IDFEntity();
            HashMap<String, HashMap<String, Integer>> docTermFrequency = docTermFrequencyEntity.getTermFrequency();
            QueryTFEntity queryTFEntity = new QueryTFEntity();


            for (String word : words) { // words of query
                System.out.println("word: " + word); // TODO Test
                // Calc doc TF-IDF
                // Calc doc TF value
                ArrayList<Float> docToTFValue = new ArrayList<>();
                if (!docTermFrequency.keySet().contains(word)) { // 所有DOC裡沒有這個word
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
                            float TFNumerator = calcTFIDF.calcDocTFNumerator(docToFre, docID); // TF分子：一個term在DOCi裡面的出現次數
                            float TFDenominator = calcTFIDF.calcDocTFDenominator(documentEntity); // TF分母：DOCi的詞總數
                            float TFValue = TFNumerator / TFDenominator;
                            TFValue = decimalFormat(TFValue); // 小數點後第15位
                            docToTFValue.add(TFValue);
                        }
                    }
                }
                docTFEntity.setDocTF(word, docToTFValue);

                // Calc doc IDF Value
                float IDFDenominator = calcTFIDF.calcIDFDenominator(word, docTermFrequency); // IDF分母：有出現term的DOC數
                float IDFNumerator = calcTFIDF.calcIDFNumerator(documents); // IDF分子：DOC總數
                float IDFValue = (float) Math.log(IDFNumerator / IDFDenominator);
                IDFValue = decimalFormat(IDFValue); // 小數點後第15位
                idfEntity.setIDF(word, IDFValue);

                // Calc query TF-IDF
                // Calc query TF Vlaue
                HashMap<String, Integer> queryToFre = queryTermFrequencyEntity.getTermFrequency().get(word);
                float TFNumerator = calcTFIDF.calcQueryTFNumerator(queryToFre, queryID); // TF分子：一個term在QUERYi裡面的出現次數
                float TFDenominator = calcTFIDF.calcQueryTFDenominator(queryEntity); // TF分母：QUERYi的詞總數
                float TFValue = TFNumerator / TFDenominator;
                TFValue = decimalFormat(TFValue); // 小數點後第15位
                queryTFEntity.setQueryTF(word, TFValue);
            }

            // Doc TF*IDF
            HashMap<String, ArrayList<Float>> docTFIDF = calcTFIDF.calcDocTFIDF(docTFEntity, idfEntity);
            docTFIDFEntity.setDocTFIDF(queryID, docTFIDF);

            // Query TF*IDF
            HashMap<String, Float> queryTFIDF = new HashMap<>();
            for (String word : queryTFEntity.getQueryTF().keySet()) {
                float tf = queryTFEntity.getQueryTF().get(word);
                float idf = idfEntity.getIDF().get(word);
                float tfidf = tf * idf;
                tfidf = decimalFormat(tfidf); // 小數點後第15位
                queryTFIDF.put(word, tfidf);
            }
            queryTFIDFEntity.setQueryTFIDF(queryID, queryTFIDF);
        }

        // Cosine similarity
        CalcCosineSimilarity calcCosineSimilarity = new CalcCosineSimilarity();
        HashMap<String, LinkedHashMap<Integer, Float>> cosSimiQueryToDoc =
                calcCosineSimilarity.calc(documents, querys, docTFIDFEntity, queryTFIDFEntity);

        // Rocchio Feedback: 取前5個最相關的Doc (參數：a取1，b取0.8)
        QueryAfterRocciTFIDFEntity queryAfterRocciTFIDFEntity = new QueryAfterRocciTFIDFEntity();
        for (QueryEntity queryEntity : querys) {
            String queryContent = queryEntity.getContentAfterSeg();
            String[] words = queryContent.split("\\|");
            String queryID = queryEntity.getNumber();
            HashMap<String, Float> queryTFIDFMap = queryTFIDFEntity.getQueryTFIDF().get(queryID);
            HashMap<String, ArrayList<Float>> docTFIDFMap = docTFIDFEntity.getDocTFIDF().get(queryID);
            LinkedHashMap<Integer, Float> cosSimiToDoc = cosSimiQueryToDoc.get(queryID);
            HashMap<String, Float> queryAfRocciTFIDF = new HashMap<>();
            for (String word : words) {
                float queryTFIDF = queryTFIDFMap.get(word);
                int num = 0;
                float docTFIDFSum = 0;
                for (int docIndex : cosSimiToDoc.keySet()) {
                    float docTFIDF = docTFIDFMap.get(word).get(docIndex);
                    docTFIDFSum = docTFIDFSum + docTFIDF;
                    num++;
                    if (num == 5) {
                        break;
                    }
                }
                float newQueryTFIDF = queryTFIDF + (0.16f * docTFIDFSum);
                queryAfRocciTFIDF.put(word, newQueryTFIDF);
            }
            queryAfterRocciTFIDFEntity.setQueryAfRocciTFIDF(queryID, queryAfRocciTFIDF);
        }

        // Calc Cosine Similarity again
        calcCosineSimilarity = new CalcCosineSimilarity();
        cosSimiQueryToDoc = calcCosineSimilarity.calcAfterRocciho(documents, querys, docTFIDFEntity, queryAfterRocciTFIDFEntity);

        // TODO Result
        try {
            FileWriter fileWriter = new FileWriter("result.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String newLine = "\n";
            String space = " ";
            String comma = ",";
            bufferedWriter.write("query_id,retrieved_docs");
            bufferedWriter.write(newLine);
            bufferedWriter.flush();
            int queryNum = 1;
            for (QueryEntity queryEntity : querys) {
                String queryID = queryEntity.getNumber();
                String lastThreeQueryID = queryID.substring(queryID.length() - 3);
//                int qID = Integer.parseInt(lastThreeQueryID);
                bufferedWriter.write(lastThreeQueryID);
                bufferedWriter.write(comma);
                bufferedWriter.flush();
                LinkedHashMap<Integer, Float> cosSimiMap = cosSimiQueryToDoc.get(queryID);

                int docNum = 1;
                for (int docIndex : cosSimiMap.keySet()) {
                    String docID = documents.get(docIndex).getId();
                    if (docNum < cosSimiMap.keySet().size()) {
                        bufferedWriter.write(docID);
                        bufferedWriter.write(space);
                    } else {
                        bufferedWriter.write(docID);
                    }
                    bufferedWriter.flush();
                    docNum++;
                }

                if (queryNum < querys.size()) {
                    bufferedWriter.write(newLine);
                    bufferedWriter.flush();
                }
                queryNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float decimalFormat(float value) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(15); // 小數點後第15位
        value = Float.parseFloat(df.format(value));
        return value;
    }
}
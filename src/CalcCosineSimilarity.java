
import java.util.*;

/**
 * Created by weng on 2017/4/6.
 */
public class CalcCosineSimilarity {
    public CalcCosineSimilarity() {
    }

    public HashMap<String, LinkedHashMap<Integer, Float>> calc(List<DocumentEntity> documents, List<QueryEntity> querys,
                                                               DocTFIDFEntity docTFIDFEntity, QueryTFIDFEntity queryTFIDFEntity) {
        // Cosine Similarity(Query,Document1) = Dot product(Query, Document1) / ||Query|| * ||Document1||
        HashMap<String, LinkedHashMap<Integer, Float>> cosSimiQueryToDoc = new HashMap<>();
        for (QueryEntity queryEntity : querys) {
            System.out.println("deal cosSimi query: " + queryEntity.getNumber()); // TODO Test
            String queryID = queryEntity.getNumber();
            String queryContent = queryEntity.getContentAfterSeg();
            String[] words = queryContent.split("\\|");

            int docIndex = 0;
            HashMap<Integer, Float> cosSimiToDoc = new HashMap<>();
            for (DocumentEntity documentEntity : documents) {
                float dotQueryDocVector = 0f;
                float queryVectorSquare = 0f;
                float docVectorSquare = 0f;
                for (String word : words) {
                    float queryTFIDF = queryTFIDFEntity.getQueryTFIDF().get(queryID).get(word);
                    float docTFIDF = docTFIDFEntity.getDocTFIDF().get(queryID).get(word).get(docIndex);
                    dotQueryDocVector = dotQueryDocVector + (queryTFIDF * docTFIDF);
                    queryVectorSquare = queryVectorSquare + (queryTFIDF * queryTFIDF);
                    docVectorSquare = docVectorSquare + (docTFIDF * docTFIDF);
                }
                float queryVectorLength = (float) Math.sqrt(queryVectorSquare);
                float docVectorLength = (float) Math.sqrt(docVectorSquare);
                float cosineSimi;
                if ((queryVectorLength * docVectorLength) != 0f) {
                    cosineSimi = dotQueryDocVector / (queryVectorLength * docVectorLength);
                } else {
                    cosineSimi = 0;
                }
                cosSimiToDoc.put(docIndex, cosineSimi);
                docIndex++;
            }

            LinkedHashMap<Integer, Float> sortedCosSimiToDoc = sortMap(cosSimiToDoc);
            LinkedHashMap<Integer, Float> limitSizeSortedCosSimiToDoc = limitMapSize(sortedCosSimiToDoc);
            cosSimiQueryToDoc.put(queryID, limitSizeSortedCosSimiToDoc);
            System.out.println("sorted: " + cosSimiQueryToDoc.get(queryID)); // TODO Test
        }
        return cosSimiQueryToDoc;
    }

    public HashMap<String, LinkedHashMap<Integer, Float>> calcAfterRocciho(List<DocumentEntity> documents, List<QueryEntity> querys,
                                                                 DocTFIDFEntity docTFIDFEntity, QueryAfterRocciTFIDFEntity queryAfterRocciTFIDFEntity) {
        // Cosine Similarity(Query,Document1) = Dot product(Query, Document1) / ||Query|| * ||Document1||
        HashMap<String, LinkedHashMap<Integer, Float>> cosSimiQueryToDoc = new HashMap<>();
        for (QueryEntity queryEntity : querys) {
            System.out.println("deal cosSimi query: " + queryEntity.getNumber()); // TODO Test
            String queryID = queryEntity.getNumber();
            String queryContent = queryEntity.getContentAfterSeg();
            String[] words = queryContent.split("\\|");

            int docIndex = 0;
            HashMap<Integer, Float> cosSimiToDoc = new HashMap<>();
            for (DocumentEntity documentEntity : documents) {
                float dotQueryDocVector = 0f;
                float queryVectorSquare = 0f;
                float docVectorSquare = 0f;
                for (String word : words) {
                    float queryTFIDF = queryAfterRocciTFIDFEntity.getQueryAfRocciTFIDF().get(queryID).get(word);
                    float docTFIDF = docTFIDFEntity.getDocTFIDF().get(queryID).get(word).get(docIndex);
                    dotQueryDocVector = dotQueryDocVector + (queryTFIDF * docTFIDF);
                    queryVectorSquare = queryVectorSquare + (queryTFIDF * queryTFIDF);
                    docVectorSquare = docVectorSquare + (docTFIDF * docTFIDF);
                }
                float queryVectorLength = (float) Math.sqrt(queryVectorSquare);
                float docVectorLength = (float) Math.sqrt(docVectorSquare);
                float cosineSimi;
                if ((queryVectorLength * docVectorLength) != 0f) {
                    cosineSimi = dotQueryDocVector / (queryVectorLength * docVectorLength);
                } else {
                    cosineSimi = 0;
                }
                cosSimiToDoc.put(docIndex, cosineSimi);
                docIndex++;
            }

            LinkedHashMap<Integer, Float> sortedCosSimiToDoc = sortMap(cosSimiToDoc);
            LinkedHashMap<Integer, Float> limitSizeSortedCosSimiToDoc = limitMapSize(sortedCosSimiToDoc);
            cosSimiQueryToDoc.put(queryID, limitSizeSortedCosSimiToDoc);
            System.out.println("sorted: " + cosSimiQueryToDoc.get(queryID)); // TODO Test
        }
        return cosSimiQueryToDoc;
    }

    public LinkedHashMap<Integer, Float> limitMapSize(LinkedHashMap<Integer, Float> sortedCosSimiToDoc) {
        LinkedHashMap<Integer, Float> limitSizeMap = new LinkedHashMap<>();
        int i = 0;
        for (int docIndex : sortedCosSimiToDoc.keySet()) {
            float map = sortedCosSimiToDoc.get(docIndex);
            limitSizeMap.put(docIndex, map);
            i++;
            if (i == 100) {
                break;
            }
        }
        return limitSizeMap;
    }

    public LinkedHashMap<Integer, Float> sortMap(HashMap<Integer, Float> unSortedMap) {
        List<Map.Entry> list = new ArrayList<>(unSortedMap.entrySet());
        Comparator<Map.Entry> sortByValue = (e2, e1) -> ((Float) e1.getValue()).compareTo((Float) e2.getValue());
        list.sort(sortByValue);
        LinkedHashMap<Integer, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Float> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}

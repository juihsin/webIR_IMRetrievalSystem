package Program1.action;

import Program1.Entity.DocTermFrequencyEntity;
import Program1.Entity.DocumentEntity;
import Program1.Entity.QueryEntity;
import Program1.Entity.QueryTermFrequencyEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by weng on 2017/4/4.
 */
public class TermFrequencyOfQuery {
    public TermFrequencyOfQuery() {
    }

    public void calcTermFreEachQuery(List<QueryEntity> queries, QueryTermFrequencyEntity queryTermFrequencyEntity) {
        for (QueryEntity queryEntity : queries) {
            String queryID = queryEntity.getNumber();
            String querySegContent = queryEntity.getContentAfterSeg();
            String[] words = querySegContent.split("\\|");
            for (String word : words) {
                HashMap<String, Integer> queryIDToTermFre = new HashMap<>(); // (檔案ID,詞頻)
                HashMap<String, HashMap<String, Integer>> termFrequency = queryTermFrequencyEntity.getTermFrequency();

                if (!termFrequency.keySet().contains(word)) {
                    queryIDToTermFre.put(queryID, 1);
                } else {
                    queryIDToTermFre = termFrequency.get(word);
                    if (queryIDToTermFre.keySet().contains(queryID)) {
                        queryIDToTermFre.put(queryID, queryIDToTermFre.get(queryID) + 1);
                    } else {
                        queryIDToTermFre.put(queryID, 1);
                    }
                }
                queryTermFrequencyEntity.setTermFrequency(word, queryIDToTermFre); // 哪個字
            }
        }
    }

    public void calcTotalTermEachQuery(List<QueryEntity> queries) {
        for (QueryEntity queryEntity : queries) {
            String querySegContent = queryEntity.getContentAfterSeg();
            String[] words = querySegContent.split("\\|");
            queryEntity.setTermsNum(words.length); // 檔案的總詞頻
        }
    }
}

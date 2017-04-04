package Program1.Entity;

import java.util.HashMap;

/**
 * Created by weng on 2017/4/4.
 */
public class QueryTermFrequencyEntity {
    private HashMap<String, HashMap<String, Integer>> termFrequency;

    public QueryTermFrequencyEntity() {
        termFrequency = new HashMap<>();
    }

    public HashMap<String, HashMap<String, Integer>> getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(String term, HashMap<String, Integer> freMap) {
        termFrequency.put(term, freMap);
    }
}

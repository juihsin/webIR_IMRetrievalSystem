package Program1.Entity;

import java.util.HashMap;

public class DocumentEntity {
    private String id;
    private String date;
    private String title;
    private String text;
    private String contentAfterSeg;
    private int termsNum;
    private HashMap<String, Integer> docIDToTermFre; // (檔案ID,詞頻)
    private HashMap<String, HashMap<String, Integer>> termFrequency; // 哪個字

    public DocumentEntity() {
        id = "";
        date = "";
        title = "";
        text = "";
        contentAfterSeg = "";
        termsNum = 0;
        docIDToTermFre = new HashMap<>();
        termFrequency = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContentAfterSeg() {
        return contentAfterSeg;
    }

    public void setContentAfterSeg(String contentAfterSeg) {
        this.contentAfterSeg = contentAfterSeg;
    }

    public int getTermsNum() {
        return termsNum;
    }

    public void setTermsNum(int termsNum) {
        this.termsNum = termsNum;
    }

    public HashMap<String, Integer> getDocIDToTermFre() {
        return docIDToTermFre;
    }

    public void setDocIDToTermFre(String id, int fre) {
        docIDToTermFre.put(id, fre);
    }

    public HashMap<String, HashMap<String, Integer>> getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(String term, HashMap<String, Integer> freMap) {
        termFrequency.put(term, freMap);
    }
}

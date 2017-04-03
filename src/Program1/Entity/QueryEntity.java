package Program1.Entity;

/**
 * Created by weng on 2017/4/3.
 */
public class QueryEntity {
    private String number;
    private String title;
    private String question;
    private String narrative;
    private String concepts;
    private String contentAfterSeg;

    public QueryEntity() {
        number = "";
        title = "";
        question = "";
        narrative = "";
        concepts = "";
        contentAfterSeg = "";
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getConcepts() {
        return concepts;
    }

    public void setConcepts(String concepts) {
        this.concepts = concepts;
    }

    public String getContentAfterSeg() {
        return contentAfterSeg;
    }

    public void setContentAfterSeg(String contentAfterSeg) {
        this.contentAfterSeg = contentAfterSeg;
    }
}

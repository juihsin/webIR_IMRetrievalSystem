
import com.chenlb.mmseg4j.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Created by weng on 2017/4/3.
 */
public class ChineseMMSeg {
    private Dictionary dic;

    public ChineseMMSeg() {
        System.setProperty("mmseg.dic.path", "./dictionary");  //這裡可以指定自訂詞庫
        dic = Dictionary.getInstance();
    }

    public Seg getSeg() {
        return new ComplexSeg(dic);
    }

    public String segWords(String txt, String wordSpilt) throws IOException {
        Reader input = new StringReader(txt);
        StringBuilder sb = new StringBuilder();
        Seg seg = getSeg();
        MMSeg mmSeg = new MMSeg(input, seg);

        Word word = null;
        boolean first = true;
        while ((word = mmSeg.next()) != null) {
            if (!first) {
                sb.append(wordSpilt);
            }
            String w = word.getString();
            sb.append(w);
            first = false;
        }
        return sb.toString();
    }

    public void segDocuments(List<DocumentEntity> documents) {
        XMLParser xMLParser = new XMLParser();
        try {
            for (DocumentEntity documentEntity : documents) {
                String docContent = xMLParser.combineDocContent(documentEntity);
                String docAfterSeg = segWords(docContent, "|");
                documentEntity.setContentAfterSeg(docAfterSeg);
                System.out.println("segDoc: " + documentEntity.getId()); // TODO Test
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void segAllQueries(List<QueryEntity> querys) {
        XMLParser xMLParser = new XMLParser();
        try {
            for (QueryEntity queryEntity : querys) {
                String queryContent = queryEntity.getConcepts(); // TODO Test: just use concept
//                String queryContent = xMLParser.combineQueryContent(queryEntity);
                String queryAfterSeg = segWords(queryContent, "|");
                queryEntity.setContentAfterSeg(queryAfterSeg);
                System.out.println("segQuery: " + queryEntity.getNumber()); // TODO Test
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

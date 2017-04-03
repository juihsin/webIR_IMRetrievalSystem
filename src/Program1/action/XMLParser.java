package Program1.action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Program1.Entity.QueryEntity;
import kevin.zhang.NLPIR;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import Program1.Entity.DocumentEntity;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.title;

public class XMLParser {
    // Combine all text of a Document
    public String combineDocContent(DocumentEntity documentEntity) {
        String id = documentEntity.getId();
        String date = documentEntity.getDate();
        String title = documentEntity.getTitle();
        String text = documentEntity.getText();
        StringBuilder docContent = new StringBuilder();
        docContent = docContent.append(title).append(text);
        return docContent.toString();
    }

    // Combine all text of one query
    public String combineQueryContent(QueryEntity queryEntity) {
        String title = queryEntity.getTitle();
        String question = queryEntity.getQuestion();
        String narrative = queryEntity.getNarrative();
        String concepts = queryEntity.getConcepts();
        StringBuilder queryContent = new StringBuilder();
        queryContent = queryContent.append(title).append(question).append(narrative).append(concepts);
        return queryContent.toString();
    }

    // Read all data in a folder
    public List<String> listFilesForFolder(String path) {
        List<String> filePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(filePath -> Files.isRegularFile(filePath))
                    .filter(filePath -> !filePath.getFileName().toString().equals(".DS_Store"))
                    .forEach(filePath -> filePaths.add(filePath.toString()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return filePaths;
    }

    public List<DocumentEntity> parserDocFile(List<String> filePaths) {
        List<DocumentEntity> documents = new ArrayList<>();
        try {
            for (String filePath : filePaths) {
                System.out.print("read : " + filePath);
                File xmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document xmlDoc = dBuilder.parse(xmlFile);
                Node docNode = xmlDoc.getElementsByTagName("doc").item(0);

                if (docNode.getNodeType() == Node.ELEMENT_NODE) {
                    DocumentEntity documentEntity = new DocumentEntity();
                    Element contentElement = (Element) docNode;
                    documentEntity.setId(contentElement.getElementsByTagName("id").item(0).getTextContent());
                    documentEntity.setDate(contentElement.getElementsByTagName("date").item(0).getTextContent());
                    documentEntity.setTitle(contentElement.getElementsByTagName("title").item(0).getTextContent());
                    documentEntity.setText(contentElement.getElementsByTagName("text").item(0).getTextContent());
                    documents.add(documentEntity);
                }
                System.out.println(" finish!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }

    public List<QueryEntity> parserQueryFile(String fileName) {
        List<QueryEntity> querys = new ArrayList<>();
        try {
            File xmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDoc = dBuilder.parse(xmlFile);

            int topicNodeNumber = xmlDoc.getElementsByTagName("topic").getLength();
            for (int i = 0; i < topicNodeNumber; i++) {
                Node topicNode = xmlDoc.getElementsByTagName("topic").item(i);
                if (topicNode.getNodeType() == Node.ELEMENT_NODE) {
                    QueryEntity queryEntity = new QueryEntity();
                    Element contentElement = (Element) topicNode;
                    queryEntity.setNumber(contentElement.getElementsByTagName("number").item(0).getTextContent());
                    queryEntity.setTitle(contentElement.getElementsByTagName("title").item(0).getTextContent());
                    queryEntity.setQuestion(contentElement.getElementsByTagName("question").item(0).getTextContent());
                    queryEntity.setNarrative(contentElement.getElementsByTagName("narrative").item(0).getTextContent());
                    queryEntity.setConcepts(contentElement.getElementsByTagName("concepts").item(0).getTextContent());
                    querys.add(queryEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return querys;
    }
}

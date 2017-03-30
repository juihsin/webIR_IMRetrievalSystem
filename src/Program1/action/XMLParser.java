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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import Program1.Entity.DocumentEntity;

public class XMLParser {
	public static void main(String argv[]) {
		XMLParser xMLParser = new XMLParser();
		List<String> filePaths = xMLParser.listFilesForFolder(
				"/home/monster/Documents/Jim/Code/workspace/program1/program1/CIRB010");
		System.out.println(filePaths.size());
		List<DocumentEntity> documents = xMLParser.parserXMLFile(filePaths);
		System.out.println(documents.size());
	}

	// read all data in a folder
	public List<String> listFilesForFolder(String path) {
		List<String> filePaths = new ArrayList<>();
		try(Stream<Path> paths = Files.walk(Paths.get(path))) {
			paths.filter(filePath -> Files.isRegularFile(filePath))
				.forEach(filePath -> filePaths.add(filePath.toString()));
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		return filePaths;
	}

	public List<DocumentEntity> parserXMLFile(List<String> filePaths) {
		List<DocumentEntity> documents = new ArrayList<>();
		try {
			for(String filePath:filePaths){
				System.out.println("read : "+filePath);
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
				System.out.println("finish!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documents;
	}
}

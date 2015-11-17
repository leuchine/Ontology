package dbpedia;

//http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DBpediaOntologyXMLParser {

	public static void main(String argv[]) {

		try {
			// File xmlFile = new File("staff.xml");
			File xmlFile = new File("dbpedia_3.9.owl");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("owl:Class");
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"ontology.txt", false));
			System.out.println("-------------" + nList.getLength()
					+ "---------------");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());
				System.out.print(i + "\t");
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String cls1 = eElement.getAttribute("rdf:about")
							.replaceAll("http://dbpedia.org/ontology/", "");
					// System.out.println(eElement.getElementsByTagName("rdfs:label").item(0).getTextContent());
					Element elemt = (Element) eElement.getElementsByTagName(
							"rdfs:subClassOf").item(0);
					String cls2 = elemt.getAttribute("rdf:resource")
							.replaceAll("http://dbpedia.org/ontology/", "");
					// remove prefix of owl#Thing
					if (cls2.contains("http://www.w3.org/2002/07/"))
						cls2 = cls2.replace("http://www.w3.org/2002/07/", "");

					System.out.print(cls1 + "\t" + cls2);
					if (cls2.equals("owl#Thing"))
						System.out.print("  -----------");
					if (cls1.contains("/") || cls2.contains("/")) {
						System.out
								.println("+++++++++++++++++++++++++++++++++++++++++++\n\n\n");
						continue;
					}
					System.out.println();

					if (map.get(cls1) == null)
						map.put(cls1, 1);
					else {// check whether a class has multiple superclass
						map.put(cls1, map.get(cls1) + 1);
						System.out
								.println("+++++++++++++++++++++++++++++++++++++++++++\n\n\n\n\n");
					}

					writer.write(cls1 + "+" + cls2);
					writer.newLine();
					// System.out.println("Last Name : " +
					// eElement.getElementsByTagName("lastname").item(0).getTextContent());
					// System.out.println("Nick Name : " +
					// eElement.getElementsByTagName("nickname").item(0).getTextContent());
					// System.out.println("Salary : " +
					// eElement.getElementsByTagName("salary").item(0).getTextContent());
				}
				// break;
			}// for

			for (Entry<String, Integer> en : map.entrySet()) {
				if (en.getValue() > 1)
					System.out.println(en.getKey() + "  " + en.getValue());
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done!");
	}
}

package fr.fileshare.utilities;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * xml helper qui permet d'ecrire et lire dans un fichier xml etudiant
 */
public class XmlHelper {
    /**
     * Sauvegarder une liste de document dans un fichier xml
     *
     * @param documents list de documents
     */
    public void saveToXMLDocument(List<HashMap<String, String>> documents) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("documents");
            doc.appendChild(rootElement);

            /**
             * lire les documents pour y ajouter les nouveaux documents
             */
            List<HashMap<String, String>> listDocs = readXmlDocument();
            ecrireDansXml(rootElement,listDocs,doc);
            ecrireDansXml(rootElement,documents,doc);




            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            DOMImplementation domImpl = doc.getImplementation();
            DocumentType doctype = domImpl.createDocumentType("documents",
                    "documents",
                    "document.dtd");

            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source source = new DOMSource(doc);
            File xmlFile = new File(System.getProperty("user.dir") + "/src/main/java/ressources/document.xml");
            StreamResult file = new StreamResult(xmlFile);

            StreamResult console = new StreamResult(System.out);

            //write data
            transformer.transform(source, console);
            transformer.transform(source, file);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ecrireDansXml(Element rootElement , List<HashMap<String, String>> listDocs,Document doc){
        for (int i = 0; i < listDocs.size(); i++) {
            HashMap<String, String> d = new HashMap<>();
            Element document = doc.createElement("document");

            Element idDoc = doc.createElement("idDoc");
            Element emailAuteur = doc.createElement("emailAuteur");
            Element mdpAuteur = doc.createElement("mdpAuteur");
            Element datePub = doc.createElement("datePub");
            Element tags = doc.createElement("tags");
            Element text = doc.createElement("text");
            Element version = doc.createElement("version");
            Element status = doc.createElement("version");
            Element utilAutorises = doc.createElement("version");
            Element intitule = doc.createElement("intitule");
            Element description = doc.createElement("description");

            idDoc.setTextContent(d.get("idDoc"));
            emailAuteur.setTextContent(d.get("emailAuteur"));
            mdpAuteur.setTextContent(d.get("mdpAuteur"));
            datePub.setTextContent(d.get("datePub"));
            tags.setTextContent(d.get("tags"));
            text.setTextContent(d.get("text"));
            status.setTextContent(d.get("status"));
            utilAutorises.setTextContent(d.get("utilAutorises"));
            intitule.setTextContent(d.get("intitule"));
            description.setTextContent(d.get("description"));
            
            version.setTextContent(d.get("version"));
            document.appendChild(idDoc);
            document.appendChild(intitule);
            document.appendChild(emailAuteur);
            document.appendChild(mdpAuteur);
            document.appendChild(datePub);
            document.appendChild(tags);
            document.appendChild(text);
            document.appendChild(version);
            document.appendChild(status);
            document.appendChild(utilAutorises);
            document.appendChild(description);

            

            rootElement.appendChild(document);
        }
    }
    /**
     * Lire le fichier document.xml dans une List de HashMap chaque HashMap represente un document
     *
     * @return List de Hashmap
     */
    public List<HashMap<String, String>> readXmlDocument() {
        List<HashMap<String, String>> documents = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(System.getProperty("user.dir") + "/src/main/java/ressources/document.xml"));
            NodeList nodeList = doc.getElementsByTagName("documents").item(0).getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node documentNode = nodeList.item(i);
                if (documentNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList documentChilds = documentNode.getChildNodes();
                    HashMap<String, String> document = new HashMap<>();
                    for (int j = 0; j < documentChilds.getLength(); j++) {
                        Node documentElement = documentChilds.item(j);
                        if (documentElement.getNodeType() == Node.ELEMENT_NODE) {
                            document.put(documentElement.getNodeName(), documentElement.getTextContent().trim());
                        }

                    }
                    documents.add(document);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return documents;
    }
}

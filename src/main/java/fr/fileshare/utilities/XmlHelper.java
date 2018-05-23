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
            List<HashMap<String, String>> listDocs = readXmlDocument(documents.get(0).get("emailAuteur"), documents.get(0).get("mdpAuteur"));
            System.out.println("size: " + listDocs.size());
            for (int i = 0; i < listDocs.size(); i++) {
                System.out.println(listDocs.get(i));

            }
            if (listDocs.size() > 0) {
                ecrireDansXml(rootElement, listDocs, doc);
            }
            ecrireDansXml(rootElement, documents, doc);
            docToXml(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * transformer l'objet Document a un fichier xml
     *
     * @param doc Document
     * @throws TransformerException
     */
    public void docToXml(Document doc) throws TransformerException {
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
    }

    /**
     * creation de l'arborescence du fichier xml
     *
     * @param rootElement
     * @param listDocs
     * @param doc
     */
    public void ecrireDansXml(Element rootElement, List<HashMap<String, String>> listDocs, Document doc) {
        for (int i = 0; i < listDocs.size(); i++) {
            HashMap<String, String> d = listDocs.get(i);
            Element document = doc.createElement("document");

            Element idDoc = doc.createElement("idDoc");
            Element emailAuteur = doc.createElement("emailAuteur");
            Element mdpAuteur = doc.createElement("mdpAuteur");
            Element datePub = doc.createElement("datePub");
            Element tags = doc.createElement("tags");
            Element text = doc.createElement("text");
            Element version = doc.createElement("version");
            Element status = doc.createElement("status");
            Element utilAutorises = doc.createElement("utilAutorises");
            Element intitule = doc.createElement("intitule");
            Element description = doc.createElement("description");
            Element synchronise = doc.createElement("synchronise");
            Element idDocPublie = doc.createElement("idDocPublie");

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
            synchronise.setTextContent(d.get("synchronise"));
            idDocPublie.setTextContent(d.get("idDocPublie"));

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
            document.appendChild(synchronise);
            document.appendChild(idDocPublie);

            rootElement.appendChild(document);
        }
    }

    /**
     * Lire le fichier document.xml dans une List de HashMap chaque HashMap
     * represente un document
     *
     * @return List de Hashmap
     */
    public List<HashMap<String, String>> readXmlDocument(String email, String mdp) {
        List<HashMap<String, String>> documents = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(System.getProperty("user.dir") + "/src/main/java/ressources/document.xml"));
            NodeList nodeList = doc.getElementsByTagName("documents").item(0).getChildNodes();
            boolean emailCorrect = false, mdpCorrect = false;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node documentNode = nodeList.item(i);
                if (documentNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList documentChilds = documentNode.getChildNodes();
                    HashMap<String, String> document = new HashMap<>();
                    for (int j = 0; j < documentChilds.getLength(); j++) {
                        Node documentElement = documentChilds.item(j);
                        if (documentElement.getNodeType() == Node.ELEMENT_NODE) {

                            if (documentElement.getNodeName().equals("emailAuteur") && documentElement.getTextContent().equals(email)) {
                                emailCorrect = true;
                            } else if (documentElement.getNodeName().equals("mdpAuteur") && documentElement.getTextContent().equals(mdp)) {
                                mdpCorrect = true;
                            }

                            document.put(documentElement.getNodeName(), documentElement.getTextContent().trim());

                        }

                    }
                    if (emailCorrect && mdpCorrect) {
                        documents.add(document);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return documents;
    }
    /**
     * Modification d un document dans le fichier xml
     * @param nouveauDoc 
     */
    public void modifierDocumentXml(HashMap<String, String> nouveauDoc) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(System.getProperty("user.dir") + "/src/main/java/ressources/document.xml"));
            Document nouvDoc = documentBuilder.newDocument();
            NodeList nodeList = doc.getElementsByTagName("documents").item(0).getChildNodes();
            Element rootElement = nouvDoc.createElement("documents");
            nouvDoc.appendChild(rootElement);
            boolean docTrouver = false;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node documentNode = nodeList.item(i);
                if (documentNode.getNodeType() == Node.ELEMENT_NODE) {
                    docTrouver = false;
                    Element document = nouvDoc.createElement("document");
                    NodeList documentChilds = documentNode.getChildNodes();
                    for (int j = 0; j < documentChilds.getLength(); j++) {
                        Node documentElement = documentChilds.item(j);
                        if (documentElement.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = nouvDoc.createElement(documentElement.getNodeName());
                            System.out.println("oldDoc id: "+documentElement.getTextContent().trim());
                            System.out.println("nouveauDoc id: "+nouveauDoc.get("idDoc"));
                            if (documentElement.getNodeName().equals("idDoc") && documentElement.getTextContent().trim().equals(nouveauDoc.get("idDoc").trim())) {
                                docTrouver = true;
                            }
                            if(docTrouver){
                                element.setTextContent(nouveauDoc.get(documentElement.getNodeName()));
                            } else {
                                element.setTextContent(documentElement.getTextContent());
                            }
                            document.appendChild(element);
                        }

                    }

                    rootElement.appendChild(document);

                }

            }
            docToXml(nouvDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * supression d un document du fichier xml
     * @param idDoc 
     */
    public void supprimerDocumentXml(String idDoc) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(System.getProperty("user.dir") + "/src/main/java/ressources/document.xml"));
            Document nouvDoc = documentBuilder.newDocument();
            NodeList nodeList = doc.getElementsByTagName("documents").item(0).getChildNodes();
            Element rootElement = nouvDoc.createElement("documents");
            nouvDoc.appendChild(rootElement);
            boolean docTrouver = false;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node documentNode = nodeList.item(i);
                if (documentNode.getNodeType() == Node.ELEMENT_NODE) {
                    docTrouver = false;
                    Element document = nouvDoc.createElement("document");
                    NodeList documentChilds = documentNode.getChildNodes();
                    for (int j = 0; j < documentChilds.getLength(); j++) {
                        Node documentElement = documentChilds.item(j);
                        if (documentElement.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = nouvDoc.createElement(documentElement.getNodeName());
                            
                            if (documentElement.getNodeName().equals("idDoc") && documentElement.getTextContent().trim().equals(idDoc.trim())) {
                                docTrouver = true;
                            }
                            if(!docTrouver){
                                element.setTextContent(documentElement.getTextContent());
                                document.appendChild(element);
                            }
                            
                        }

                    }

                    rootElement.appendChild(document);

                }

            }
            docToXml(nouvDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
}

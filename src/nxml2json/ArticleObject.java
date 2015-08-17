/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nxml2json;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Andrey
 */
public class ArticleObject {

    private Document document;
    @SerializedName("title")
    public String title = "";
    @SerializedName("abstract")
    public Section abstractStr = null;
    //public String abstractStr = "";
    @SerializedName("date")
    public String date = "";
    @SerializedName("doi")
    public String doi = "";
    @SerializedName("pmid")
    public String pmid = "";
    @SerializedName("volume")
    public String volume = "";
    @SerializedName("journal")
    public String journal = "";
    @SerializedName("authors")
    public List<Author> authors = new ArrayList<Author>();
    @SerializedName("metainfo")
    public String metainfo;
    @SerializedName("FullText")
    public Section fullText = null;
    @SerializedName("citations")
    public Citation[] citations = null;

    public ArticleObject(Document _doc) {
        document = _doc;
    }

    public void parse() {
        abstractStr = getAbstractSec();
        title = getTitleString();
        date = getDateString();
        doi = getDoiString();
        pmid = getPmidString();
        volume = getVolumeString();
        journal = getJournalString();
        authors = getAuthorsList();

        fullText = getFullText();

        citations = getCitations();
        metainfo = getMetaInfo();
        document = null;
    }

    private String getAbstractString() {
        String value = null;
        Element main = document.getDocumentElement();

        NodeList frontNodes = main.getElementsByTagName("front");
        Node frontNode = frontNodes.item(0);

        NodeList frontNodeChilds = frontNode.getChildNodes();
        NodeList articleMetaNodes = null;

        for (int i = 0; i < frontNodeChilds.getLength(); i++) {
            Node node = frontNodeChilds.item(i);
            if (node.getNodeName() == "article-meta") {
                articleMetaNodes = node.getChildNodes();
                break;
            }
        }

        for (int i = 0; i < articleMetaNodes.getLength(); i++) {
            Node node = articleMetaNodes.item(i);
            if (node.getNodeName() == "abstract") {
                value = node.getTextContent();
                break;
            }
        }

        return value;
    }

    private String getTitleString() {
        String value = null;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile("/article/front/article-meta/title-group/article-title");
            value = (String) expr.evaluate(document, XPathConstants.STRING);

        } catch (XPathExpressionException xpex) {

        }

        return value;
    }

    private String getDateString() {
        String day = null;
        String month = null;
        String year = null;

        Element main = document.getDocumentElement();

        NodeList frontNodes = main.getElementsByTagName("front");
        Node frontNode = frontNodes.item(0);

        NodeList frontNodeChilds = frontNode.getChildNodes();
        NodeList articleMetaNodes = null;
        NodeList pubDateNodes = null;

        for (int i = 0; i < frontNodeChilds.getLength(); i++) {
            Node node = frontNodeChilds.item(i);
            if (node.getNodeName() == "article-meta") {
                articleMetaNodes = node.getChildNodes();
                break;
            }
        }

        for (int i = 0; i < articleMetaNodes.getLength(); i++) {
            Node node = articleMetaNodes.item(i);

            if (node.getNodeName() == "pub-date") {
                String pub_type = node.getAttributes().getNamedItem("pub-type").getNodeValue();
                if (pub_type.compareTo("epub") == 0) {
                    pubDateNodes = node.getChildNodes();
                    break;
                }
            }
        }

        for (int i = 0; i < pubDateNodes.getLength(); i++) {
            Node node = pubDateNodes.item(i);

            if (node.getNodeName() == "day") {
                day = node.getTextContent();
            } else if (node.getNodeName() == "month") {
                month = node.getTextContent();
            } else if (node.getNodeName() == "year") {
                year = node.getTextContent();
            }
        }

        return String.format("%s-%s-%s", month, day, year);
    }

    private String getDoiString() {
        String value = null;

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile("/article/front/article-meta/article-id[@pub-id-type='doi']");
            value = ((Node) expr.evaluate(document, XPathConstants.NODE)) != null ? ((Node) expr.evaluate(document, XPathConstants.NODE)).getTextContent() : null;

        } catch (XPathExpressionException xpex) {

        }

        return value;
    }

    private String getPmidString() {
        String value = null;

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile("/article/front/article-meta/article-id[@pub-id-type='pmid']");
            value = ((Node) expr.evaluate(document, XPathConstants.NODE)) != null ? ((Node) expr.evaluate(document, XPathConstants.NODE)).getTextContent() : null;

        } catch (XPathExpressionException xpex) {

        }

        return value;
    }

    private String getVolumeString() {
        String value = null;
        Element main = document.getDocumentElement();

        NodeList frontNodes = main.getElementsByTagName("front");
        Node frontNode = frontNodes.item(0);

        NodeList frontNodeChilds = frontNode.getChildNodes();
        NodeList articleMetaNodes = null;

        for (int i = 0; i < frontNodeChilds.getLength(); i++) {
            Node node = frontNodeChilds.item(i);
            if (node.getNodeName() == "article-meta") {
                articleMetaNodes = node.getChildNodes();
                break;
            }
        }

        for (int i = 0; i < articleMetaNodes.getLength(); i++) {
            Node node = articleMetaNodes.item(i);
            if (node.getNodeName() == "volume") {
                value = node.getTextContent();
                break;
            }
        }

        return value;
    }

    private String getJournalString() {
        String value = null;
        Element main = document.getDocumentElement();

        NodeList frontNodes = main.getElementsByTagName("front");
        Node frontNode = frontNodes.item(0);

        NodeList frontNodeChilds = frontNode.getChildNodes();
        NodeList journalMetaNodes = null;

        for (int i = 0; i < frontNodeChilds.getLength(); i++) {
            Node node = frontNodeChilds.item(i);
            if (node.getNodeName() == "journal-meta") {
                journalMetaNodes = node.getChildNodes();
                break;
            }
        }

        for (int i = 0; i < journalMetaNodes.getLength(); i++) {
            Node node = journalMetaNodes.item(i);
            String journal_id_type = node.getAttributes().getNamedItem("journal-id-type").getNodeValue();
            if (journal_id_type.compareTo("iso-abbrev") == 0) {
                value = node.getTextContent();
                break;
            }
        }

        return value;
    }

    private List<Author> getAuthorsList() {
        List<Author> value = new ArrayList<Author>();

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        NodeList authors = null;

        try {
            expr = xpath.compile("/article/front/article-meta/contrib-group/contrib[@contrib-type='author']/name");
            authors = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < authors.getLength(); i++) {
            NodeList subNodes = authors.item(i).getChildNodes();
            Author A = new Author();
            for (int j = 0; j < subNodes.getLength(); j++) {
                Node node = subNodes.item(j);
                if (node.getNodeName().equals("surname")) {
                    A.surname = node.getTextContent();
                } else if (node.getNodeName().equals("given-names")) {
                    A.givenNames = node.getTextContent();
                }
            }
            value.add(A);

        }

        return value;

        /*List<Author> value = new ArrayList<>();

         Element main = document.getDocumentElement();

         NodeList frontNodes = main.getElementsByTagName("front");
         Node frontNode = frontNodes.item(0);

         NodeList frontNodeChilds = frontNode.getChildNodes();
         NodeList articleMetaNodes = null;
         NodeList contribGroupNodes = null;

         for (int i = 0; i < frontNodeChilds.getLength(); i++) {
         Node node = frontNodeChilds.item(i);
         if (node.getNodeName() == "article-meta") {
         articleMetaNodes = node.getChildNodes();
         break;
         }
         }

         for (int i = 0; i < articleMetaNodes.getLength(); i++) {
         Node node = articleMetaNodes.item(i);

         if (node.getNodeName() == "contrib-group") {
         contribGroupNodes = node.getChildNodes();
         break;
         }
         }

         for (int i = 0; i < contribGroupNodes.getLength(); i++) {
         Node node = contribGroupNodes.item(i);
         String contrib_type = node.getAttributes().getNamedItem("contrib-type").getNodeValue();
         if (contrib_type.compareTo("author") == 0) {
         Author A = new Author();
         NodeList authorNodes = node.getChildNodes();
         for (int j = 0; j < authorNodes.getLength(); j++) {
         Node authorSubNode = authorNodes.item(j);
         if (authorSubNode.getNodeName().compareTo("name") == 0) {
         NodeList authorSubNodeList = authorSubNode.getChildNodes();
         for (int k = 0; k < authorSubNodeList.getLength(); k++) {
         Node authorInitials = authorSubNodeList.item(k);

         if (authorInitials.getNodeName().compareTo("surname") == 0) {
         A.surname = authorInitials.getTextContent();
         } else if (authorInitials.getNodeName().compareTo("given-names") == 0) {
         A.givenNames = authorInitials.getTextContent();
         }
         }
         }
         }
         value.add(A);

         }

         }

         return value;*/
    }

    private Section getFullText() {
        Section value = null;

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        Element body = null;

        try {
            expr = xpath.compile("/article/body");
            body = (Element) expr.evaluate(document, XPathConstants.NODE);

        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
        value = parseSection(body);
        return value;
    }

    private Section getAbstractSec() {
        Section value = null;

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        Element body = null;

        try {
            expr = xpath.compile("/article/front/article-meta/abstract");
            body = (Element) expr.evaluate(document, XPathConstants.NODE);

        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
        value = body != null ? parseSection(body) : null;
        return value;

    }

    private Section parseSection(Element sec) {
        Section value = new Section();

        NodeList title = sec.getElementsByTagName("title");
        value.title = title.getLength() > 0 ? title.item(0).getTextContent() : null;

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;

        NodeList P = null;
        try {
            expr = xpath.compile("p");
            P = (NodeList) expr.evaluate(sec, XPathConstants.NODESET);
        } catch (Exception e) {
        }
        if (P.getLength() > 0) {
            value.paragraph = new String[P.getLength()];

            for (int j = 0; j < P.getLength(); j++) {
                value.paragraph[j] = P.item(j).getTextContent();
            }
        }

        xPathfactory = XPathFactory.newInstance();
        xpath = xPathfactory.newXPath();
        expr = null;
        NodeList subSec = null;
        try {
            expr = xpath.compile("sec");
            subSec = (NodeList) expr.evaluate(sec, XPathConstants.NODESET);
        } catch (Exception e) {
        }
        if (subSec.getLength() > 0) {
            value.subsections = new Section[subSec.getLength()];

            for (int i = 0; i < subSec.getLength(); i++) {
                Element sub = (Element) subSec.item(i);
                value.subsections[i] = parseSection(sub);
            }
        }
        return value;
    }

    private Citation[] getCitations() {
        Citation[] value = null;

        NodeList refs = null;

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile("/article/back/ref-list/ref/element-citation");
            refs = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {

        }

        value = new Citation[refs.getLength()];

        for (int i = 0; i < refs.getLength(); i++) {
            Citation citation = parseCitation((Element) refs.item(i));
            if (citation.personGroup.name != null) {
                for (Name name : citation.personGroup.name) {
                    citation.reference += name.givenNames + " " + name.surname + " ";
                }
            }
            citation.reference += citation.articleTitle + " ";
            citation.reference += !citation.source.values().isEmpty() ? citation.source.values().toArray()[0] : "";
            citation.reference += " " + citation.volume + " " + citation.fpage + " " + citation.year;
            value[i] = citation;
        }

        return value;
    }

    private Citation parseCitation(Element node) {
        Citation value = new Citation();
        value.publicationType = node.getAttribute("publication-type");

        NodeList names = null;
        NodeList source = null;
        Element personaGroup = null;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        NodeList pubIds = null;

        XPathExpression expr = null;
        try {
            expr = xpath.compile("person-group");
            personaGroup = (Element) expr.evaluate(node, XPathConstants.NODE);

            Element tmp;

            expr = xpath.compile("volume");
            tmp = (Element) expr.evaluate(node, XPathConstants.NODE);
            value.volume = tmp != null ? tmp.getTextContent() : null;

            expr = xpath.compile("year");
            tmp = (Element) expr.evaluate(node, XPathConstants.NODE);
            value.year = tmp != null ? tmp.getTextContent() : null;

            expr = xpath.compile("fpage");
            tmp = (Element) expr.evaluate(node, XPathConstants.NODE);
            value.fpage = tmp != null ? tmp.getTextContent() : null;

            expr = xpath.compile("lpage");
            tmp = (Element) expr.evaluate(node, XPathConstants.NODE);
            value.lpage = tmp != null ? tmp.getTextContent() : null;

            expr = xpath.compile("issue");
            tmp = (Element) expr.evaluate(node, XPathConstants.NODE);
            value.issue = tmp != null ? tmp.getTextContent() : null;

            expr = xpath.compile("article-title");
            tmp = (Element) expr.evaluate(node, XPathConstants.NODE);
            value.articleTitle = tmp != null ? tmp.getTextContent() : null;

            expr = xpath.compile("pub-id");
            pubIds = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

            expr = xpath.compile("source");
            source = ((Node) expr.evaluate(node, XPathConstants.NODE)) != null ? ((Node) expr.evaluate(node, XPathConstants.NODE)).getChildNodes() : null;
        } catch (XPathExpressionException ex) {
        }
        if (personaGroup != null) {
            value.personGroup.personGroupType = personaGroup.getAttribute("person-group-type");
            names = personaGroup.getChildNodes();

            value.personGroup.name = new Name[names.getLength()];
            for (int i = 0; i < names.getLength(); i++) {
                NodeList nameSubNode = names.item(i).getChildNodes();
                Name name = new Name();
                for (int j = 0; j < nameSubNode.getLength(); j++) {
                    Node N = nameSubNode.item(j);
                    if (N.getNodeName().compareTo("surname") == 0) {
                        name.surname = N.getTextContent();
                    } else if (N.getNodeName().compareTo("given-names") == 0) {
                        name.givenNames = N.getTextContent();
                    }
                }

                value.personGroup.name[i] = name;
            }

        }

        for (int i = 0; source != null && i < source.getLength(); i++) {
            Node N = source.item(i);
            value.source.put(N.getNodeName(), N.getTextContent());
        }

        if (pubIds.getLength() > 0) {
            value.pubId = new PubId[pubIds.getLength()];

            for (int i = 0; i < pubIds.getLength(); i++) {
                Element E = (Element) pubIds.item(i);

                PubId P = new PubId();
                P.pubIdType = E.getAttribute("pub-id-type");
                P.content = E.getTextContent();
                value.pubId[i] = P;
            }
        }
        return value;
    }

    private String getMetaInfo() {
        String value = "";
        value += this.title + " " + this.volume;
        return value;
    }
}

package com.cinema;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Main {
	// Fonctionnalite 1 : validation XML/XSD
    public static void validerXML() {

        System.out.println("===== VALIDATION XML/XSD =====");

        try (
            InputStream xsd =
                Main.class.getClassLoader()
                    .getResourceAsStream("cinema.xsd");

            InputStream xml =
                Main.class.getClassLoader()
                    .getResourceAsStream("cinema.xml")
        ) {

            if (xsd == null || xml == null) {
                System.out.println(
                    "Erreur : cinema.xml ou cinema.xsd introuvable.");
                return;
            }

            SchemaFactory factory =
                SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema schema =
                factory.newSchema(new StreamSource(xsd));

            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(xml));

            System.out.println(
                "Le document cinema.xml est valide !");

        } catch (Exception e) {

            System.out.println(
                "Erreur de validation : " + e.getMessage());
        }
    }

    // Fonctionnalite 2 : interrogation XPath
    public static void interrogerXPath() {

        System.out.println("\n===== INTERROGATION XPATH =====");

        try (
            InputStream xml =
                Main.class.getClassLoader()
                    .getResourceAsStream("cinema.xml")
        ) {

            if (xml == null) {
                System.out.println(
                    "Erreur : cinema.xml introuvable.");
                return;
            }

            DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                dbFactory.newDocumentBuilder();

            Document document = builder.parse(xml);

            XPath xpath =
                XPathFactory.newInstance().newXPath();

            // Requete 1 : tous les titres des films
            afficherResultats(
                document,
                xpath,
                "//film/titre",
                "Tous les titres des films");

            // Requete 2 : films de science-fiction
            afficherResultats(
                document,
                xpath,
                "//film[@genre='Science-fiction']/titre",
                "Films de science-fiction");

            // Requete 3 : spectateurs actifs
            afficherResultats(
                document,
                xpath,
                "//spectateur[@actif='true']/nom",
                "Spectateurs actifs");

            // Requete 4 : emails des spectateurs actifs
            afficherResultats(
                document,
                xpath,
                "//spectateur[@actif='true']/email",
                "Emails des spectateurs actifs");

            // Requete 5 : identifiants des seances
            afficherResultats(
                document,
                xpath,
                "//seance/@id",
                "Identifiants des seances");

            // Requete 6 : seances dans la salle 1
            afficherResultats(
                document,
                xpath,
                "//seance[salle=1]/@id",
                "Seances dans la salle 1");

        } catch (Exception e) {

            System.out.println(
                "Erreur XPath : " + e.getMessage());
        }
    }

    // Methode pour executer et afficher une requete XPath
    private static void afficherResultats(
            Document document,
            XPath xpath,
            String expression,
            String description) throws Exception {

        NodeList resultats = (NodeList) xpath.evaluate(
            expression,
            document,
            XPathConstants.NODESET);

        System.out.println("\n" + description + " :");

        if (resultats.getLength() == 0) {
            System.out.println("Aucun resultat.");
            return;
        }

        for (int i = 0; i < resultats.getLength(); i++) {
            System.out.println(
                "- " + resultats.item(i).getTextContent());
        }
    }

    public static void main(String[] args) {

        validerXML();

        interrogerXPath();
    }

}

package com.example.semantic_web_hw1.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class RecipeScraper {

    private static final String URL = "https://www.bbcgoodfood.com/recipes/collection/budget-autumn";

    public static void main(String[] args) {
        try {
            List<String> scrapedTitles = scrapeRecipeTitles();
            List<String> uniqueTitles = removeDuplicates(scrapedTitles);

            System.out.println("SCRAPED TITLES:");
            for (String title : uniqueTitles) {
                System.out.println(title);
            }

            if (uniqueTitles.size() < 20) {
                System.out.println("Not enough recipe titles found. Found only: " + uniqueTitles.size());
                return;
            }

            List<String> first20Titles = uniqueTitles.subList(0, 20);
            generateRecipesXml(first20Titles);

            System.out.println("recipes.xml generated successfully with 20 scraped recipe titles.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> scrapeRecipeTitles() throws Exception {
        List<String> titles = new ArrayList<>();

        Document doc = Jsoup.connect(URL)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        Elements links = doc.select("a[href]");

        for (Element link : links) {
            String href = link.attr("href").trim();
            String text = link.text().trim();

            if (text.isEmpty()) {
                continue;
            }

            // accept both relative and absolute recipe links
            if (!(href.contains("/recipes/") || href.contains("bbcgoodfood.com/recipes/"))) {
                continue;
            }

            // exclude collection pages
            if (href.contains("/recipes/collection/")) {
                continue;
            }

            if (isValidRecipeTitle(text)) {
                titles.add(cleanRecipeTitle(text));
            }
        }

        return titles;
    }

    private static boolean isValidRecipeTitle(String text) {
        String lower = text.toLowerCase();

        if (lower.contains("skip to main content")) return false;
        if (lower.contains("subscribe")) return false;
        if (lower.contains("download our app")) return false;
        if (lower.contains("good food shows")) return false;
        if (lower.contains("gf x")) return false;
        if (lower.contains("sign up today")) return false;
        if (lower.contains("whatsapp")) return false;
        if (lower.contains("what to cook")) return false;
        if (lower.contains("quick and easy")) return false;
        if (lower.contains("batch cooking")) return false;
        if (lower.contains("all inspiration")) return false;
        if (lower.contains("meal type")) return false;
        if (lower.contains("browse all meal types")) return false;
        if (lower.contains("budget autumn recipes")) return false;
        if (lower.contains("showing")) return false;
        if (lower.contains("results")) return false;
        if (lower.equals("occasion recipes")) return false;
        if (lower.equals("diet type")) return false;
        if (lower.equals("browse all diet types")) return false;
        if (lower.equals("quick budget dinners")) return false;
        if (lower.equals("dietary needs")) return false;
        if (lower.equals("healthy eating")) return false;
        if (lower.equals("bread, pastry & cakes")) return false;
        if (lower.equals("dinner ideas")) return false;
        if (lower.equals("nuts & seeds")) return false;
        if (lower.equals("dinner party")) return false;
        if (lower.equals("special occasions")) return false;
        if (lower.equals("casserole & slow cooker")) return false;
        if (lower.equals("pulses & grains")) return false;
        if (lower.equals("sweet treats")) return false;
        if (lower.equals("quick & easy budget meals")) return false;
        if (lower.equals("family friendly")) return false;
        if (lower.equals("red meat & game")) return false;
        if (lower.equals("fish & seafood")) return false;
        if (lower.equals("religious events")) return false;

        return text.length() >= 4 && text.split(" ").length >= 2;
    }

    private static String cleanRecipeTitle(String text) {
        return text
                .replace("App only", "")
                .replace(". This is a premium piece of content available to subscribed users.", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static List<String> removeDuplicates(List<String> titles) {
        LinkedHashSet<String> unique = new LinkedHashSet<>(titles);
        return new ArrayList<>(unique);
    }

    private static void generateRecipesXml(List<String> titles) throws Exception {
        String[] cuisinePool = {
                "Italian", "European", "Mediterranean", "Indian", "Asian",
                "Chinese", "Mexican", "Latin American", "Greek", "Japanese",
                "French", "Spanish", "American", "Thai", "Middle Eastern", "British"
        };

        String[] difficultyPool = {"Beginner", "Intermediate", "Advanced"};

        Random random = new Random();

        File outputFile = new File("src/main/resources/data/recipes.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document xmlDoc = builder.newDocument();

        org.w3c.dom.Element root = xmlDoc.createElement("recipes");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:noNamespaceSchemaLocation", "recipes.xsd");
        xmlDoc.appendChild(root);

        for (int i = 0; i < titles.size(); i++) {
            org.w3c.dom.Element recipe = xmlDoc.createElement("recipe");
            recipe.setAttribute("id", String.valueOf(i + 1));

            org.w3c.dom.Element title = xmlDoc.createElement("title");
            title.setTextContent(titles.get(i));
            recipe.appendChild(title);

            org.w3c.dom.Element cuisineTypes = xmlDoc.createElement("cuisineTypes");
            List<String> twoCuisines = pickTwoDifferent(cuisinePool, random);
            for (String cuisine : twoCuisines) {
                org.w3c.dom.Element cuisineEl = xmlDoc.createElement("cuisine");
                cuisineEl.setTextContent(cuisine);
                cuisineTypes.appendChild(cuisineEl);
            }
            recipe.appendChild(cuisineTypes);

            org.w3c.dom.Element difficultyLevels = xmlDoc.createElement("difficultyLevels");
            for (String levelValue : difficultyPool) {
                org.w3c.dom.Element level = xmlDoc.createElement("level");
                level.setTextContent(levelValue);
                difficultyLevels.appendChild(level);
            }
            recipe.appendChild(difficultyLevels);

            org.w3c.dom.Element description = xmlDoc.createElement("description");
            description.setTextContent("Scraped recipe title from BBC Good Food collection page.");
            recipe.appendChild(description);

            root.appendChild(recipe);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(xmlDoc), new StreamResult(outputFile));
    }

    private static List<String> pickTwoDifferent(String[] values, Random random) {
        int first = random.nextInt(values.length);
        int second;

        do {
            second = random.nextInt(values.length);
        } while (second == first);

        return Arrays.asList(values[first], values[second]);
    }
}
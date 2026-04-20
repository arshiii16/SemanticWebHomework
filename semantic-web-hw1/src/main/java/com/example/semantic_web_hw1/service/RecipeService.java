package com.example.semantic_web_hw1.service;

import com.example.semantic_web_hw1.model.Recipe;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

@Service
public class RecipeService {

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        try {
            File xmlFile = new File("src/main/resources/data/recipes.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList recipeNodes = document.getElementsByTagName("recipe");

            for (int i = 0; i < recipeNodes.getLength(); i++) {
                Node node = recipeNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element recipeElement = (Element) node;

                    int id = Integer.parseInt(recipeElement.getAttribute("id"));
                    String title = getTagValue("title", recipeElement);
                    String description = getTagValue("description", recipeElement);

                    List<String> cuisineTypes = new ArrayList<>();
                    NodeList cuisineNodes = recipeElement.getElementsByTagName("cuisine");
                    for (int j = 0; j < cuisineNodes.getLength(); j++) {
                        cuisineTypes.add(cuisineNodes.item(j).getTextContent());
                    }

                    List<String> difficultyLevels = new ArrayList<>();
                    NodeList levelNodes = recipeElement.getElementsByTagName("level");
                    for (int j = 0; j < levelNodes.getLength(); j++) {
                        difficultyLevels.add(levelNodes.item(j).getTextContent());
                    }

                    Recipe recipe = new Recipe(id, title, cuisineTypes, difficultyLevels, description);
                    recipes.add(recipe);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public List<Recipe> getRecipesBySkillLevel(String skillLevel) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            File xmlFile = new File("src/main/resources/data/recipes.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/recipes/recipe[difficultyLevels/level='" + skillLevel + "']";

            NodeList recipeNodes = (NodeList) xPath.evaluate(expression, document, XPathConstants.NODESET);

            for (int i = 0; i < recipeNodes.getLength(); i++) {
                Node node = recipeNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element recipeElement = (Element) node;

                    int id = Integer.parseInt(recipeElement.getAttribute("id"));
                    String title = getTagValue("title", recipeElement);
                    String description = getTagValue("description", recipeElement);

                    List<String> cuisineTypes = new ArrayList<>();
                    NodeList cuisineNodes = recipeElement.getElementsByTagName("cuisine");
                    for (int j = 0; j < cuisineNodes.getLength(); j++) {
                        cuisineTypes.add(cuisineNodes.item(j).getTextContent());
                    }

                    List<String> difficultyLevels = new ArrayList<>();
                    NodeList levelNodes = recipeElement.getElementsByTagName("level");
                    for (int j = 0; j < levelNodes.getLength(); j++) {
                        difficultyLevels.add(levelNodes.item(j).getTextContent());
                    }

                    recipes.add(new Recipe(id, title, cuisineTypes, difficultyLevels, description));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public List<Recipe> getRecipesBySkillLevelAndCuisine(String skillLevel, String cuisineType) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            File xmlFile = new File("src/main/resources/data/recipes.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/recipes/recipe[difficultyLevels/level='" + skillLevel +
                    "' and cuisineTypes/cuisine='" + cuisineType + "']";

            NodeList recipeNodes = (NodeList) xPath.evaluate(expression, document, XPathConstants.NODESET);

            for (int i = 0; i < recipeNodes.getLength(); i++) {
                Node node = recipeNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element recipeElement = (Element) node;

                    int id = Integer.parseInt(recipeElement.getAttribute("id"));
                    String title = getTagValue("title", recipeElement);
                    String description = getTagValue("description", recipeElement);

                    List<String> cuisineTypes = new ArrayList<>();
                    NodeList cuisineNodes = recipeElement.getElementsByTagName("cuisine");
                    for (int j = 0; j < cuisineNodes.getLength(); j++) {
                        cuisineTypes.add(cuisineNodes.item(j).getTextContent());
                    }

                    List<String> difficultyLevels = new ArrayList<>();
                    NodeList levelNodes = recipeElement.getElementsByTagName("level");
                    for (int j = 0; j < levelNodes.getLength(); j++) {
                        difficultyLevels.add(levelNodes.item(j).getTextContent());
                    }

                    recipes.add(new Recipe(id, title, cuisineTypes, difficultyLevels, description));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

    private String getTagValue(String tagName, Element parentElement) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    public void addRecipe(Recipe recipe) throws Exception {
        if (recipe.getTitle() == null || recipe.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }

        if (recipe.getCuisineInput() == null || recipe.getCuisineInput().trim().isEmpty()) {
            throw new IllegalArgumentException("Cuisine types are required.");
        }

        if (recipe.getDifficultyInput() == null || recipe.getDifficultyInput().trim().isEmpty()) {
            throw new IllegalArgumentException("Difficulty levels are required.");
        }

        List<String> cuisines = Arrays.stream(recipe.getCuisineInput().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        List<String> difficultyLevels = Arrays.stream(recipe.getDifficultyInput().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        if (cuisines.size() != 2) {
            throw new IllegalArgumentException("You must enter exactly 2 cuisine types, separated by commas.");
        }

        if (difficultyLevels.size() != 3) {
            throw new IllegalArgumentException("You must enter exactly 3 difficulty levels, separated by commas.");
        }

        if (recipe.getDescription() == null || recipe.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }

        File xmlFile = new File("src/main/resources/data/recipes.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);

        Element root = document.getDocumentElement();

        NodeList recipeNodes = document.getElementsByTagName("recipe");
        int newId = recipeNodes.getLength() + 1;

        Element newRecipe = document.createElement("recipe");
        newRecipe.setAttribute("id", String.valueOf(newId));

        Element title = document.createElement("title");
        title.setTextContent(recipe.getTitle());
        newRecipe.appendChild(title);

        Element cuisineTypes = document.createElement("cuisineTypes");
        for (String cuisine : cuisines) {
            Element cuisineElement = document.createElement("cuisine");
            cuisineElement.setTextContent(cuisine);
            cuisineTypes.appendChild(cuisineElement);
        }
        newRecipe.appendChild(cuisineTypes);

        Element difficultyLevelsElement = document.createElement("difficultyLevels");
        for (String level : difficultyLevels) {
            Element levelElement = document.createElement("level");
            levelElement.setTextContent(level);
            difficultyLevelsElement.appendChild(levelElement);
        }
        newRecipe.appendChild(difficultyLevelsElement);

        Element description = document.createElement("description");
        description.setTextContent(recipe.getDescription());
        newRecipe.appendChild(description);

        root.appendChild(newRecipe);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }

    public String transformRecipesWithXsl(String userSkill) {
        try {
            File xmlFile = new File("src/main/resources/data/recipes.xml");
            File xslFile = new File("src/main/resources/xsl/recipes.xsl");

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslFile));

            transformer.setParameter("userSkill", userSkill);

            StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(xmlFile), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<h2>Error while transforming XML with XSL.</h2>";
        }
    }

    public Recipe getRecipeById(int recipeId) {
        try {
            File xmlFile = new File("src/main/resources/data/recipes.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/recipes/recipe[@id='" + recipeId + "']";

            Node node = (Node) xPath.evaluate(expression, document, XPathConstants.NODE);

            if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
                Element recipeElement = (Element) node;

                int id = Integer.parseInt(recipeElement.getAttribute("id"));
                String title = getTagValue("title", recipeElement);
                String description = getTagValue("description", recipeElement);

                List<String> cuisineTypes = new ArrayList<>();
                NodeList cuisineNodes = recipeElement.getElementsByTagName("cuisine");
                for (int j = 0; j < cuisineNodes.getLength(); j++) {
                    cuisineTypes.add(cuisineNodes.item(j).getTextContent());
                }

                List<String> difficultyLevels = new ArrayList<>();
                NodeList levelNodes = recipeElement.getElementsByTagName("level");
                for (int j = 0; j < levelNodes.getLength(); j++) {
                    difficultyLevels.add(levelNodes.item(j).getTextContent());
                }

                return new Recipe(id, title, cuisineTypes, difficultyLevels, description);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<Recipe> getRecipesByCuisineType(String cuisineType) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            File xmlFile = new File("src/main/resources/data/recipes.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/recipes/recipe[cuisineTypes/cuisine='" + cuisineType + "']";

            NodeList recipeNodes = (NodeList) xPath.evaluate(expression, document, XPathConstants.NODESET);

            for (int i = 0; i < recipeNodes.getLength(); i++) {
                Node node = recipeNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element recipeElement = (Element) node;

                    int id = Integer.parseInt(recipeElement.getAttribute("id"));
                    String title = getTagValue("title", recipeElement);
                    String description = getTagValue("description", recipeElement);

                    List<String> cuisineTypes = new ArrayList<>();
                    NodeList cuisineNodes = recipeElement.getElementsByTagName("cuisine");
                    for (int j = 0; j < cuisineNodes.getLength(); j++) {
                        cuisineTypes.add(cuisineNodes.item(j).getTextContent());
                    }

                    List<String> difficultyLevels = new ArrayList<>();
                    NodeList levelNodes = recipeElement.getElementsByTagName("level");
                    for (int j = 0; j < levelNodes.getLength(); j++) {
                        difficultyLevels.add(levelNodes.item(j).getTextContent());
                    }

                    recipes.add(new Recipe(id, title, cuisineTypes, difficultyLevels, description));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
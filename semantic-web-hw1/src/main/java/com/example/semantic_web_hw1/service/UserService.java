package com.example.semantic_web_hw1.service;

import com.example.semantic_web_hw1.model.UserProfile;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public List<UserProfile> getAllUsers() {
        List<UserProfile> users = new ArrayList<>();

        try {
            File xmlFile = new File("src/main/resources/data/users.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList userNodes = document.getElementsByTagName("user");

            for (int i = 0; i < userNodes.getLength(); i++) {
                Element userElement = (Element) userNodes.item(i);

                int id = Integer.parseInt(userElement.getAttribute("id"));
                String name = getTagValue("name", userElement);
                String surname = getTagValue("surname", userElement);
                String cookingSkillLevel = getTagValue("cookingSkillLevel", userElement);
                String preferredCuisineType = getTagValue("preferredCuisineType", userElement);

                users.add(new UserProfile(id, name, surname, cookingSkillLevel, preferredCuisineType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public void addUser(UserProfile user) throws Exception {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (user.getSurname() == null || user.getSurname().trim().isEmpty()) {
            throw new IllegalArgumentException("Surname is required.");
        }

        if (user.getCookingSkillLevel() == null || user.getCookingSkillLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("Cooking skill level is required.");
        }

        if (user.getPreferredCuisineType() == null || user.getPreferredCuisineType().trim().isEmpty()) {
            throw new IllegalArgumentException("Preferred cuisine type is required.");
        }

        File xmlFile = new File("src/main/resources/data/users.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);

        Element root = document.getDocumentElement();

        NodeList userNodes = document.getElementsByTagName("user");
        int newId = userNodes.getLength() + 1;

        Element newUser = document.createElement("user");
        newUser.setAttribute("id", String.valueOf(newId));

        Element name = document.createElement("name");
        name.setTextContent(user.getName());
        newUser.appendChild(name);

        Element surname = document.createElement("surname");
        surname.setTextContent(user.getSurname());
        newUser.appendChild(surname);

        Element cookingSkillLevel = document.createElement("cookingSkillLevel");
        cookingSkillLevel.setTextContent(user.getCookingSkillLevel());
        newUser.appendChild(cookingSkillLevel);

        Element preferredCuisineType = document.createElement("preferredCuisineType");
        preferredCuisineType.setTextContent(user.getPreferredCuisineType());
        newUser.appendChild(preferredCuisineType);

        root.appendChild(newUser);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }

    public UserProfile getFirstUser() {
        List<UserProfile> users = getAllUsers();
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    private String getTagValue(String tagName, Element parentElement) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}
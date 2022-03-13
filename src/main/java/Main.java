import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "res" + File.separator + "data.xml";
        String jsonFile = "res" + File.separator + "json.json";
        List<Employee> list = parseXML(fileName);
        String json = listToJson(list);
        writeString(json, jsonFile);
    }

    private static List<Employee> parseXML(String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = factory.newDocumentBuilder();

            Document doc = db.parse(new File(fileName));
            doc.getDocumentElement().normalize();
            Node root = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("employee");

            List<Employee> empList = new ArrayList<Employee>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                empList.add(getEmployee(nodeList.item(i)));
            }
            return empList;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }


    private static Employee getEmployee(Node node) {
        Employee employee = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            employee = new Employee(
                    Integer.parseInt(getTagValue("id", element)),
                    getTagValue("firstName", element),
                    getTagValue("lastName", element),
                    getTagValue("country", element),
                    Integer.parseInt(getTagValue("age", element))
            );
        }

        return employee;
    }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }




    private static void writeString(String json, String jsonFile) {
        File file = new File(jsonFile);
        try (FileWriter writer = new FileWriter(file, false)){
            writer.write(json.toString());
            writer.flush();
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        //System.out.println(json);
        return json;
    }

}

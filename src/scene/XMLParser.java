package scene;

import geometries.*;
import lighting.AmbientLight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import primitives.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * XMLParser class to parse XML files and construct Scene objects.
 *
 * @author Hila and Hila
 */
public class XMLParser {
    /**
     * The name of the XML file to be parsed.
     */
    private final String fileName;

    /**
     * Constructor to initialize the XMLParser with a filename.
     *
     * @param name the name of the XML file
     */
    public XMLParser(String name) {
        this.fileName = name;
    }

    /**
     * Convert a string to Double3 object.
     *
     * @param string the input string
     * @return a Double3 object
     */
    private Double3 stringToDouble3(String string) {
        String[] list = string.split(" ");
        return new Double3(
                Double.parseDouble(list[0]),
                Double.parseDouble(list[1]),
                Double.parseDouble(list[2])
        );
    }

    /**
     * Convert a string to a Color object.
     *
     * @param string the input string
     * @return a Color object
     */
    private Color makeColor(String string) {
        Double3 rgb = stringToDouble3(string);
        return new Color(rgb.d1(), rgb.d2(), rgb.d3());
    }

    /**
     * Parse geometries from an XML element and add them to the Geometries object.
     *
     * @param element    the XML element
     * @param geometries the Geometries object to populate
     */
    private void parseGeometries(Element element, Geometries geometries) {
        NodeList shapes = element.getChildNodes();

        for (int i = 0; i < shapes.getLength(); i++) {
            Node node = shapes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element shapeElement = (Element) node;
                switch (shapeElement.getTagName()) {
                    case "sphere" -> geometries.add(new Sphere(
                            Double.parseDouble(shapeElement.getAttribute("radius")),
                            new Point(stringToDouble3(shapeElement.getAttribute("center")))
                                        ));
                    case "triangle" -> geometries.add(new Triangle(
                            new Point(stringToDouble3(shapeElement.getAttribute("p0"))),
                            new Point(stringToDouble3(shapeElement.getAttribute("p1"))),
                            new Point(stringToDouble3(shapeElement.getAttribute("p2")))
                    ));
                    case "plane" -> geometries.add(new Plane(
                            new Point(stringToDouble3(shapeElement.getAttribute("p0"))),
                            new Point(stringToDouble3(shapeElement.getAttribute("p1"))),
                            new Point(stringToDouble3(shapeElement.getAttribute("p2")))
                    ));
                    case "cylinder" -> geometries.add(new Cylinder(
                            Double.parseDouble(shapeElement.getAttribute("radius")),
                            new Ray(
                                    new Point(stringToDouble3(shapeElement.getAttribute("origin"))),
                                    new Vector(stringToDouble3(shapeElement.getAttribute("direction")))
                            ),
                            Double.parseDouble(shapeElement.getAttribute("height"))
                    ));
                    case "tube" -> geometries.add(new Tube(
                            Double.parseDouble(shapeElement.getAttribute("radius")),
                            new Ray(
                                                        new Point(stringToDouble3(shapeElement.getAttribute("origin"))),
                                                        new Vector(stringToDouble3(shapeElement.getAttribute("direction")))
                                                )
                                        ));
                    case "polygon" -> {
                        String[] pointsStr = shapeElement.getAttribute("points").split(";");
                        Point[] points = Arrays.stream(pointsStr)
                                .map((String p) -> new Point(stringToDouble3(p.trim())))
                                .toArray(Point[]::new);
                        geometries.add(new Polygon(points));

                    }

                    default -> throw new UnsupportedOperationException(
                            "Unsupported geometry type: " + shapeElement.getTagName()
                    );
                }
            }
        }
    }

    /**
     * Parse the XML file and construct a Scene object.
     *
     * @return a Scene object
     * @throws IOException                  if reading the file fails
     * @throws ParserConfigurationException if XML parser setup fails
     * @throws SAXException                 if the XML is invalid
     */
    public Scene parse() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(this.fileName));
        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();
        if (!"scene".equals(root.getNodeName())) {
            throw new UnsupportedOperationException("Invalid root element in XML");
        }

        Scene scene = new Scene(this.fileName);
        scene.setBackground(makeColor(root.getAttribute("background-color")));

        NodeList children = root.getChildNodes();
        Geometries geometries = new Geometries();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                switch (element.getTagName()) {
                    case "ambient-light" -> scene.setAmbientLight(new AmbientLight(
                            makeColor(element.getAttribute("color"))
                    ));
                    case "geometries" -> parseGeometries(element, geometries);
                    default -> throw new UnsupportedOperationException(
                            "Unsupported tag: " + element.getTagName()
                    );
                }
            }
        }

        scene.setGeometries(geometries);
        return scene;
    }
}
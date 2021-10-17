package modeldifference.orient.query;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import com.orientechnologies.orient.core.record.impl.OVertexDocument;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import modeldifference.models.ConcreteStateId;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.IOrientDbFactory;
import modeldifference.orient.OrientDbCommand;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WidgetTreeQuery implements IWidgetTreeQuery{

    private final IOrientDbFactory orientDbFactory;

    public WidgetTreeQuery(IOrientDbFactory orientDbFactory){

        this.orientDbFactory = orientDbFactory;
    }

    public String query(Path location, ConcreteStateId concreteStateId) {
        var elements = getWidgets(concreteStateId);

        // then get the parent/child relationship between the widgets
        var sql = "SELECT FROM isChildOf WHERE in IN(SELECT ConcreteIDCustom FROM (TRAVERSE in('isChildOf') FROM (SELECT FROM Widget WHERE ConcreteIDCustom = :concreteId)))";

        var command = new OrientDbCommand(sql)
                .addParameter("concreteId", concreteStateId);

        try(var resultSet = command.executeReader(orientDbFactory.openDatabase())){
            elements.addAll(fetchEdges(resultSet, "isChildOf"));
        }

        // create a filename
        var filename = concreteStateId.getValue() + "_widget_tree_json";
        return writeJson(location, elements, filename);
    }

    private ArrayList<Element> getWidgets(ConcreteStateId concreteStateId){
        var elements = new ArrayList<Element>();

        // first get all the widgets using the State ConcreteIDCustom property
        var sql = "SELECT FROM (TRAVERSE IN('isChildOf') FROM (SELECT FROM Widget WHERE ConcreteIDCustom = :concreteId))";
        var command = new OrientDbCommand(sql)
                .addParameter("concreteId", concreteStateId);

        try(var resultSet = command.executeReader(orientDbFactory.openDatabase())){
            elements.addAll(fetchNodes(resultSet, "Widget", null, ""));
        }

        return elements;
    }



    // this helper method will write elements to a file in json format
    private String writeJson(Path location, ArrayList<Element> elements, String filename) {
        // check if the subfolder already exists

        var subFolder = Paths.get(location.toString(), "widgetTrees").toFile();

        if(!subFolder.exists()) {subFolder.mkdirs();}

        File output = new File(subFolder, filename);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(elements);
            // let's write the resulting json to a file
            if (output.exists() || output.createNewFile()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(output.getAbsolutePath()));
                writer.write(result);
                writer.close();
            }

            return output.getCanonicalPath();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }


    public class Vertex extends Document {

        public Vertex(String id) {
            super(id);
        }

    }

    public class Edge extends Document {

        private String sourceId;

        private String targetId;

        public Edge(String id, String sourceId, String targetId) {
            super(id);
            this.sourceId = sourceId;
            this.targetId = targetId;
        }

        @JsonGetter("source")
        public String getSourceId() {
            return sourceId;
        }

        @JsonGetter("target")
        public String getTargetId() {
            return targetId;
        }
    }


    // this helper method formats the @RID property into something that can be used in a web frontend
    private String formatId(String id) {
        if (id.indexOf("#") != 0) return id; // not an orientdb id
        id = id.replaceAll("[#]", "");
        return id.replaceAll("[:]", "_");
    }

    /**
     * This method saves screenshots to disk.
     * @param recordBytes
     * @param identifier
     */
        /*

    private String processScreenShot(ORecordBytes recordBytes, String identifier, String folderName) {
        if (!Main.outputDir.substring(Main.outputDir.length() - 1).equals(File.separator)) {
            Main.outputDir += File.separator;
        }

        // see if we have a directory for the screenshots yet
        File screenshotDir = new File(modelDifferenceReportDirectory + File.separator);

        // save the file to disk
        File screenshotFile = new File(screenshotDir, identifier + ".png");
        if (screenshotFile.exists()) {
            return new File(screenshotFile.getName()).getPath();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(screenshotFile.getCanonicalPath());
            outputStream.write(recordBytes.toStream());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new File(screenshotFile.getName()).getPath();
    }
    */

    /**
     * This method transforms a resultset of nodes into elements.
     * @param resultSet
     * @param className
     * @return
     */
    private ArrayList<Element> fetchNodes(OResultSet resultSet, String className, String parent, String modelIdentifier) {
        ArrayList<Element> elements = new ArrayList<>();

        while (resultSet.hasNext()) {
            OResult result = resultSet.next();
            // we're expecting a vertex
            if (result.isVertex()) {
                Optional<OVertex> op = result.getVertex();
                if (!op.isPresent()) continue;
                OVertex stateVertex = op.get();
                Vertex jsonVertex = new Vertex("n" + formatId(stateVertex.getIdentity().toString()));
                for (String propertyName : stateVertex.getPropertyNames()) {
                    if (propertyName.contains("in_") || propertyName.contains("out_")) {
                        // these are edge indicators. Ignore
                        continue;
                    }
                    if (propertyName.equals("screenshot")) {
                        // process the screenshot separately
         //               processScreenShot(stateVertex.getProperty("screenshot"), "n" + formatId(stateVertex.getIdentity().toString()), modelIdentifier);
                        continue;
                    }
                    jsonVertex.addProperty(propertyName, stateVertex.getProperty(propertyName).toString());
                }
                // optionally add a parent
                if (parent != null) {
                    jsonVertex.addProperty("parent", parent);
                }
                Element element = new Element(Element.GROUP_NODES, jsonVertex, className);
                if(stateVertex.getPropertyNames().contains("isInitial") && (Boolean)stateVertex.getProperty("isInitial")) {
                    element.addClass("isInitial");
                }
                elements.add(element);
            }
        }
        return elements;
    }

    private ArrayList<Element> fetchEdges(OResultSet resultSet, String className) {
        ArrayList<Element> elements = new ArrayList<>();
        while (resultSet.hasNext()) {
            OResult result = resultSet.next();
            // we're expecting a vertex
            if (result.isEdge()) {
                Optional<OEdge> op = result.getEdge();
                if (!op.isPresent()) continue;
                OEdge actionEdge = op.get();
                OVertexDocument source = actionEdge.getProperty("out");
                OVertexDocument target = actionEdge.getProperty("in");
                Edge jsonEdge = new Edge("e" + formatId(actionEdge.getIdentity().toString()), "n" + formatId(source.getIdentity().toString()), "n" + formatId(target.getIdentity().toString()));
                for (String propertyName : actionEdge.getPropertyNames()) {
                    if (propertyName.contains("in") || propertyName.contains("out")) {
                        // these are edge indicators. Ignore
                        continue;
                    }
                    jsonEdge.addProperty(propertyName, actionEdge.getProperty(propertyName).toString());
                }
                elements.add(new Element(Element.GROUP_EDGES, jsonEdge, className));
            }
        }
        return elements;
    }

    abstract class Document {

        // every document needs an id
        private String id;

        // a mapping of properties that the document contains
        private Map<String, String> properties;

        public Document(String id) {
            this.id = id;
            properties = new HashMap<>();
        }

        public void addProperty(String key, String value) {
            properties.put(key, value);
        }

        @JsonAnyGetter
        public Map<String, String> getProperties() {
            return properties;
        }

        @JsonGetter("id")
        public String getId() {
            return id;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Element {

        public static final String GROUP_NODES = "nodes";

        public static final String GROUP_EDGES = "edges";

        private String group;

        private Document document;

        private ArrayList<String> classes;

        public Element(String group, Document document) {
            this(group, document, null);
        }

        public Element(String group, Document document, String className) {
            this.group = group;
            this.document = document;
            if (className != null) {
                classes = new ArrayList<>();
                classes.add(className);
            }
        }

        @JsonGetter("group")
        public String getGroup() {
            return group;
        }

        @JsonGetter("data")
        public Document getDocument() {
            return document;
        }

        @JsonGetter("classes")
        public ArrayList<String> getClasses() {
            return classes;
        }

        public void addClass(String className) {
            if (classes == null) {
                classes = new ArrayList<>();
            }
            classes.add(className);
        }
    }
}

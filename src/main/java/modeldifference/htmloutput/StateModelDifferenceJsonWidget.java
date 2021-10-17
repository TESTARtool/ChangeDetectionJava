package modeldifference.htmloutput;


import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import modeldifference.models.AbstractState;
import modeldifference.orient.query.IWidgetTreeQuery;
import org.fruit.alayer.Tag;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class StateModelDifferenceJsonWidget implements IStateModelDifferenceJsonWidget {

    private final IWidgetTreeQuery widgetTreeQuery;

    public StateModelDifferenceJsonWidget(IWidgetTreeQuery widgetTreeQuery) {
        this.widgetTreeQuery = widgetTreeQuery;
    }

    /**
     * Given two Abstract States identifiers, the one that disappeared and the new that takes his place
     * extract the Widget Tree from both States and make a comparison to detect the differences.
     *
     * A List<String> of information about Widgets with changes will be returned.
     *
     * @param dissStateModelOne
     * @param newStateModelTwo
     */
    public List<String> jsonWidgetTreeDifference(Set<Tag<?>> abstractAttributesTags, Path location, AbstractState dissStateModelOne, AbstractState newStateModelTwo) {
        List<String> widgetTreeInformation = new ArrayList<>();

        // From the Abstract State that disappeared, extract the widget tree
        JsonArray jsonArrayDisappearedWidgetTree = extractJsonWidgetTreeFromAbstractState(location, dissStateModelOne);
        // From the new Abstract State, extract the widget tree
        JsonArray jsonArrayNewWidgetTree = extractJsonWidgetTreeFromAbstractState(location, newStateModelTwo);

        // Iterate over all disappeared widgets from the widget-tree
        for(int i = 0; i < jsonArrayDisappearedWidgetTree.size(); i++) {
            JsonElement disElement = jsonArrayDisappearedWidgetTree.get(i);
            if(jsonElementIsWidget(disElement)) {

                // Check if the Element that represents a Widget exists inside the New Widget Tree Array
                if(!checkIfJsonWidgetAbstracIdExistsInsideJsonWidgetTree(disElement, jsonArrayNewWidgetTree)) {
                    String widgetAbstractId = disElement.getAsJsonObject().getAsJsonObject("data").get("AbstractIDCustom").getAsString();
                    widgetTreeInformation.add("This Widget no loger exists in the new Model: " + widgetAbstractId);
                    widgetTreeInformation.add(extractAttributesFromWidgetJsonElement(abstractAttributesTags, disElement));
                }
            }
        }

        // Iterate over all new widgets from the widget-tree
        for(int j = 0; j < jsonArrayNewWidgetTree.size(); j++) {
            JsonElement newElement = jsonArrayNewWidgetTree.get(j);
            if(jsonElementIsWidget(newElement)) {

                // Check if the Element that represents a Widget exists inside the New Widget Tree Array
                if(!checkIfJsonWidgetAbstracIdExistsInsideJsonWidgetTree(newElement, jsonArrayDisappearedWidgetTree)) {
                    String widgetAbstractId = newElement.getAsJsonObject().getAsJsonObject("data").get("AbstractIDCustom").getAsString();
                    widgetTreeInformation.add("This Widget is completely new in the new Model: " + widgetAbstractId);
                    widgetTreeInformation.add(extractAttributesFromWidgetJsonElement(abstractAttributesTags, newElement));
                }
            }
        }

        return widgetTreeInformation;
    }

    /**
     * Using the desired the Abstract State Identifier,
     * get one of his Concrete State Id and fetch the widget tree as JSON
     */
    private JsonArray extractJsonWidgetTreeFromAbstractState(Path location, AbstractState abstractState) {

        var concreteState = abstractState.getConcreteStates().stream().findFirst();
        if (concreteState.isEmpty()) {
            return new JsonArray();
        }

        var jsonWidgetTree = widgetTreeQuery.query(location, concreteState.get().getId());

        try(FileReader readerWidgetTree = new FileReader(new File(jsonWidgetTree).getCanonicalFile())){
            return new JsonParser().parse(readerWidgetTree).getAsJsonArray();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return new JsonArray();
    }

    /**
     * Helper class to check if the JSON element is a node Widget
     * "classes": ["Widget"]
     *
     * And check that the JSON element is not a node State
     * data { "AbstractIDCustom": "SAC1he7ngze41331129227" }
     *
     * @param element
     * @return
     */
    private boolean jsonElementIsWidget(JsonElement element) {
        try {
            boolean isWidget = element.getAsJsonObject().getAsJsonArray("classes").get(0).getAsString().equals("Widget");

            // State is the root Widget, but ignore in this level of the difference report
            boolean isState = element.getAsJsonObject().getAsJsonObject("data").get("AbstractIDCustom").getAsString().contains("SAC");

            return (isWidget && !isState);
        } catch(Exception e) {e.printStackTrace();}
        return false;
    }

    /**
     * Check if one JSON Element that represents a Widget, exists inside one JSON Array that represents a Widget-Tree
     *
     * @param jsonWidget
     * @param jsonWidgetTree
     * @return
     */
    private boolean checkIfJsonWidgetAbstracIdExistsInsideJsonWidgetTree(JsonElement jsonWidget, JsonArray jsonWidgetTree) {
        // Iterate over all widget from the JSON Widget Tree
        for(int i = 0; i < jsonWidgetTree.size(); i++) {
            JsonElement widgetElement = jsonWidgetTree.get(i);
            // Check if the element it is a Widget class
            if(jsonElementIsWidget(widgetElement)) {
                // Check if both Widget Elements have the same AbstractIDCustom property
                if(compareWidgetJsonElementByAbstractId(jsonWidget, widgetElement)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if two JSON Elements that represents two Widgets, have the same AbstractIDCustom attribute
     *
     * @param disWidget
     * @param newWidget
     * @return
     */
    private boolean compareWidgetJsonElementByAbstractId(JsonElement disWidget, JsonElement newWidget) {
        try {
            String disWidgetAbstractIDCustom = disWidget.getAsJsonObject().getAsJsonObject("data").get("AbstractIDCustom").getAsString();
            String newWidgetAbstractIDCustom = newWidget.getAsJsonObject().getAsJsonObject("data").get("AbstractIDCustom").getAsString();

            return disWidgetAbstractIDCustom.equals(newWidgetAbstractIDCustom);
        } catch(Exception e) {}

        return false;
    }

    private String extractAttributesFromWidgetJsonElement(Set<Tag<?>> abstractAttributesTags, JsonElement jsonWidget) {
        StringBuilder attributes = new StringBuilder("");

        for(Tag<?> tag : abstractAttributesTags) {
            // Check that the Tag Attribute exists inside JSON data (Widget Tag property)
            if(jsonWidget.getAsJsonObject().getAsJsonObject("data").has(tag.name())) {
                try {
                    String tagValue = jsonWidget.getAsJsonObject().getAsJsonObject("data").get(tag.name()).getAsString();
                    attributes.append(tag.name() + ":" + tagValue + " ");
                } catch (Exception e) {}
            }
        }

        return attributes.toString();
    }
}

package modeldifference.htmloutput;

import com.google.common.collect.Sets;

import es.upv.staq.testar.StateManagementTags;
import modeldifference.IOutputDifferences;
import modeldifference.calculator.ApplicationDifferences;
import modeldifference.calculator.StateModelDifferenceImages;
import modeldifference.models.AbstractAction;
import modeldifference.models.AbstractState;

import org.fruit.Pair;
import org.fruit.alayer.Tag;
import org.fruit.alayer.webdriver.enums.WdMapping;
import org.fruit.alayer.windows.UIAMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class HtmlOutput implements IOutputDifferences {

    public final static String CHARSET = "UTF-8";
    private final UIAMapping uiaMapping;
    private final WdMapping wdMapping;
    private final StateManagementTags stateManagementTags;
    private final IStateModelDifferenceJsonWidget stateModelDifferenceJsonWidget;

    public HtmlOutput(UIAMapping uiaMapping, WdMapping wdMapping, StateManagementTags stateManagementTags, IStateModelDifferenceJsonWidget stateModelDifferenceJsonWidget){
        this.uiaMapping = uiaMapping;
        this.wdMapping = wdMapping;
        this.stateManagementTags = stateManagementTags;
        this.stateModelDifferenceJsonWidget = stateModelDifferenceJsonWidget;
    }

    private final boolean overwriteExistingRun = true;
    private final String outputDirectory  = "out";

    private void deleteDirectoryAndItContents(Path path){
        try(var files = Files.walk(path)){

            files.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::deleteOnExit);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String generateDifferenceName(ApplicationDifferences differences){
        return differences.getFirstVersion().getName() + "_" + differences.getFirstVersion().getVersion()
               + "_diff_" +
               differences.getSecondVersion().getName() + "_" + differences.getSecondVersion().getVersion();
    }

    public void output(ApplicationDifferences differences) {
        var diffName = generateDifferenceName(differences);

        var path = Paths.get(outputDirectory, diffName);

        if (Files.exists(path) && overwriteExistingRun){
            deleteDirectoryAndItContents(path);
        }

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            this.htmlFilename = outputDirectory + File.separator + diffName + File.separator + "DifferenceReport.html";

            this.out = new PrintWriter(new File(htmlFilename).getCanonicalPath(), CHARSET);

            for(String s : HEADER){
                out.println(s);
                out.flush();
            }

            addStateAndActionsToHtml(differences.getRemovedStates(), path);
            addStateAndActionsToHtml(differences.getAddedStates(),path);
            addImageOrWidgetTreeComparison(differences, path);

            closeHtmlReport();

        } catch (IOException e) {
            System.out.println("ERROR: Unable to start the State Model Difference Report : " + htmlFilename);
            e.printStackTrace();
        }
    }


    private Set<Tag<?>> abstractAttributesTags(ApplicationDifferences differences){

        // Update Set object "abstractAttributesTags" with the Tags
        // we need to check for Widget Tree difference
        var stateModelTags =  differences.getFirstVersion().getAttributes();

        var mangementTags = stateModelTags.stream()
                .map(this::capitalizeWordsAndRemoveSpaces)
                .map(x -> stateManagementTags.getTagFromSettingsString(x))
                .collect(Collectors.toSet());

        var tags = new HashSet<Tag<?>>();

        mangementTags.stream()
                .map(x -> uiaMapping.getMappedStateTag((Tag)x))
                .map(x -> (Tag<?>) x)
                .forEach(tags::add);

        mangementTags.stream()
                .map(x -> wdMapping.getMappedStateTag((Tag)x))
                .map(x -> (Tag<?>) x)
                .forEach(tags::add);

        return tags;
    }

    /**
     * Helper class to transform State Model String attribute
     * into a StateManagementTag Setting String
     *
     * From: widget control type
     * To: WidgetControlType
     *
     * @return
     */
    private String capitalizeWordsAndRemoveSpaces(String attribute) {
        String words[] = attribute.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder("");
        for(String w : words){
            String first = w.substring(0,1);
            String afterfirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase() + afterfirst);
        }
        return capitalizeWord.toString().trim();
    }

    private void addImageOrWidgetTreeComparison(ApplicationDifferences differences, Path path){
        // Image or Widget Tree comparison
        startSpecificStateChanges();

        var abstractAttributesTags = abstractAttributesTags(differences);
        var newAbstractStates = differences.getAddedStates();

        for(var newStateModelTwo :  newAbstractStates) {

            var incomingActionsModelTwo = newStateModelTwo.getIncomingActions();

            var two = new HashSet<Pair<String, String>>();

            for (var incommingAction: incomingActionsModelTwo){
                 two.add(new Pair<>(incommingAction.getId().getValue(), incommingAction.getDescription()));
            }

            var disappearedAbstractStates = differences.getRemovedStates();
            for(var dissStateModelOne :  disappearedAbstractStates) {

                var incomingActionsModelOne = dissStateModelOne.getIncomingActions();

                var one = new HashSet<Pair<String, String>>();

                for (var incommingAction: incomingActionsModelOne){
                    one.add(new Pair<>(incommingAction.getId().getValue(), incommingAction.getDescription()));
                }

             //   incomingActionsModelTwo.remove(new Pair<String, String>(null,""));

                var intersection = Sets.intersection(two, one);

                if (!intersection.isEmpty()){
                    // Create the Image Difference
                    var diffDisk = StateModelDifferenceImages.getDifferenceImage(
                            dissStateModelOne.getScreenshot(), dissStateModelOne.getId(),
                            newStateModelTwo.getScreenshot(), newStateModelTwo.getId(),
                            path);

                    if (diffDisk.isPresent()) {

                        var locationScreenshotOne = Paths.get(path.toString(), dissStateModelOne.getId().getValue() + ".png");
                        var locationScreenshotTwo = Paths.get(path.toString(), newStateModelTwo.getId().getValue() + ".png");

                        addSpecificStateChange(locationScreenshotOne, locationScreenshotTwo, diffDisk.get());
                    }

                    addSpecificActionReached(intersection.toString());

                    // Widget Tree Abstract Properties Difference
                    var widgetTreeDifference = stateModelDifferenceJsonWidget.jsonWidgetTreeDifference(abstractAttributesTags, path, dissStateModelOne, newStateModelTwo);

                    for(var widgetInformation : widgetTreeDifference) {
                        addSpecificWidgetInfo(widgetInformation);
                    }
                }

            }

        }
    }

    private void addStateAndActionsToHtml(List<AbstractState> states, Path outputLocation){
        startDisappearedAbstractStates(states.size());

        for (var state: states){
            addDisappearedAbstractState(state, outputLocation);

            for (var action : state.getOutgoingActions()) {
                addActionDescription(action);
            }

            addEndDiv();

        }
        addEndDiv();
    }


    private static final String[] HEADER = new String[] {
            "<!DOCTYPE html>",
            "<html>",
            "<style>",
            "   .container {display: flex;}",
            "   .float {display:inline-block;}",
            "</style>",
            "<head>",
            "   <title>TESTAR State Model difference report</title>",
            "</head>",
            "<body>"
    };

    private String htmlFilename = "DifferenceReport.html";
    private PrintWriter out;


    public void startDisappearedAbstractStates(int numberDisappearedAbstractStates) {
        out.println("<h2> Disappeared Abstract States: " + numberDisappearedAbstractStates + "</h2>");
        out.println("<div class=\"container\">");
        out.flush();
    }

    public void addDisappearedAbstractState(AbstractState abstractState, Path outputLocation) {
        // save the file to disk
        var screenshotPath = Paths.get(outputLocation.toString(), abstractState.getId().getValue() + ".png");
        var screenshotFile = new File(screenshotPath.toUri());

        if (!screenshotFile.exists()) {
            try {
                var outputStream = new FileOutputStream(screenshotFile.getCanonicalFile());
                outputStream.write(abstractState.getScreenshot());
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        out.println("<div class=<\"float\">");
        out.println("<p><img src=\"" + abstractState.getId().getValue() + ".png" + "\"></p>");
        out.println("<h4> Disappeared Actions of this State, Concrete Description </h4>");
        out.flush();
    }

    public void startNewAbstractStates(int numberNewAbstractStates) {
        out.println("<h2> New Abstract States: " + numberNewAbstractStates + "</h2>");
        out.println("<div class=\"container\">");
        out.flush();
    }

    public void addNewAbstractState(String newStateImage) {
        out.println("<div class=<\"float\">");
        out.println("<p><img src=\"" + newStateImage + "\"></p>");
        out.println("<h4> New Actions Discovered on this State, Concrete Description </h4>");
        out.flush();
    }

    public void addActionDescription(AbstractAction actionDesc) {
        out.println("<p style=\"color:red;\">" + actionDesc.getDescription() + "</p>");
        out.flush();
    }

    public void startSpecificStateChanges() {
        out.println("<h2> Specific State changes </h2>");
        out.flush();
    }

    public void addSpecificStateChange(Path oldStateImage, Path newStateImage, Path diffStateImage) {
        out.println("<p><img src=\"" + oldStateImage.getFileName() + "\">");
        out.println("<img src=\"" + newStateImage.getFileName() + "\">");
        out.println("<img src=\"" + diffStateImage.getFileName() + "\"></p>");
        out.flush();
    }

    public void addSpecificActionReached(String actionDesc) {
        out.println("<p style=\"color:blue;\">" + "We have reached this State with Action: " + actionDesc + "</p>");
        out.flush();
    }

    public void addSpecificWidgetInfo(String widgetInfo) {
        out.println("<p style=\"color:black;\">" + widgetInfo + "</p>");
        out.flush();
    }

    public void addEndDiv() {
        out.println("</div>");
        out.flush();
    }

    public void closeHtmlReport() {
        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();
    }
}

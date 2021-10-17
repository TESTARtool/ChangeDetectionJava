package modeldifference.htmloutput;

import com.google.common.collect.Sets;
import modeldifference.IOutputDifferences;
import modeldifference.calculator.ApplicationDifferences;
import modeldifference.calculator.StateModelDifferenceImages;
import modeldifference.models.AbstractAction;
import modeldifference.models.AbstractState;
import org.fruit.Pair;
import org.fruit.alayer.IStateManagementTags;
import org.fruit.alayer.IUIAMapping;
import org.fruit.alayer.IWdMapping;
import org.fruit.alayer.Tag;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class HtmlOutput implements IOutputDifferences {

    public final static String CHARSET = "UTF-8";
    private final IUIAMapping uiaMapping;
    private final IWdMapping wdMapping;
    private final IStateManagementTags stateManagementTags;
    private final IStateModelDifferenceJsonWidget stateModelDifferenceJsonWidget;

    public HtmlOutput(IUIAMapping uiaMapping, IWdMapping wdMapping, IStateManagementTags stateManagementTags, IStateModelDifferenceJsonWidget stateModelDifferenceJsonWidget){
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


    private  Set<Tag<?>> abstractAttributesTags(ApplicationDifferences differences){
        return null;
    }

    private void addImageOrWidgetTreeComparison(ApplicationDifferences differences, Path path){
        // Image or Widget Tree comparison
        startSpecificStateChanges();

        var abstractAttributesTags = abstractAttributesTags(differences);
        var newAbstractStates = differences.getAddedStates();

        for(var newStateModelTwo :  newAbstractStates) {

            var incomingActionsModelTwo = newStateModelTwo.getIncomingActions();

            // Set<Pair<String, String>> incomingActionsModelTwo = modelDifferenceDatabase.incomingActionsIdDesc(identifierModelTwo, newStateModelTwo);
            //       incomingActionsModelTwo.remove(new Pair<String, String>(null,""));


            var disappearedAbstractStates = differences.getRemovedStates();
            for(var dissStateModelOne :  disappearedAbstractStates) {

                var incomingActionsModelOne = dissStateModelOne.getIncomingActions();

                // Set<Pair<String, String>> incomingActionsModelOne = modelDifferenceDatabase.incomingActionsIdDesc(identifierModelOne, dissStateModelOne);
                // incomingActionsModelOne.remove(new Pair<String, String>(null,""));

                var intersection = Sets.intersection(incomingActionsModelTwo, incomingActionsModelOne);

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
        //var screenshotFile = new File(outputLocation + abstractState.getId().getValue() + ".png");

      //  System.out.println(screenshotFile.getCanonicalPath().toString());

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
        out.println("<p><img src=\"" + screenshotFile.getPath() + "\"></p>");
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
        out.println("<p><img src=\"" + oldStateImage.toString() + "\">");
        out.println("<img src=\"" + newStateImage.toString() + "\">");
        out.println("<img src=\"" + diffStateImage.toString() + "\"></p>");
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
        out.flush();
        out.close();
    }
}

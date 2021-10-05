package modeldifference.htmloutput;

import modeldifference.IOutputDifferences;
import modeldifference.calculator.ApplicationDifferences;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class HtmlOutputter implements IOutputDifferences {

    public final static String CHARSET = "UTF-8";

    public void output(ApplicationDifferences differences) {
        this.htmlFilename = "DifferenceReport.html";
        try {
            this.out = new PrintWriter(new File(htmlFilename).getCanonicalPath(), CHARSET);

            for(String s : HEADER){
                out.println(s);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Unable to start the State Model Difference Report : " + htmlFilename);
            e.printStackTrace();
        }
    }


    private static final String[] HEADER = new String[] {
            "<!DOCTYPE html>",
            "<html>",
            "<style>",
            ".container {display: flex;}",
            ".float {display:inline-block;}",
            "</style>",
            "<head>",
            "<title>TESTAR State Model difference report</title>",
            "</head>",
            "<body>"
    };

    private String htmlFilename = "DifferenceReport.html";
    private PrintWriter out;

    public String getHtmlFileName() {
        return htmlFilename;
    }

    public void startDisappearedAbstractStates(int numberDisappearedAbstractStates) {
        out.println("<h2> Disappeared Abstract States: " + numberDisappearedAbstractStates + "</h2>");
        out.println("<div class=\"container\">");
        out.flush();
    }

    public void addDisappearedAbstractState(String disStateImage) {
        out.println("<div class=<\"float\">");
        out.println("<p><img src=\"" + disStateImage + "\"></p>");
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

    public void addActionDesc(String actionDesc) {
        out.println("<p style=\"color:red;\">" + actionDesc + "</p>");
        out.flush();
    }

    public void startSpecificStateChanges() {
        out.println("<h2> Specific State changes </h2>");
        out.flush();
    }

    public void addSpecificStateChange(String oldStateImage, String newStateImage, String diffStateImage) {
        out.println("<p><img src=\"" + oldStateImage + "\">");
        out.println("<img src=\"" + newStateImage + "\">");
        out.println("<img src=\"" + diffStateImage + "\"></p>");
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

    public void closeHTMLreport() {
        out.flush();
        out.close();
    }
}

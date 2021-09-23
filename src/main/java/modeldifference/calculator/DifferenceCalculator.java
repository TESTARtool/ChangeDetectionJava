package modeldifference.calculator;

import modeldifference.models.AbstractStateId;
import modeldifference.models.Application;
import org.fruit.alayer.IStateManagementTags;
import org.fruit.alayer.IUIAMapping;
import org.fruit.alayer.IWdMapping;
import org.fruit.alayer.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DifferenceCalculator implements IDifferenceCalculator {

    public DifferenceCalculator(IStateManagementTags tags){//}, IUIAMapping uiMapping, IWdMapping wdMappings){
    }

    public ApplicationDifferences findApplicationDifferences(Application application1, Application application2) throws DifferenceCalculatorException {

        var isAbstractDifferent = !(application1.getAttributes().containsAll(application2.getAttributes())
                && application2.getAttributes().containsAll(application1.getAttributes()));

        if (isAbstractDifferent){
            // if Abstract Attributes are different, Abstract Layer is different and no sense to continue
            throw new AbstractAttributesNotTheSameException();
        }

        application1.getAbstractStateIds().forEach(x -> {});

        var removeState = application1.getAbstractStateIds().stream()
                .filter(x -> !application2.getAbstractStateIds().contains(x))
                .collect(Collectors.toSet());

        var addedState = application2.getAbstractStateIds().stream()
                .filter(x -> !application1.getAbstractStateIds().contains(x))
                .collect(Collectors.toSet());

        return new ApplicationDifferences(removeState,  addedState);

/*
        application1.getAbstractStateIds().forEach( abstractStateId -> {
            // Only if doesn't exists in the State Model Two
            if(!allAbstractStatesModelTwo.contains(abstractStateId)) {

                String screenshotPath = modelDifferenceDatabase.screenshotConcreteState(modelDifferenceDatabase.concreteStateId(abstractStateId), "disappearedState");
                disappearedStatesImages.put(abstractStateId, screenshotPath);

                disappearedActions.put(abstractStateId, modelDifferenceDatabase.outgoingActionIdDesc(identifierModelOne, abstractStateId));
            }

        });

        allAbstractStatesModelTwo.forEach( abstractStateId -> {
            // Only if doesn't exists in the State Model One
            if(!allAbstractStatesModelOne.contains(abstractStateId)) {

                String screenshotPath = modelDifferenceDatabase.screenshotConcreteState(modelDifferenceDatabase.concreteStateId(abstractStateId), "NewState");
                newStatesImages.put(abstractStateId, screenshotPath);

                newActions.put(abstractStateId, modelDifferenceDatabase.outgoingActionIdDesc(identifierModelTwo, abstractStateId));
            }
        */
    }

    public class AbstractAttributesNotTheSameException extends DifferenceCalculatorException{

    }


/*
    private Set<Tag<?>> checkStateModelAbstractAttributes(Application application ) {

        var stateManagementTags = application.getAttributes().stream()
                .map(x -> capitalizeWordsAndRemoveSpaces(x))
                .map(x -> tags.getTagFromSettingsString(x))
                .collect(Collectors.toSet());


        // Abstract Attributes used to create the Abstract Layer of the State Model
        var abstractAttributesTags = new HashSet<Tag<?>>();

        // Now check UIAWindows and WdWebriver Maps
        // To extract the specific API Tag attribute that matches with the State Management Tag
        // It could match for both:
        // StateManagementTag: Widget title matches with UIAWindows: UIAName
        // StateManagementTag: Widget title matches with WdTag: WebGenericTitle
        for(var stateManagementTag : stateManagementTags) {
            var windowsTag = uiMapping.getMappedStateTag(stateManagementTag);

            if (windowsTag.isPresent()){
                abstractAttributesTags.add(windowsTag.get());
            }

            var webdriverTag = wdMappings.getMappedStateTag(stateManagementTag);
            if (webdriverTag.isPresent()){
                abstractAttributesTags.add(webdriverTag.get());
            }
        }

        return abstractAttributesTags;
    }

    private String capitalizeWordsAndRemoveSpaces(String attribute) {
        String words[] = attribute.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder("");
        for(String w : words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase() + afterFirst);
        }

        return capitalizeWord.toString().trim();
    }
    */
}
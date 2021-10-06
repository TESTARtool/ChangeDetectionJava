package modeldifference.calculator;

import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import modeldifference.models.*;
import modeldifference.orient.IOrientDbFactory;
import modeldifference.orient.entity.ConcreteActionEntity;
import modeldifference.orient.entity.ConcreteStateEntity;
import modeldifference.orient.query.IAbstractActionEntityQuery;
import modeldifference.orient.query.IAbstractStateEntityQuery;
import modeldifference.orient.query.IConcreteActionEntityQuery;
import modeldifference.orient.query.IConcreteStateEntityQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class DifferenceCalculator implements IDifferenceCalculator {

    private final IOrientDbFactory orientDbFactory;
    private final IAbstractActionEntityQuery abstractActionEntityQuery;
    private final IAbstractStateEntityQuery stateEntityQuery;
    private final IConcreteActionEntityQuery concreteActionEntityQuery;
    private final IConcreteStateEntityQuery concreteStateEntityQuery;

    public DifferenceCalculator(IOrientDbFactory orientDbFactory, IAbstractActionEntityQuery abstractActionEntityQuery, IAbstractStateEntityQuery stateEntityQuery,
                                IConcreteActionEntityQuery concreteActionEntityQuery,
                                IConcreteStateEntityQuery concreteStateEntityQuery){
        this.orientDbFactory = orientDbFactory;
        this.abstractActionEntityQuery = abstractActionEntityQuery;//}, IUIAMapping uiMapping, IWdMapping wdMappings){
        this.stateEntityQuery = stateEntityQuery;
        this.concreteActionEntityQuery = concreteActionEntityQuery;
        this.concreteStateEntityQuery = concreteStateEntityQuery;
    }

    public ApplicationDifferences findApplicationDifferences(Application application1, Application application2) throws DifferenceCalculatorException {

        var isAbstractDifferent = !(application1.getAttributes().containsAll(application2.getAttributes())
                && application2.getAttributes().containsAll(application1.getAttributes()));

        if (isAbstractDifferent){
            // if Abstract Attributes are different, Abstract Layer is different and no sense to continue
            throw new AbstractAttributesNotTheSameException();
        }

        try (var orientDb = orientDbFactory.openDatabase()) {

            var removeStateEntities = application1.getAbstractStateIds().stream()
                    .filter(x -> !application2.getAbstractStateIds().contains(x))
                    .map(x -> stateEntityQuery.query(application1.getAbstractIdentifier(), x, orientDb))
                    .map(Optional::get)
                    .collect(Collectors.toList());

            var addedStateEntities = application2.getAbstractStateIds().stream()
                    .filter(x -> !application1.getAbstractStateIds().contains(x))
                    .map(x -> stateEntityQuery.query(application2.getAbstractIdentifier(), x, orientDb))
                    .map(Optional::get)
                    .collect(Collectors.toList());


            var removedStates = new ArrayList<AbstractState>();

            for (var disappearedState : removeStateEntities) {
                var concreteStateEntity = disappearedState.getConcreteStateIdsStateIds().stream()
                        .map(x -> concreteStateEntityQuery.query(x, orientDb))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                        .orElse(new ConcreteStateEntity(new ConcreteStateId(""), new ORecordBytes()));

                var abstractActions = disappearedState.getOutgoingActionIds().stream()
                        .map(x -> abstractActionEntityQuery.query(x, orientDb))
                        .flatMap(Optional::stream)
                        .collect(Collectors.toList());

                var actions = new ArrayList<AbstractAction>();

                for (var action : abstractActions) {
                    var concreteActionEntity = action.getConcreteActionIds().stream()
                            .map(x -> concreteActionEntityQuery.query(x, orientDb))
                            .flatMap(Collection::stream)
                            .findFirst()
                            .orElse(new ConcreteActionEntity(new ConcreteActionId(""), "TILT"));

                    actions.add(new AbstractAction(action.getId(), concreteActionEntity.getDescription()));
                }

                removedStates.add(new AbstractState(disappearedState.getId(), concreteStateEntity.getScreenshotBytes().toStream(), actions));
            }

            var addedStates = new ArrayList<AbstractState>();

            for (var addedState : addedStateEntities) {
                var concreteStateEntity = addedState.getConcreteStateIdsStateIds().stream()
                        .map(x -> concreteStateEntityQuery.query(x, orientDb))
                        .filter(Optional::isPresent)
                        .map((Optional::get))
                        .findFirst()
                        .orElse(new ConcreteStateEntity(new ConcreteStateId(""), new ORecordBytes()));

                var abstractActions = addedState.getOutgoingActionIds().stream()
                        .map(x -> abstractActionEntityQuery.query(x, orientDb))
                        .flatMap(Optional::stream)
                        .collect(Collectors.toList());

                var actions = new ArrayList<AbstractAction>();
                for (var action : abstractActions) {
                    var concreteActionEntity = action.getConcreteActionIds().stream()
                            .map(x -> concreteActionEntityQuery.query(x, orientDb))
                            .flatMap(Collection::stream)
                            .findFirst()
                            .orElse(new ConcreteActionEntity(new ConcreteActionId(""), "TILT"));
                    actions.add(new AbstractAction(action.getId(), concreteActionEntity.getDescription()));
                }

                addedStates.add(new AbstractState(addedState.getId(), concreteStateEntity.getScreenshotBytes().toStream(), actions));
            }

            return new ApplicationDifferences(application1, application2, removedStates, addedStates);
        }
        catch(Exception ex){
            System.out.println("error");
            ex.printStackTrace();
            throw new DifferenceCalculatorException(ex);
        }


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
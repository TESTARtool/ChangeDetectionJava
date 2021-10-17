package modeldifference.calculator;

import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import modeldifference.models.*;
import modeldifference.orient.IODatabaseSession;
import modeldifference.orient.IOrientDbFactory;
import modeldifference.orient.IOrientDbSetting;
import modeldifference.orient.entity.AbstractActionEntity;
import modeldifference.orient.entity.AbstractStateEntity;
import modeldifference.orient.entity.ConcreteActionEntity;
import modeldifference.orient.entity.ConcreteStateEntity;
import modeldifference.orient.query.IAbstractActionEntityQuery;
import modeldifference.orient.query.IAbstractStateEntityQuery;
import modeldifference.orient.query.IConcreteActionEntityQuery;
import modeldifference.orient.query.IConcreteStateEntityQuery;

import java.util.*;
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
        this.abstractActionEntityQuery = abstractActionEntityQuery;
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

        try{

            var removeStateEntities = application1.getAbstractStateIds().stream()
                    .filter(x -> !application2.getAbstractStateIds().contains(x))
                    .map(x -> stateEntityQuery.query(application1.getAbstractIdentifier(), x))
                    .map(Optional::get)
                    .collect(Collectors.toList());

            var addedStateEntities = application2.getAbstractStateIds().stream()
                    .filter(x -> !application1.getAbstractStateIds().contains(x))
                    .map(x -> stateEntityQuery.query(application2.getAbstractIdentifier(), x))
                    .map(Optional::get)
                    .collect(Collectors.toList());

            var removedStates = new ArrayList<AbstractState>();

            for (var disappearedState : removeStateEntities) {
                var concreteStates =getConcreteStates(disappearedState);
                var abstractOutgoingActions = getAbstractActionEntities(disappearedState);

                var outgoingActions = new HashSet<AbstractAction>();
                for (var action : abstractOutgoingActions) {
                    var concreteActionEntity = action.getConcreteActionIds().stream()
                            .map(x -> concreteActionEntityQuery.query(x))
                            .flatMap(Collection::stream)
                            .findFirst()
                            .orElse(new ConcreteActionEntity(new ConcreteActionId(""), "TILT"));

                    outgoingActions.add(new AbstractAction(action.getId(), concreteActionEntity.getDescription()));
                }

                var abstractIncommingActions = disappearedState.getIncomingActionIds().stream()
                        .map(x -> abstractActionEntityQuery.query(x))
                        .flatMap(Optional::stream)
                        .collect(Collectors.toList());

                var incommingActions = new HashSet<AbstractAction>();
                for (var action : abstractIncommingActions) {
                    var concreteActionEntity = action.getConcreteActionIds().stream()
                            .map(x -> concreteActionEntityQuery.query(x))
                            .flatMap(Collection::stream)
                            .findFirst()
                            .orElse(new ConcreteActionEntity(new ConcreteActionId(""), "TILT"));

                    incommingActions.add(new AbstractAction(action.getId(), concreteActionEntity.getDescription()));
                }

                removedStates.add(new AbstractState(disappearedState.getId(), concreteStates, outgoingActions, incommingActions));
            }

            var addedStates = new ArrayList<AbstractState>();

            for (var addedState : addedStateEntities) {
                var concreteStates =getConcreteStates(addedState);
                var abstractOutgoingActions = getAbstractActionEntities(addedState);

                var outgoingActions = new HashSet<AbstractAction>();
                for (var action : abstractOutgoingActions) {
                    var concreteActionEntity = action.getConcreteActionIds().stream()
                            .map(x -> concreteActionEntityQuery.query(x))
                            .flatMap(Collection::stream)
                            .findFirst()
                            .orElse(new ConcreteActionEntity(new ConcreteActionId(""), "TILT"));

                    outgoingActions.add(new AbstractAction(action.getId(), concreteActionEntity.getDescription()));
                }

                var abstractIncomingActions = addedState.getIncomingActionIds().stream()
                        .map(x -> abstractActionEntityQuery.query(x))
                        .flatMap(Optional::stream)
                        .collect(Collectors.toList());

                var incomingActions = new HashSet<AbstractAction>();
                for (var action : abstractIncomingActions) {
                    var concreteActionEntity = action.getConcreteActionIds().stream()
                            .map(x -> concreteActionEntityQuery.query(x))
                            .flatMap(Collection::stream)
                            .findFirst()
                            .orElse(new ConcreteActionEntity(new ConcreteActionId(""), "TILT"));

                    outgoingActions.add(new AbstractAction(action.getId(), concreteActionEntity.getDescription()));
                }

                addedStates.add(new AbstractState(addedState.getId(), concreteStates, outgoingActions, incomingActions));
            }

            return new ApplicationDifferences(application1, application2, removedStates, addedStates);
        }
        catch(Exception ex){
            System.out.println("error");
            ex.printStackTrace();
            throw new DifferenceCalculatorException(ex);
        }


    }

    private Set<ConcreteState> getConcreteStates(AbstractStateEntity abstractState){
        return abstractState.getConcreteStateIdsStateIds().stream()
                .map(x -> concreteStateEntityQuery.query(x))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(x -> new ConcreteState(x.getId(), x.getScreenshotBytes().toStream()))
                .collect(Collectors.toSet());
    }

    private List<AbstractActionEntity> getAbstractActionEntities(AbstractStateEntity abstractState){
        return abstractState.getOutgoingActionIds().stream()
                .map(x -> abstractActionEntityQuery.query(x))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}
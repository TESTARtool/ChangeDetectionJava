package modeldifference.orient;

import modeldifference.IModelApplicationBuilder;
import modeldifference.models.*;
import modeldifference.orient.query.IAbstractStateEntityQuery;
import modeldifference.orient.query.IAbstractStateModelEntityQuery;

import java.util.*;

public class OrientDbApplicationBuilder implements IModelApplicationBuilder {

    private final IAbstractStateModelEntityQuery abstractStateModelQuery;
    private final IAbstractStateEntityQuery abstractStateEntityQuery;

    public OrientDbApplicationBuilder(IAbstractStateModelEntityQuery abstractStateModelQuery, IAbstractStateEntityQuery abstractStateEntityQuery){
        this.abstractStateModelQuery = abstractStateModelQuery;
        this.abstractStateEntityQuery = abstractStateEntityQuery;
    }

    public Optional<Application> getApplication(String applicationName, String version) {

        try{

            var entity = abstractStateModelQuery.query(applicationName, version);

            if (entity.isEmpty()){
                return Optional.empty();
            }

            var application = new Application(applicationName, version)
                    .setModelIdentifier(entity.get().getModelIdentifier());

            entity.get().getAbstractionAttributes()
                    .forEach(application::addAbstractAttribute);

            var states = abstractStateEntityQuery.query(application.getAbstractIdentifier());
            states.stream()
                    .forEach(x -> application.addAbstractStateId(x.getId()));

            return Optional.of(application);
        }
        catch(Exception ex){
            System.out.println("error");
        }

        return Optional.empty();
    }
}

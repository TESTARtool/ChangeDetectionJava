package modeldifference.orient;

import modeldifference.IApplicationBuilder;
import modeldifference.models.*;
import com.orientechnologies.orient.core.record.ODirection;
import modeldifference.orient.query.IAbstractStateEntityQuery;
import modeldifference.orient.query.IAbstractStateModelEntityQuery;

import java.util.*;
import java.util.stream.Collectors;

public class OrientDbApplicationBuilder implements IApplicationBuilder {

    private final IAbstractStateModelEntityQuery abstractStateModelQuery;
    private final IAbstractStateEntityQuery abstractStateEntityQuery;

    public OrientDbApplicationBuilder(IAbstractStateModelEntityQuery abstractStateModelQuery, IAbstractStateEntityQuery abstractStateEntityQuery){
        this.abstractStateModelQuery = abstractStateModelQuery;
        this.abstractStateEntityQuery = abstractStateEntityQuery;
    }

    public Optional<Application> getApplication(String applicationName, int version) {

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

package tryouts;

import application.IApplication;
import com.orientechnologies.orient.core.db.record.ridbag.ORidBag;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import modeldifference.IModelApplicationBuilder;
import modeldifference.models.ConcreteStateId;
import modeldifference.orient.IOrientDbFactory;
import modeldifference.orient.OrientDbCommand;
import modeldifference.orient.query.ConcreteActionEntityQuery;
import modeldifference.orient.query.IAbstractStateEntityQuery;
import modeldifference.orient.query.IConcreteStateEntityQuery;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TryOutApplication implements IApplication {

    private final IModelApplicationBuilder applicationBuilder;
    private final IAbstractStateEntityQuery abstractStateEntityQuery;
    private final IConcreteStateEntityQuery concreteStateEntityQuery;
    private final IOrientDbFactory orientDbFactory;

    public TryOutApplication(IModelApplicationBuilder applicationBuilder, IAbstractStateEntityQuery abstractStateEntityQuery, IConcreteStateEntityQuery concreteStateEntityQuery, IOrientDbFactory orientDbFactory){
        this.applicationBuilder = applicationBuilder;
        this.abstractStateEntityQuery = abstractStateEntityQuery;
        this.concreteStateEntityQuery = concreteStateEntityQuery;
        this.orientDbFactory = orientDbFactory;
    }

    public void Run() throws Exception {

        var applicationName1 = "wpfApp";
        var applicationVersion1 = "2";
        var application1 = applicationBuilder.getApplication(applicationName1, applicationVersion1);

        Logger.getLogger("TRYOUT").log(Level.INFO, application1.get().getName());

        var stateIds = application1.get().getAbstractStateIds();

        for (var stateId: stateIds) {

            var initAbstractState =  abstractStateEntityQuery.query(application1.get().getAbstractIdentifier(), stateId)
                    .stream().filter(x -> x.getIsInitial())
                    .findFirst();

            if (initAbstractState.isPresent()){

                var state = initAbstractState.get();

                System.out.println(state.getId().getValue() + " IsInitial: " + state.getIsInitial());

                for (var concreteId : state.getConcreteStateIdsStateIds()){

                    var concreteStateOptional = concreteStateEntityQuery.query(concreteId);

                    if (concreteStateOptional.isPresent()){
                        var concreteState = concreteStateOptional.get();
                        System.out.println(" # ConcreteId: " + concreteState.getId().getValue());

                        // getWidget(concreteId);
                        getWidgetTree(concreteId);
                    }
                }
            }
        }

    }

    private void getWidget(ConcreteStateId id){
        var sql = "SELECT FROM Widget WHERE ConcreteIDCustom = :concreteId";
        var command = new OrientDbCommand(sql)
                .addParameter("concreteId", id);

        try (var resultSet = command.executeReader(orientDbFactory)){



            resultSet.edgeStream().forEach(this::dumpAllProperties);
        }
    }

    private void dumpAllProperties(OEdge x){

        var propertyNames = x.getPropertyNames();
        for (var propertyName:propertyNames) {
            var value = x.getProperty(propertyName);
            System.out.println("     - EDGE   " + propertyName + " : " + value);

        }

    }

    private void dumpAllProperties(OResult x){
        var propertyNames = x.getPropertyNames();
        for (var propertyName:propertyNames) {
            var value = x.getProperty(propertyName);
            System.out.println("     - RESULT " + propertyName + " : " + value);

        }
    }

    private void dumpAllProperties(OVertex x){

        var propertyNames = x.getPropertyNames();
        for (var propertyName:propertyNames) {
            var value = x.getProperty(propertyName);
            System.out.println("     - VERTEX " + propertyName + " : " + value);

        }
    }

    private void dumpResultSet(OResultSet resultSet){

        var edges = new HashMap<ORID, WidgetEdge>();

        var widgets = new ArrayList<WidgetVertex>();



        resultSet.stream()
            .forEach(x -> {

                System.out.println("------ getIdentity.ToString() : " + x.getIdentity().get().toString());

                var out_isChildOf = (ORidBag) x.getProperty("in_isChildOf");
                var in_isChildOf = (ORidBag)x.getProperty("out_isChildOf");
                var role = (String)x.getProperty("Role");
                var title = (String)x.getProperty("Title");



                var widgetVertex = new WidgetVertex(title, role);

                if (in_isChildOf != null) {
                    for (var id : in_isChildOf) {
                        if (!edges.containsKey(id)) {
                            edges.put(id.getIdentity(), new WidgetEdge(id.getIdentity()));
                        }

                        var edge = edges.get(id.getIdentity());
                        edge.add(widgetVertex);

                        widgetVertex.addIn(edge);

                    }
                }

                if (out_isChildOf != null) {
                    for (var id : out_isChildOf) {
                        if (!edges.containsKey(id.getIdentity())) {
                            edges.put(id.getIdentity(), new WidgetEdge(id.getIdentity()));
                        }

                        var edge = edges.get(id.getIdentity());
                      //  edge.add(widgetVertex);
                        widgetVertex.addOut(edge);
                    }
                }

                widgets.add(widgetVertex);

                /*
                var propertiesToPrint = new ArrayList<String>();
                propertiesToPrint.add("in_isChildOf");
                propertiesToPrint.add("out_isChildOf");
                propertiesToPrint.add("Role");
                propertiesToPrint.add("Title");

                var propertyNames = x.getPropertyNames();
                for (var propertyName : propertyNames){

                    if (propertiesToPrint.contains(propertyName)) {
                        var value = x.getProperty(propertyName);
                        System.out.println("     - PROP " + propertyName + " : " + value);
                    }
              }
*/

                //var edge = x.getEdge();
                //if (edge.isPresent()){
                  //  dumpAllProperties(edge.get());
                //}

               // var vertex = x.getVertex();
                //if (vertex.isPresent()){
                //    dumpAllProperties(vertex.get());
                //}

    //            dumpAllProperties(x);

                System.out.println("--------------------------------");
            });

        widgets.stream().forEach(x -> x.Calculate());

        var roots = widgets.stream().filter(x -> x.isRoot());

        // should only have one!



    }

    private void getWidgetTree(ConcreteStateId id){
        var sql = "SELECT FROM (TRAVERSE IN('isChildOf') FROM (SELECT FROM Widget WHERE ConcreteIDCustom = :concreteId))";
        var command = new OrientDbCommand(sql)
                .addParameter("concreteId", id);

        try(var resultSet = command.executeReader(orientDbFactory)){

            dumpResultSet(resultSet);
            // resultSet.vertexStream().forEach(this::dumpAllProperties);


        }

    }

    private class WidgetEdge{
        private ORID orid;
        private ArrayList<WidgetVertex> widgets = new ArrayList<>();

        public WidgetEdge(ORID orid){
            this.orid = orid;
        }

        public void add(WidgetVertex widgetVertex){
            widgets.add(widgetVertex);
        }
    }

    private class WidgetVertex {
        private String title;
        private String role;

        private ArrayList<WidgetEdge> ins = new ArrayList<>();
        private ArrayList<WidgetEdge> outs = new ArrayList<>();

        private boolean isRoot;
        private boolean isLeaf;
        private ArrayList<WidgetVertex> childeren = new ArrayList<>();

        public WidgetVertex(String title, String role){
            this.title =title;
            this.role = role;
        }

        public void addIn(WidgetEdge edge){
            ins.add(edge);
        }

        public void addOut(WidgetEdge edge){
            outs.add(edge);
        }

        public boolean isRoot(){
            return isRoot;
        }

        public void Calculate(){
            isLeaf = !ins.isEmpty() && outs.isEmpty();
            isRoot = ins.isEmpty() && !outs.isEmpty();

            for (var out: outs){
                childeren.addAll(out.widgets);
            }

        }
    }
}

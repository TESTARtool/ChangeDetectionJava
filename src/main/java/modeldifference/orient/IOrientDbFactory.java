package modeldifference.orient;

public interface IOrientDbFactory {
    IODatabaseSession openDatabase();
}

package modeldifference.orient;

import com.orientechnologies.orient.core.db.ODatabaseSession;

public interface IOrientDbFactory {
    public ODatabaseSession openDatabase(IOrientDbSetting setting);
}

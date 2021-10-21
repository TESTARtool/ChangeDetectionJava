package application.settings;

public interface ISettingsFor<T> extends ISettingsForInternal {
    T getValue();
    boolean isValid();
}



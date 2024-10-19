package com.hundirlaflota.Client.Utils;

import java.util.ArrayList;
import java.util.List;

abstract class PropertyEvent<T> {
}

class PropertyChangeEvent<T> extends PropertyEvent<T> {
    private T oldValue;
    private T newValue;

    public PropertyChangeEvent(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}

interface PropertyChangeListener<T> {
    void propertyChange(PropertyChangeEvent<T> event);
}

public class Observable<T> {
    private List<PropertyChangeListener<T>> listeners;
    private T value;

    public Observable(T value) {
        this.listeners = new ArrayList<>();
        this.value = value;
    }

    public void addPropertyChangeListener(PropertyChangeListener<T> listener) {
        this.listeners.add(listener);
    }
    

    public T get() {
        return value;
    }

    public void set(T value) {
        T oldValue = this.value;
        this.value = value;
        for (PropertyChangeListener<T> listener : listeners) {
            listener.propertyChange(new PropertyChangeEvent<T>(oldValue, value));
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

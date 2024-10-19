package com.hundirlaflota.Client.Utils.Observable;

import java.util.Collection;

public class CollectionRemoveEvent<T> extends PropertyEvent<T> {
    private Collection<T> oldCollection;
    private Collection<T> newCollection;
    private T removedValue;

    public CollectionRemoveEvent(Collection<T> oldCollection, Collection<T> newCollection, T removedValue) {
        this.oldCollection = oldCollection;
        this.newCollection = newCollection;
        this.removedValue = removedValue;
    }

    public Collection<T> getOldCollection() {
        return oldCollection;
    }

    public Collection<T> getNewCollection() {
        return newCollection;
    }
    
    public T getRemovedValue() {
        return removedValue;
    }
}
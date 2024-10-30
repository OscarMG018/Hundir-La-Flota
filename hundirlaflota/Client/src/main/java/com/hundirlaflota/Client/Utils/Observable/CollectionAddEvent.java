package com.hundirlaflota.Client.Utils.Observable;

import java.util.Collection;

public class CollectionAddEvent<T> extends PropertyEvent<T> {
    private Collection<T> oldCollection;
    private Collection<T> newCollection;
    private T addedValue;

    public CollectionAddEvent(Collection<T> oldCollection, Collection<T> newCollection, T addedValue) {
        this.oldCollection = oldCollection;
        this.newCollection = newCollection;
        this.addedValue = addedValue;
    }

    public Collection<T> getOldCollection() {
        return oldCollection;
    }

    public Collection<T> getNewCollection() {
        return newCollection;
    }
    
    public T getAddedValue() {
        return addedValue;
    }
}
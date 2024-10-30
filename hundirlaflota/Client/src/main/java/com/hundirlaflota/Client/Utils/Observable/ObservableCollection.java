package com.hundirlaflota.Client.Utils.Observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.Collection;


public class ObservableCollection<T> extends Observable<Collection<T>> implements Iterable<T> {
    private List<CollectionAddListener<T>> addListeners;
    private List<CollectionRemoveListener<T>> removeListeners;

    public ObservableCollection(Collection<T> value) {
        super(value);
        this.addListeners = new ArrayList<>();
        this.removeListeners = new ArrayList<>();
    }

    public void addCollectionAddListener(CollectionAddListener<T> listener) {
        this.addListeners.add(listener);
    }

    public void addCollectionRemoveListener(CollectionRemoveListener<T> listener) {
        this.removeListeners.add(listener);
    }

    public void add(T value) {
        Collection<T> oldCollection = this.get();
        Collection<T> newCollection = new ArrayList<>(oldCollection);
        newCollection.add(value);
        this.set(newCollection);
        for (CollectionAddListener<T> listener : addListeners) {
            listener.onCollectionAdd(new CollectionAddEvent<T>(oldCollection, newCollection, value));
        }
    }

    public void remove(T value) {
        Collection<T> oldCollection = this.get();
        Collection<T> newCollection = new ArrayList<>(oldCollection);
        newCollection.remove(value);
        this.set(newCollection);
        for (CollectionRemoveListener<T> listener : removeListeners) {
            listener.onCollectionRemove(new CollectionRemoveEvent<T>(oldCollection, newCollection, value));
        }
    }

    public void remove(int index) {
        Collection<T> oldCollection = this.get();
        T removedValue = oldCollection.stream().skip(index).findFirst().orElse(null);
        oldCollection.remove(index);
        this.set(oldCollection);
        for (CollectionRemoveListener<T> listener : removeListeners) {
            listener.onCollectionRemove(new CollectionRemoveEvent<T>(oldCollection, oldCollection, removedValue));
        }
    }

    @Override
    public String toString() {
        return get().toString();
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }

    public Stream<T> stream() {
        return get().stream();
    }
}

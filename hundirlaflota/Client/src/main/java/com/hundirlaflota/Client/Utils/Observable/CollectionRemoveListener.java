package com.hundirlaflota.Client.Utils.Observable;

public interface CollectionRemoveListener<T> {
    void onCollectionRemove(CollectionRemoveEvent<T> event);
}
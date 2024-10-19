package com.hundirlaflota.Client.Utils.Observable;

public interface CollectionAddListener<T> {
    void onCollectionAdd(CollectionAddEvent<T> event);
}
package Neu.Network.model.dao;

import java.io.IOException;

public interface Dao<T> extends AutoCloseable {

    T read(String name) throws IOException, ClassNotFoundException;
    void write(String name,T obj) throws IOException;
}
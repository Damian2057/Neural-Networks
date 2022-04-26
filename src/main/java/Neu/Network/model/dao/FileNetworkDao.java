package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;
import Neu.Network.model.exceptions.model.LogicException;
import java.io.*;

public class FileNetworkDao<T> implements Dao<T> {

    private final String path = "@../../archives/";

    @Override
    public T read(String name) {
        T temp = null;
        try (FileInputStream inputFileStream = new FileInputStream(path+name);
             ObjectInputStream in = new ObjectInputStream(inputFileStream)) {
            temp = (T)in.readObject();
            return temp;
        } catch (Exception e) {
            throw new FileOperationException("Network read error");
        }
    }

    @Override
    public void write(String name, T obj) {
        try (FileOutputStream outputFileStream = new FileOutputStream(path+name);
             ObjectOutputStream out = new ObjectOutputStream(outputFileStream)) {
            out.writeObject(obj);
        } catch (IOException exception) {
            throw new FileOperationException("Network write error");
        }
    }

    public void readNamesOfFilesInDirectory() {
        File folder = new File("@../../archives/");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        if(listOfFiles.length == 0) {
            throw new LogicException("No previously saved object was found.");
        }
        System.out.println("Select a saved network: ");
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println("- "+file.getName());
            }
        }
    }

    @Override
    public void close() {
    }
}

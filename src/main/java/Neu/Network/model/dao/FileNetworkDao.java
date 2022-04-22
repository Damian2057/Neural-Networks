package Neu.Network.model.dao;

import java.io.*;

public class FileNetworkDao<T> implements Dao<T> {

    private final String path = "@../../archives/";

    @Override
    public T read(String name) throws IOException, ClassNotFoundException {
        T temp = null;
        try (FileInputStream inputFileStream = new FileInputStream(name);
             ObjectInputStream in = new ObjectInputStream(inputFileStream)) {
            temp = (T)in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
        return (T) temp;
    }

    @Override
    public void write(String name, T obj) throws IOException {
        try (FileOutputStream outputFileStream = new FileOutputStream(path+name);
             ObjectOutputStream out = new ObjectOutputStream(outputFileStream)) {
            out.writeObject(obj);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Error");
        }
    }

    public void readNamesOfFilesInDirectory() {
        File folder = new File("@../../archives/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

    @Override
    public void close() throws Exception {

    }
}

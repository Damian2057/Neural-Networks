package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;

import java.io.File;

public class DirectoryManager {

    public static void ClearDirectories() {
        ClearStats("@../../PythonCharts/statistics/");
        ClearStats("@../../Data/SelectedData");
    }


    public static void ClearStats(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    public static void CreateDirectories() {
        try {
            File theData = new File("@../../Data");
            if (!theData.exists()){
                theData.mkdirs();
            }
            File theArchives = new File("@../../archives");
            if (!theArchives.exists()){
                theArchives.mkdirs();
            }
            File theStats = new File("@../../PythonCharts/statistics");
            if (!theStats.exists()){
                theStats.mkdirs();
            }
            File theSelectedData = new File("@../../Data/SelectedData");
            if (!theSelectedData.exists()){
                theSelectedData.mkdirs();
            }
        } catch (Exception e) {
            throw new FileOperationException("error while creating paths");
        }
    }
}

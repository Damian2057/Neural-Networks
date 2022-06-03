package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;
import java.io.File;

public class DirectoryManager {

    public static void ClearDirectories() {
        ClearStats("@../../PythonCharts/statistics/");
        ClearStats("@../../Data/SelectedData");
        ClearStats("@../../outputData");
    }

    public static void CreateDirectories() {
        try {
            CreateSingleDirectory("@../../Data");
            CreateSingleDirectory("@../../archives");
            CreateSingleDirectory("@../../PythonCharts/statistics");
            CreateSingleDirectory("@../../Data/SelectedData");
            CreateSingleDirectory("@../../outputData");
        } catch (Exception e) {
            throw new FileOperationException("error during creating paths");
        }
    }

    private static void ClearStats(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    private static void CreateSingleDirectory(String path) {
        try {
            File theData = new File(path);
            if (!theData.exists()){
                theData.mkdirs();
            }
        } catch (Exception e) {
            throw new FileOperationException("error during creating paths");
        }
    }

}

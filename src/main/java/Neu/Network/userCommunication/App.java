package Neu.Network.userCommunication;

import Neu.Network.model.Structure;
import Neu.Network.model.dao.DataReader;
import Neu.Network.model.dao.FileNetworkDao;
import Neu.Network.model.flower.Irys;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);

        //Upload data
        ArrayList<Irys> data;
        try {
            data = DataReader.readData();
        } catch (Exception e) {
            System.out.println("Error occurred");
            return;
        }

        System.out.println("""
                Select an operating mode:
                [1]. Learning mode
                [2]. Test mode""");
        int choice = Integer.parseInt(scanner.nextLine());
        System.out.println("""
                Network options:
                [1]. Create a new network
                [2]. Load the saved network""");
        int networkChoice = Integer.parseInt(scanner.nextLine());

        Structure structure;
        switch (networkChoice) {
            case 1 -> {
                structure = new Structure(data);
            }
            case 2 -> {
                try(FileNetworkDao<Structure> fileManager = new FileNetworkDao<>()) {
                    String selectedFile = "";
                    System.out.println("Select a saved network");
                    fileManager.readNamesOfFilesInDirectory();
                    selectedFile = scanner.nextLine();
                    structure = fileManager.read(selectedFile);
                } catch (Exception ignored) {
                }
            }
            default -> {
                System.out.println("Invalid option");
                return;
            }
        }

        switch (choice) {
            case 1 -> {

            }
            case 2 -> {

            }
            default -> {
                System.out.println("Invalid option");
            }
        }

    }
}

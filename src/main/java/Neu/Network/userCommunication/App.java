package Neu.Network.userCommunication;

import Neu.Network.model.Structure;
import Neu.Network.model.dao.FileNetworkDao;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);

        String operationMessage = """
                Select an operating mode:
                [1]. Learning mode
                [2]. Test mode""";
        System.out.println(operationMessage);
        int choice = Integer.parseInt(scanner.nextLine());
        String networkMessage = """
                Network options:
                [1]. Create a new network
                [2]. Load the saved network""";
        System.out.println(networkMessage);
        int networkChoice = Integer.parseInt(scanner.nextLine());

        Structure structure;
        switch (networkChoice) {
            case 1 -> {
                structure = new Structure();
            }
            case 2 -> {
                try(FileNetworkDao<Structure> fileManager = new FileNetworkDao<>()) {
                    String selectedFile = "";
                    System.out.println("Select a saved network");
                    fileManager.readNamesOfFilesInDirectory();
                    selectedFile = scanner.nextLine();
                    structure = fileManager.read(selectedFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            default -> {
                System.out.println("Invalid option");
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

package Neu.Network.userCommunication;

import Neu.Network.model.component.NeuralNetwork;
import Neu.Network.model.dao.DataReader;
import Neu.Network.model.dao.FileNetworkDao;
import Neu.Network.model.exceptions.model.LogicException;
import Neu.Network.model.flower.Irys;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);

        ArrayList<Irys> data;
        try { //Upload data
            data = DataReader.readData();
            System.out.println("Collected "+data.size()+" portions of data.\n");
        } catch (Exception e) {
            System.out.println("Error occurred");
            return;
        }

        System.out.println("""
                Network options:
                [1]. Create a new network
                [2]. Load the saved network""");
        int networkChoice = Integer.parseInt(scanner.nextLine());

        NeuralNetwork neuralNetwork = null;
        switch (networkChoice) {
            case 1 -> {
                System.out.println("Enter learning factor:");
                double learningFactor = Double.parseDouble(scanner.nextLine());
                System.out.println("Enter the momentum factor:");
                double momentumFactor = Double.parseDouble(scanner.nextLine());
                neuralNetwork = new NeuralNetwork(learningFactor,momentumFactor);
            }
            case 2 -> {
                try(FileNetworkDao<NeuralNetwork> fileManager = new FileNetworkDao<>()) {
                    String selectedFile;
                    fileManager.readNamesOfFilesInDirectory();
                    selectedFile = scanner.nextLine();
                    neuralNetwork = fileManager.read(selectedFile);
                    System.out.println("Network loaded!\nlearning factor: "
                            + neuralNetwork.getLearningFactor()+"\nmomentum factor: "
                            + neuralNetwork.getMomentumFactor()+"\n");
                } catch (LogicException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception ignored) {
                }
            }
            default -> {
                System.out.println("Invalid option");
                return;
            }
        }

        while (true) {
            System.out.println("""
                Select an operating mode:
                [1]. Learning mode.
                [2]. Test mode.
                [3]. Exit.""");
            int modeChoice = Integer.parseInt(scanner.nextLine());

            switch (modeChoice) {
                case 1 -> {
                    //Operation
                }
                case 2 -> {
                    //Operation
                }
                case 3 -> {
                    return;
                }
                default -> {
                    System.out.println("Invalid option.");
                    return;
                }
            }

            System.out.println("""
                Do you want to save the network to a file?:
                Yes/No""");
            String saveChoice = scanner.nextLine();
            switch (saveChoice) {
                case "No" -> {

                }
                case "Yes" -> {
                    String fileName;
                    System.out.println("Enter a name for the saved network:");
                    fileName = scanner.nextLine();
                    try(FileNetworkDao<NeuralNetwork> fileManager = new FileNetworkDao<>()) {
                        fileManager.write(fileName, neuralNetwork);
                        System.out.println("Saved!");
                    } catch (Exception e) {
                        System.out.println("Error occurred");
                    }
                }
                default -> {
                    System.out.println("Invalid option, end of the program");
                    return;
                }
            }
        }
    }
}

package Neu.Network.userCommunication;

import Neu.Network.model.dao.*;
import Neu.Network.model.exceptions.argument.ArgumentException;
import Neu.Network.summary.SummaryCalculator;
import Neu.Network.model.components.NeuralNetwork;
import Neu.Network.model.flower.Iris;
import java.util.*;

public class App {
    public static void main(String[] args) {

        DirectoryManager.CreateDirectories();
        if(Json.GetDeleteMode()) {
            DirectoryManager.ClearDirectories();
        }

        Scanner scanner= new Scanner(System.in);
        ArrayList<Iris> data;
        ArrayList<Iris> trainingData;

        try { //Download data
            data = DataOperation.readData("data.csv");
            trainingData = DataOperation.readData("trainingPartOfData.csv");
            System.out.println("Collected " + trainingData.size() + " portions of data to train.\n");
            System.out.println("Collected " + data.size() + " portions of data.\n");
        } catch (Exception e) {
            System.out.println("Error occurred");
            return;
        }

        if(Json.getCreateNewNetworkMode() && Json.getLoadPrevNetworkMode()) {
            throw new ArgumentException("invalid data in the config file");
        }

        NeuralNetwork neuralNetwork;
            if(Json.getCreateNewNetworkMode()) {
                neuralNetwork = new NeuralNetwork(Json.getNumberOfInPuts()
                        ,Json.getNumberOfHiddenNeurons()
                        ,Json.getNumberOfOutPuts()
                        , Json.getLearningFactor());
                neuralNetwork.setBias(Json.getBiasMode());
            } else {
                try(FileNetworkDao<NeuralNetwork> fileManager = new FileNetworkDao<>()) {
                    String selectedFile;
                    fileManager.readNamesOfFilesInDirectory();
                    selectedFile = scanner.nextLine();
                    neuralNetwork = fileManager.read(selectedFile);
                    System.out.println("Network loaded!");
                    neuralNetwork.showInformation();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }

        while (true) {
            System.out.println("""
                Select an operating mode:
                [1]. Learning mode.
                [2]. Test mode.
                [3]. Network Information.
                [4]. Exit.""");
            int modeChoice = Integer.parseInt(scanner.nextLine());

            switch (modeChoice) {
                case 1 -> {
                    System.out.println("Select the options based on which you want to create the network:\n");
                    System.out.println("Stop condition:\n[1]. number of epochs\n[2]. error level");
                    String stopCondition = scanner.nextLine();
                    switch (stopCondition) {
                        case "1" -> {
                            neuralNetwork.setStopConditionFlag(true);
                            neuralNetwork.setEpochs(Json.getNumberOfEpochs());
                        }
                        case "2" -> {
                            neuralNetwork.setStopConditionFlag(false);
                            neuralNetwork.setAccuracy(Json.getAccuracy());
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + stopCondition);
                    }

                    if(Json.getMomentumMode()) {
                        neuralNetwork.setMomentumFactor(Json.getMomentumValue());
                    }

                    System.out.println("Enter the method of entering the data:\n[1]. Random\n[2]. Sequentially");
                    String enterChoice = scanner.nextLine();
                    switch (enterChoice) {
                        case "1" -> neuralNetwork.setTypeOfSequence(true);
                        case "2" -> neuralNetwork.setTypeOfSequence(false);
                        default -> {
                            System.out.println("Invalid option.");
                            return;
                        }
                    }
                    neuralNetwork.showInformation();
                    neuralNetwork.trainNetwork(trainingData);
                }
                case 2 -> {
                    SummaryCalculator logicCalculator = new SummaryCalculator();
                    for (var sample : data) {
                        ArrayList<Double> result = neuralNetwork.calculate(sample);
                        logicCalculator.summarize(result,sample);
                    }
                    logicCalculator.summarizeOfAllTypes();
                }
                case 3 -> neuralNetwork.showInformation();
                case 4 -> { //Exit
                    return;
                }
                default -> System.out.println("Invalid option.");
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
                default -> System.out.println("Invalid option");
            }
            System.out.println("""
                                    
                    Do you want to save the weights to a file?:
                    Yes/No""");
            String printWeights = scanner.nextLine();
            switch (printWeights) {
                case "No" -> {

                }
                case "Yes" -> neuralNetwork.saveWeights();
                default -> System.out.println("Invalid option");
            }
        }
    }
}
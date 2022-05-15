package Neu.Network.userCommunication;

import Neu.Network.summary.SummaryCalculator;
import Neu.Network.model.components.NeuralNetwork;
import Neu.Network.model.dao.DataReader;
import Neu.Network.model.dao.FileNetworkDao;
import Neu.Network.model.exceptions.model.LogicException;
import Neu.Network.model.flower.Iris;
import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);
        ArrayList<Iris> data;
        ArrayList<Iris> trainingData;

        try { //Download data
            data = DataReader.readData("data.csv");
            trainingData = DataReader.readData("trainingPartOfData.csv");
            System.out.println("Collected " + trainingData.size() + " portions of data to train.\n");
            System.out.println("Collected " + data.size() + " portions of data.\n");
        } catch (Exception e) {
            System.out.println("Error occurred");
            return;
        }

        System.out.println("""
                Network options:
                [1]. Create a new network
                [2]. Load the saved network""");
        int networkChoice = Integer.parseInt(scanner.nextLine());

        NeuralNetwork neuralNetwork;
        switch (networkChoice) {
            case 1 -> {
                System.out.println("Enter learning factor:");
                double learningFactor = Double.parseDouble(scanner.nextLine());
                neuralNetwork = new NeuralNetwork(4
                        ,DataReader.getNumberOfHiddenLayers()
                        ,DataReader.getNumberOfHiddenNeurons()
                        ,4
                        , learningFactor);
                System.out.println("Do you want to reflect the bias:\nYes/No");
                neuralNetwork.setBias(Objects.equals(scanner.nextLine(), "Yes"));
            }
            case 2 -> {
                try(FileNetworkDao<NeuralNetwork> fileManager = new FileNetworkDao<>()) {
                    String selectedFile;
                    fileManager.readNamesOfFilesInDirectory();
                    selectedFile = scanner.nextLine();
                    neuralNetwork = fileManager.read(selectedFile);
                    System.out.println("Network loaded!");
                    neuralNetwork.showInformation();
                } catch (LogicException e) {
                    System.out.println(e.getMessage());
                    return;
                } catch (Exception ignored) {
                    return;
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
                    System.out.println("Select the options based on which you want to create the network:\n");
                    System.out.println("Stop condition:\n[1]. number of epochs\n[2]. error level");
                    String stopCondition = scanner.nextLine();
                    System.out.println("Enter value: ");
                    switch (stopCondition) {
                        case "1" -> {
                            neuralNetwork.setStopConditionFlag(true);
                            neuralNetwork.setEpochs(Integer.parseInt(scanner.nextLine()));
                        }
                        case "2" -> {
                            neuralNetwork.setStopConditionFlag(false);
                            neuralNetwork.setAccuracy(Double.parseDouble(scanner.nextLine()));
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + stopCondition);
                    }

                    System.out.println("Do take into account the momentum:\nYes/No");
                    if(Objects.equals(scanner.nextLine(), "Yes")) {
                        System.out.println("Enter the momentum factor:");
                        neuralNetwork.setMomentumFactor(Double.parseDouble(scanner.nextLine()));
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
                case 3 -> {
                    //Exit
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
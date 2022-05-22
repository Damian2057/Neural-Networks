package Neu.Network.userCommunication;

import Neu.Network.model.dao.StatisticsCollector;
import Neu.Network.model.exceptions.argument.ArgumentException;
import Neu.Network.summary.SummaryCalculator;
import Neu.Network.model.components.NeuralNetwork;
import Neu.Network.model.dao.DataReader;
import Neu.Network.model.dao.FileNetworkDao;
import Neu.Network.model.exceptions.model.LogicException;
import Neu.Network.model.flower.Iris;
import java.util.*;

public class App {
    public static void main(String[] args) {

        DataReader.CreateDirectories();
        if(DataReader.GetDeleteMode()) {
            StatisticsCollector.ClearStats();
        }

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

        if(DataReader.getCreateNewNetworkMode() && DataReader.getLoadPrevNetworkMode()) {
            throw new ArgumentException("invalid data in the config file");
        }

        NeuralNetwork neuralNetwork = null;
            if(DataReader.getCreateNewNetworkMode()) {
                neuralNetwork = new NeuralNetwork(DataReader.getNumberOfInPuts()
                        ,DataReader.getNumberOfHiddenNeurons()
                        ,DataReader.getNumberOfOutPuts()
                        , DataReader.getLearningFactor());
                neuralNetwork.setBias(DataReader.getBiasMode());
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
                            neuralNetwork.setEpochs(DataReader.getNumberOfEpochs());
                        }
                        case "2" -> {
                            neuralNetwork.setStopConditionFlag(false);
                            neuralNetwork.setAccuracy(DataReader.getAccuracy());
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + stopCondition);
                    }

                    if(DataReader.getMomentumMode()) {
                        neuralNetwork.setMomentumFactor(DataReader.getMomentumValue());
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
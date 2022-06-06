package Neu.Network.userCommunication;

import Neu.Network.DataManager.SetDistributor;
import Neu.Network.model.dao.*;
import Neu.Network.model.exceptions.argument.ArgumentException;
import Neu.Network.summary.SummaryCalculator;
import Neu.Network.model.components.NeuralNetwork;
import java.util.*;

public class App {
    public static void main(String[] args) {

        DirectoryManager.CreateDirectories();
        if(JsonReader.GetDeleteMode()) {
            DirectoryManager.ClearDirectories();
        }

        Scanner scanner= new Scanner(System.in);

        SetDistributor setDistributor = new SetDistributor(JsonReader.getPercentageSet());
        setDistributor.getInformation();

        if(JsonReader.getCreateNewNetworkMode() && JsonReader.getLoadPrevNetworkMode()) {
            throw new ArgumentException("invalid data in the config file");
        }

        NeuralNetwork neuralNetwork;
            if(JsonReader.getCreateNewNetworkMode()) {
                neuralNetwork = new NeuralNetwork(JsonReader.getNumberOfInPuts()
                        , JsonReader.getNumberOfHiddenNeurons()
                        , JsonReader.getNumberOfOutPuts()
                        , JsonReader.getLearningFactor());
                neuralNetwork.setBias(JsonReader.getBiasMode());
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
            try {
                int modeChoice = Integer.parseInt(scanner.nextLine());

                switch (modeChoice) {
                    case 1 -> {
                        System.out.println("Select the options based on which you want to create the network:\n");
                        System.out.println("Stop condition:\n[1]. number of epochs\n[2]. error level");
                        String stopCondition = scanner.nextLine();
                        switch (stopCondition) {
                            case "1" -> {
                                neuralNetwork.setStopConditionFlag(true);
                                neuralNetwork.setEpochs(JsonReader.getNumberOfEpochs());
                            }
                            case "2" -> {
                                neuralNetwork.setStopConditionFlag(false);
                                neuralNetwork.setAccuracy(JsonReader.getAccuracy());
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + stopCondition);
                        }

                        if (JsonReader.getMomentumMode()) {
                            neuralNetwork.setMomentumFactor(JsonReader.getMomentumValue());
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
                        neuralNetwork.trainNetwork(setDistributor.getTrainingData(), setDistributor.getValidationData());
                    }
                    case 2 -> {
                        SummaryCalculator logicCalculator = new SummaryCalculator();
                        for (var sample : setDistributor.getTestData()) {
                            ArrayList<Double> result = neuralNetwork.calculate(sample);
                            logicCalculator.summarize(result, sample);
                        }
                        logicCalculator.summarizeOfAllTypes();
                    }
                    case 3 -> neuralNetwork.showInformation();
                    case 4 -> { //Exit
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception ignored) {
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
            neuralNetwork.saveWeights();
        }
    }
}
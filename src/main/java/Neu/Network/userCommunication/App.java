package Neu.Network.userCommunication;

import Neu.Network.helper.LogicSummary;
import Neu.Network.model.components.NeuralNetwork;
import Neu.Network.model.dao.DataReader;
import Neu.Network.model.dao.FileNetworkDao;
import Neu.Network.model.exceptions.model.LogicException;
import Neu.Network.model.flower.Iris;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);

        ArrayList<Iris> data;
        ArrayList<Iris> trainingData;

        try { //Upload data
            data = DataReader.readData("data.csv");
            trainingData = DataReader.readData("trainingPartOfData.csv");
            System.out.println("Collected "+data.size()+" portions of data.\n");
            System.out.println("Collected "+trainingData.size()+" portions of data to train.\n");
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
                neuralNetwork = new NeuralNetwork(4,2,4, learningFactor);
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
                    double errorEpochsLevel;
                    boolean stopConditionFlag;
                    switch (stopCondition) {
                        case "1" -> {
                            errorEpochsLevel = Double.parseDouble(scanner.nextLine());
                            stopConditionFlag = true;
                        }
                        case "2" -> {
                            errorEpochsLevel = Double.parseDouble(scanner.nextLine());
                            stopConditionFlag = false;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + stopCondition);
                    }

                    System.out.println("Do take into account the momentum:\nYes/No");
                    double momentumFactor = 0;
                    if(Objects.equals(scanner.nextLine(), "Yes")) {
                        System.out.println("Enter the momentum factor:");
                        momentumFactor = Double.parseDouble(scanner.nextLine());
                    }

                    System.out.println("Enter the method of entering the data:\n[1]. Random\n[2]. Sequentially");
                    String enterChoice = scanner.nextLine();
                    switch (enterChoice) {
                        case "1" -> {
                            List<Integer> randList;
                            randList = IntStream.rangeClosed(0, data.size()-1).boxed().collect(Collectors.toList());
                            for (int i = 0; i < 150; i++) {
                                Random randomizer = new Random();
                                int j = randList.get(randomizer.nextInt(randList.size()));
                                randList.remove(Integer.valueOf(j));
                                neuralNetwork.train(data.get(j)
                                        , stopConditionFlag, errorEpochsLevel
                                        , momentumFactor);
                            }
                        }
                        case "2" -> {
                            for (var sample: data) {
                                neuralNetwork.train(sample
                                        , stopConditionFlag, errorEpochsLevel
                                        , momentumFactor);
                            }
                        }
                        default -> {
                            System.out.println("Invalid option.");
                            return;
                        }
                    }
                }
                case 2 -> {
                    LogicSummary logicCalculator = new LogicSummary();

                    boolean flag = false;
                    System.out.println("Do you want to take into account the value of the bias input\nYes/No");
                    if(Objects.equals(scanner.nextLine(), "Yes")) {
                        flag = true;
                    }

                    for (var sample : data) {
                       // LogicCalculator.Summarize(neuralNetwork.calculate(sample),sample);
                        System.out.println(neuralNetwork.calculate(sample,flag));
                    }
                    logicCalculator.summarizeOfAllTypes();
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
                \nDo you want to save the network to a file?:
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
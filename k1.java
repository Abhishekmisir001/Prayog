package com.av.av;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class k1 extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 500;
    private static final Color BAR_COLOR = Color.rgb(255, 102, 102);
    private static final Color ACTIVE_BAR_COLOR = Color.rgb(102, 178, 255);


    private ComboBox<String> algorithmComboBox;
    private TextField sizeTextField;
    private TextField valuesTextField;
    private Canvas canvas;
    private GraphicsContext gc;
    private List<Integer> data;
    private Slider speedSlider;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #333333, #333333);");




        VBox topBox = new VBox(10);
        topBox.setStyle("-fx-background-color: #333333; -fx-padding: 10px; -fx-border-width: 1px; -fx-border-color: #cccccc;");


        algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll("Bubble Sort", "Selection Sort", "Insertion Sort","Quick Sort");
        algorithmComboBox.setValue("Bubble Sort");
        algorithmComboBox.setStyle("-fx-background-color: #fff;");
        sizeTextField = new TextField();
        sizeTextField.setPromptText("Array Size");
        sizeTextField.setStyle("-fx-background-color: #fff;");
        valuesTextField = new TextField();
        valuesTextField.setPromptText("Array Values (comma-separated)");
        valuesTextField.setStyle("-fx-background-color: #fff;");
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> startAlgorithm());
        startButton.setStyle("-fx-background-color: #90ee90; -fx-text-fill: #000;");
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetCanvas());
        resetButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: #000;");

        // New random button
        Button randomButton = new Button("Random");
        randomButton.setOnAction(e -> generateRandomValues());
        randomButton.setStyle("-fx-background-color: #87ceeb; -fx-text-fill: #000;");

        speedSlider = new Slider(-10, 10, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setBlockIncrement(1);
        HBox controlBox = new HBox(10, startButton, resetButton, randomButton, new Label("Speed:"), speedSlider);
        topBox.getChildren().addAll(
                new Label("Algorithm:"),
                algorithmComboBox,
                sizeTextField,
                valuesTextField,
                controlBox
        );
        root.setTop(topBox);

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setTitle("Algorithm Visualizer");
        primaryStage.show();
    }

    private void startAlgorithm() {
        String algorithm = algorithmComboBox.getValue();
        int size;
        try {
            size = Integer.parseInt(sizeTextField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter a valid integer for array size.");
            return;
        }
        String[] valuesStr = valuesTextField.getText().split(",");
        List<Integer> inputData = new ArrayList<>();
        for (String valueStr : valuesStr) {
            try {
                inputData.add(Integer.parseInt(valueStr.trim()));
            } catch (NumberFormatException e) {
                showAlert("Invalid input", "Please enter valid integers separated by commas for array values.");
                return;
            }
        }

        data = new ArrayList<>(inputData);
        switch (algorithm) {
            case "Bubble Sort":
                bubbleSort();
                break;
            case "Selection Sort":
                selectionSort();
                break;
            case "Insertion Sort":
                insertionSort();
                break;
            case "Quick Sort":
                quickSort(0, data.size() - 1);
                break;
        }
    }

    private void quickSort(int low, int high) {
        new Thread(() -> {
            if (low < high) {
                int pi = partition(low, high);
                quickSort(low, pi - 1);
                quickSort(pi + 1, high);
            }
        }).start();
    }

    private int partition(int low, int high) {
        int pivot = data.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (data.get(j) < pivot) {
                i++;
                int temp = data.get(i);
                data.set(i, data.get(j));
                data.set(j, temp);
                drawBars(i, j);
                try {
                    Thread.sleep((long) (2000 / speedSlider.getValue()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        int temp = data.get(i + 1);
        data.set(i + 1, data.get(high));
        data.set(high, temp);
        drawBars(i + 1, high);
        try {
            Thread.sleep((long) (2000 / speedSlider.getValue()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i + 1;
    }


    private void bubbleSort() {
        new Thread(() -> {
            int n = data.size();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (data.get(j) > data.get(j + 1)) {
                        int temp = data.get(j);
                        data.set(j, data.get(j + 1));
                        data.set(j + 1, temp);
                        drawBars(j, j + 1);
                        try {
                            Thread.sleep((long) (2000 / speedSlider.getValue())); // Adjust animation speed
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void selectionSort() {
        new Thread(() -> {
            int n = data.size();
            for (int i = 0; i < n - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < n; j++) {
                    if (data.get(j) < data.get(minIndex)) {
                        minIndex = j;
                    }
                }
                int temp = data.get(minIndex);
                data.set(minIndex, data.get(i));
                data.set(i, temp);
                drawBars(minIndex, i);
                try {
                    Thread.sleep((long) (2000 / speedSlider.getValue()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void insertionSort() {
        new Thread(() -> {
            int n = data.size();
            for (int i = 1; i < n; i++) {
                int key = data.get(i);
                int j = i - 1;
                while (j >= 0 && data.get(j) > key) {
                    data.set(j + 1, data.get(j));
                    j = j - 1;
                    drawBars(j + 1, i);
                    try {
                        Thread.sleep((long) (2000 / speedSlider.getValue()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                data.set(j + 1, key);
                drawBars(j + 1, j + 1);
                try {
                    Thread.sleep((long) (2000 / speedSlider.getValue()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean isSorted(List<Integer> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i) > data.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void resetCanvas() {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    private void drawBars(int... activeIndices) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        double barWidth = (double) CANVAS_WIDTH / data.size();
        for (int i = 0; i < data.size(); i++) {
            double barHeight = (double) data.get(i) / max(data) * CANVAS_HEIGHT;
            double x = i * barWidth;
            double y = CANVAS_HEIGHT - barHeight;
            if (contains(activeIndices, i)) {
                gc.setFill(ACTIVE_BAR_COLOR);
            } else {
                gc.setFill(BAR_COLOR);
            }
            gc.fillRect(x, y, barWidth, barHeight);


            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(data.get(i)), x + barWidth / 2, y - 5);
        }
    }


    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    private int max(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        for (int num : list) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }


    private void generateRandomValues() {
        try {
            int size = Integer.parseInt(sizeTextField.getText());
            Random random = new Random();
            StringBuilder randomValues = new StringBuilder();
            for (int i = 0; i < size; i++) {
                randomValues.append(random.nextInt(100)).append(",");
            }
            valuesTextField.setText(randomValues.toString());
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter a valid integer for array size.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package ru.nsu.dunaev;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SnakeController {
    @FXML private GridPane gameGrid;
    @FXML private Label scoreLabel;
    @FXML private Label botScoreLabel;
    @FXML private Button startButton;

    private SnakeModel model;
    private SnakeBotModel botModel;
    private SnakeView view;
    private AnimationTimer timer;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int CELL_SIZE = 20;
    private static final int TARGET_LENGTH = WIDTH * HEIGHT;

    @FXML
    public void initialize() {
        model = new SnakeModel(WIDTH, HEIGHT, TARGET_LENGTH);
        botModel = new SnakeBotModel(WIDTH, HEIGHT);
        view = new SnakeView(gameGrid, WIDTH, HEIGHT, CELL_SIZE);
        view.render(model, botModel);
    }

    public void setupInput() {
        gameGrid.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W: model.setDirection(SnakeModel.Direction.UP); break;
                case S: model.setDirection(SnakeModel.Direction.DOWN); break;
                case A: model.setDirection(SnakeModel.Direction.LEFT); break;
                case D: model.setDirection(SnakeModel.Direction.RIGHT); break;
            }
        });
    }

    @FXML
    private void startGame() {
        model.reset();
        botModel.reset();
        view.render(model, botModel);
        if (timer != null) timer.stop();

        timer = new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) {
                    updateGame();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void updateGame() {
        boolean won = model.update(botModel.getBotSnake());
        boolean botNeedsUpdate = botModel.update(model.getFood(), model.getPoop(), model.getSnake());
        if (botNeedsUpdate) {
            if (botModel.getBotSnake().get(0).x == model.getFood().x && botModel.getBotSnake().get(0).y == model.getFood().y) {
                model.spawnFood(botModel.getBotSnake());
            } else if (botModel.getBotSnake().get(0).x == model.getPoop().x && botModel.getBotSnake().get(0).y == model.getPoop().y) {
                model.spawnPoop(botModel.getBotSnake());
            }
        }
        view.render(model, botModel);
        scoreLabel.setText("Player Score: " + model.getScore());
        botScoreLabel.setText("Bot Score: " + botModel.getBotScore());
        if (model.isGameOver()) {
            timer.stop();
            scoreLabel.setText(won ? "You Won! Score: " + model.getScore() :
                    "Game Over! Score: " + model.getScore() + " Bot Score: " + botModel.getBotScore());
        }
    }
}
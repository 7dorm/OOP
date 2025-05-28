package ru.nsu.dunaev;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SnakeView {
    private final GridPane gameGrid;
    private final int width;
    private final int height;
    private final int cellSize;
    private Rectangle[][] cells;

    public SnakeView(GridPane gameGrid, int width, int height, int cellSize) {
        this.gameGrid = gameGrid;
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        initializeGrid();
    }

    private void initializeGrid() {
        gameGrid.getChildren().clear();
        cells = new Rectangle[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Rectangle cell = new Rectangle(cellSize, cellSize);
                cell.setFill(Color.WHITE);
                cells[i][j] = cell;
                gameGrid.add(cell, i, j);
            }
        }
    }

    public void render(SnakeModel model) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j].setFill(Color.WHITE);
            }
        }

        for (SnakeModel.Point p : model.getSnake()) {
            cells[p.x][p.y].setFill(Color.GREEN);
        }

        for (SnakeModel.Point p : model.getBotSnake()) {
            cells[p.x][p.y].setFill(Color.BLUE);
        }

        SnakeModel.Point food = model.getFood();
        cells[food.x][food.y].setFill(Color.RED);

        SnakeModel.Point poop = model.getPoop();
        cells[poop.x][poop.y].setFill(Color.BROWN);
    }
}
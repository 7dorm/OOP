package ru.nsu.dunaev;

import java.util.ArrayList;
import java.util.Random;

public class SnakeModel {
    private ArrayList<Point> snake;
    private Point food;
    private Point poop;
    private Direction direction;
    private int score;
    private final int width;
    private final int height;
    private final int targetLength;
    private boolean gameOver;
    private final Random random;
    private boolean isDirectionChanging;

    public enum Direction {UP, DOWN, LEFT, RIGHT}

    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
    }

    public SnakeModel(int width, int height, int targetLength) {
        this.width = width;
        this.height = height;
        this.targetLength = targetLength;
        this.random = new Random();
        reset();
    }

    public void reset() {
        snake = new ArrayList<>();
        snake.add(new Point(width / 2, height / 2));
        direction = Direction.RIGHT;
        score = 0;
        gameOver = false;
        spawnFood();
        spawnPoop();
    }

    private void spawnFood() {
        do {
            food = new Point(random.nextInt(width), random.nextInt(height));
        } while (snake.contains(food));
    }

    private void spawnPoop() {
        do {
            poop = new Point(random.nextInt(width), random.nextInt(height));
        } while (snake.contains(poop));
    }

    public boolean update(ArrayList<Point> botSnake) {
        if (gameOver) return false;

        Point head = new Point(snake.get(0).x, snake.get(0).y);
        switch (direction) {
            case UP: head.y--; break;
            case DOWN: head.y++; break;
            case LEFT: head.x--; break;
            case RIGHT: head.x++; break;
        }

        if (head.x < 0 || head.x >= width || head.y < 0 || head.y >= height || snake.contains(head) || botSnake.contains(head)) {
            gameOver = true;
            return false;
        }

        snake.add(0, head);

        if (head.x == food.x && head.y == food.y) {
            score++;
            spawnFood();
        } else if (head.x == poop.x && head.y == poop.y) {
            score--;
            spawnPoop();
            snake.removeLast();
            snake.removeLast();
        } else {
            snake.removeLast();
        }

        if (snake.size() >= targetLength) {
            gameOver = true;
            return true;
        } else if (snake.size() <= 0) {
            gameOver = true;
            return false;
        }
        return false;
    }

    public ArrayList<Point> getSnake() { return snake; }
    public Point getFood() { return food; }
    public Point getPoop() { return poop; }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }

    public void setDirection(Direction direction) {
        if (this.isDirectionChanging) return;
        this.isDirectionChanging = true;
        boolean out = false;
        switch (direction) {
            case UP: if (this.direction == Direction.DOWN) out = true; break;
            case DOWN: if (this.direction == Direction.UP) out = true; break;
            case LEFT: if (this.direction == Direction.RIGHT) out = true; break;
            case RIGHT: if (this.direction == Direction.LEFT) out = true; break;
        }
        if (!out) {
            this.direction = direction;
        }
        this.isDirectionChanging = false;
    }

    // Expose spawn methods for SnakeBotModel
    public void spawnFood(ArrayList<Point> botSnake) {
        do {
            food = new Point(random.nextInt(width), random.nextInt(height));
        } while (snake.contains(food) || botSnake.contains(food));
    }

    public void spawnPoop(ArrayList<Point> botSnake) {
        do {
            poop = new Point(random.nextInt(width), random.nextInt(height));
        } while (snake.contains(poop) || botSnake.contains(poop));
    }
}
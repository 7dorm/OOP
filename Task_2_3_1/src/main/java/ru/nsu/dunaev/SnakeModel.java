package ru.nsu.dunaev;

import java.util.ArrayList;
import java.util.Random;

public class SnakeModel {
    private ArrayList<Point> snake;
    private ArrayList<Point> botSnake;
    private Point food;
    private Point poop;
    private Direction direction;
    private Direction botDirection;
    private int score;
    private int botScore;
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
        botSnake = new ArrayList<>();
        snake.add(new Point(width / 2, height / 2));
        botSnake.add(new Point(width / 4, height / 4));
        direction = Direction.RIGHT;
        botDirection = Direction.LEFT;
        score = 0;
        botScore = 0;
        gameOver = false;
        spawnFood();
        spawnPoop();
    }

    private void spawnFood() {
        do {
            food = new Point(random.nextInt(width), random.nextInt(height));
        } while (snake.contains(food) || botSnake.contains(food));
    }

    private void spawnPoop() {
        do {
            poop = new Point(random.nextInt(width), random.nextInt(height));
        } while (snake.contains(poop) || botSnake.contains(poop));
    }

    public boolean update() {
        if (gameOver) return false;

        Point head = new Point(snake.get(0).x, snake.get(0).y);
        switch (direction) {
            case UP: head.y--; break;
            case DOWN: head.y++; break;
            case LEFT: head.x--; break;
            case RIGHT: head.x++; break;
        }

        Point botHead = new Point(botSnake.get(0).x, botSnake.get(0).y);
        botDirection = calculateBotDirection(botHead);
        switch (botDirection) {
            case UP: botHead.y--; break;
            case DOWN: botHead.y++; break;
            case LEFT: botHead.x--; break;
            case RIGHT: botHead.x++; break;
        }

        if (head.x < 0 || head.x >= width || head.y < 0 || head.y >= height || snake.contains(head) || botSnake.contains(head)) {
            gameOver = true;
            return false;
        }

        if (botHead.x < 0 || botHead.x >= width || botHead.y < 0 || botHead.y >= height || botSnake.contains(botHead) || snake.contains(botHead)) {
            botScore -= 5;
            botSnake.clear();
            botSnake.add(new Point(width / 4, height / 4));
            botDirection = Direction.LEFT;
            return false;
        }

        snake.add(0, head);
        botSnake.add(0, botHead);

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

        if (botHead.x == food.x && botHead.y == food.y) {
            botScore++;
            spawnFood();
        } else if (botHead.x == poop.x && botHead.y == poop.y) {
            botScore--;
            spawnPoop();
            botSnake.removeLast();
            botSnake.removeLast();
        } else {
            botSnake.removeLast();
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

    private Direction calculateBotDirection(Point botHead) {
        int dx = food.x - botHead.x;
        int dy = food.y - botHead.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0 && botDirection != Direction.LEFT && !willCollide(botHead, Direction.RIGHT)) {
                return Direction.RIGHT;
            } else if (dx < 0 && botDirection != Direction.RIGHT && !willCollide(botHead, Direction.LEFT)) {
                return Direction.LEFT;
            }
        } else {
            if (dy > 0 && botDirection != Direction.UP && !willCollide(botHead, Direction.DOWN)) {
                return Direction.DOWN;
            } else if (dy < 0 && botDirection != Direction.DOWN && !willCollide(botHead, Direction.UP)) {
                return Direction.UP;
            }
        }
        if (!willCollide(botHead, botDirection)) {
            return botDirection;
        }
        for (Direction dir : Direction.values()) {
            if (dir != oppositeDirection(botDirection) && !willCollide(botHead, dir)) {
                return dir;
            }
        }
        return botDirection;
    }

    private boolean willCollide(Point head, Direction dir) {
        int newX = head.x;
        int newY = head.y;
        switch (dir) {
            case UP: newY--; break;
            case DOWN: newY++; break;
            case LEFT: newX--; break;
            case RIGHT: newX++; break;
        }
        Point newHead = new Point(newX, newY);
        return newX < 0 || newX >= width || newY < 0 || newY >= height || snake.contains(newHead) || botSnake.contains(newHead);
    }

    private Direction oppositeDirection(Direction dir) {
        switch (dir) {
            case UP: return Direction.DOWN;
            case DOWN: return Direction.UP;
            case LEFT: return Direction.RIGHT;
            case RIGHT: return Direction.LEFT;
            default: return dir;
        }
    }

    public ArrayList<Point> getSnake() { return snake; }
    public ArrayList<Point> getBotSnake() { return botSnake; }
    public Point getFood() { return food; }
    public Point getPoop() { return poop; }
    public int getScore() { return score; }
    public int getBotScore() { return botScore; }
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
}
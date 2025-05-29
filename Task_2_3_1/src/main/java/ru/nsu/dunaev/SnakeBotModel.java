package ru.nsu.dunaev;

import java.util.ArrayList;
import java.util.Random;

public class SnakeBotModel {
    private ArrayList<SnakeModel.Point> botSnake;
    private SnakeModel.Direction botDirection;
    private int botScore;
    private final int width;
    private final int height;
    private final Random random;

    public SnakeBotModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        reset();
    }

    public void reset() {
        botSnake = new ArrayList<>();
        botSnake.add(new SnakeModel.Point(width / 4, height / 4));
        botDirection = SnakeModel.Direction.LEFT;
        botScore = 0;
    }

    public boolean update(SnakeModel.Point food, SnakeModel.Point poop, ArrayList<SnakeModel.Point> playerSnake) {
        SnakeModel.Point botHead = new SnakeModel.Point(botSnake.get(0).x, botSnake.get(0).y);
        botDirection = calculateBotDirection(botHead, food, playerSnake);
        switch (botDirection) {
            case UP: botHead.y--; break;
            case DOWN: botHead.y++; break;
            case LEFT: botHead.x--; break;
            case RIGHT: botHead.x++; break;
        }

        if (botHead.x < 0 || botHead.x >= width || botHead.y < 0 || botHead.y >= height || botSnake.contains(botHead) || playerSnake.contains(botHead)) {
            botScore -= 5;
            botSnake.clear();
            botSnake.add(new SnakeModel.Point(width / 4, height / 4));
            botDirection = SnakeModel.Direction.LEFT;
            return false;
        }

        botSnake.add(0, botHead);

        if (botHead.x == food.x && botHead.y == food.y) {
            botScore++;
            return true; // Signal to spawn new food
        } else if (botHead.x == poop.x && botHead.y == poop.y) {
            botScore--;
            botSnake.removeLast();
            if (!botSnake.isEmpty()) botSnake.removeLast();
            return true; // Signal to spawn new poop
        } else {
            botSnake.removeLast();
        }
        return false;
    }

    private SnakeModel.Direction calculateBotDirection(SnakeModel.Point botHead, SnakeModel.Point food, ArrayList<SnakeModel.Point> playerSnake) {
        int dx = food.x - botHead.x;
        int dy = food.y - botHead.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0 && botDirection != SnakeModel.Direction.LEFT && !willCollide(botHead, SnakeModel.Direction.RIGHT, playerSnake)) {
                return SnakeModel.Direction.RIGHT;
            } else if (dx < 0 && botDirection != SnakeModel.Direction.RIGHT && !willCollide(botHead, SnakeModel.Direction.LEFT, playerSnake)) {
                return SnakeModel.Direction.LEFT;
            }
        } else {
            if (dy > 0 && botDirection != SnakeModel.Direction.UP && !willCollide(botHead, SnakeModel.Direction.DOWN, playerSnake)) {
                return SnakeModel.Direction.DOWN;
            } else if (dy < 0 && botDirection != SnakeModel.Direction.UP && !willCollide(botHead, SnakeModel.Direction.UP, playerSnake)) {
                return SnakeModel.Direction.UP;
            }
        }

        if (!willCollide(botHead, botDirection, playerSnake)) {
            return botDirection;
        }
        for (SnakeModel.Direction dir : SnakeModel.Direction.values()) {
            if (dir != oppositeDirection(botDirection) && !willCollide(botHead, dir, playerSnake)) {
                return dir;
            }
        }
        return botDirection;
    }

    private boolean willCollide(SnakeModel.Point head, SnakeModel.Direction dir, ArrayList<SnakeModel.Point> playerSnake) {
        int newX = head.x;
        int newY = head.y;
        switch (dir) {
            case UP: newY--; break;
            case DOWN: newY++; break;
            case LEFT: newX--; break;
            case RIGHT: newX++; break;
        }
        SnakeModel.Point newHead = new SnakeModel.Point(newX, newY);
        return newX < 0 || newX >= width || newY < 0 || newY >= height || botSnake.contains(newHead) || playerSnake.contains(newHead);
    }

    private SnakeModel.Direction oppositeDirection(SnakeModel.Direction dir) {
        switch (dir) {
            case UP: return SnakeModel.Direction.DOWN;
            case DOWN: return SnakeModel.Direction.UP;
            case LEFT: return SnakeModel.Direction.RIGHT;
            case RIGHT: return SnakeModel.Direction.LEFT;
            default: return dir;
        }
    }

    public ArrayList<SnakeModel.Point> getBotSnake() { return botSnake; }
    public int getBotScore() { return botScore; }
}
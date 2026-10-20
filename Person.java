
package org.example;

import java.util.List;
import java.util.Scanner;

public class Person {

    private int y;
    private int x;

    private int hp = 3;
    private boolean hasSword = false;
    private boolean alive = true;
    private boolean won = false;

    public Person(int y, int x) {
        this.y = y;
        this.x = x;
    }

    // Размещение игрока на поле
    public void putOnMap(String[][] matrix) {
        matrix[y][x] = "*";
    }

    // Движение игрока | делал гпт
    public boolean move(String command, String[][] matrix,
                        List<Monster> monsters,
                        int castleY, int castleX,
                        Scanner scanner) {

        int newY = y;
        int newX = x;

        if (command.equals("w")) {
            newY -= 2;
        } else if (command.equals("s")) {
            newY += 2;
        } else if (command.equals("a")) {
            newX -= 2;
        } else if (command.equals("d")) {
            newX += 2;
        } else {
            System.out.println("Неизвестная команда!");
            return true;
        }

        // Проверка выхода за пределы матрицы | делал гпт
        if (newY < 1 || newY >= matrix.length - 1 ||
                newX < 1 || newX >= matrix[0].length - 1) {
            return true;
        }

        // Вода сразу убивает игрока
        if (matrix[newY][newX].equals("~")) {
            alive = false;
            matrix[y][x] = " ";
            return false;
        }

        // Подбор меча
        if (matrix[newY][newX].equals("!")) {
            hasSword = true;
            System.out.println("Ты нашёл меч!");
        }

        // Проверка встречи с монстром | делал гпт
        for (Monster monster : monsters) {
            if (monster.isAlive() &&
                    newY == monster.getY() &&
                    newX == monster.getX()) {

                if (hasSword) {
                    monster.setAlive(false);
                    matrix[newY][newX] = " ";
                    System.out.println("Ты победил монстра мечом!");
                } else {
                    boolean correct = monster.askQuestion(scanner);

                    if (correct) {
                        monster.setAlive(false);
                        matrix[newY][newX] = " ";
                        System.out.println("Правильно! Монстр побеждён.");
                    } else {
                        hp--;
                        System.out.println("Неправильно! Ты потерял 1 HP.");

                        if (hp <= 0) {
                            alive = false;
                            matrix[y][x] = " ";
                            return false;
                        }

                        return true;
                    }
                }

                break;
            }
        }

        // Проверка достижения замка
        if (newY == castleY && newX == castleX) {
            matrix[y][x] = " ";

            y = newY;
            x = newX;

            matrix[y][x] = "*";
            won = true;

            return false;
        }

        // Перемещение игрока
        matrix[y][x] = " ";

        y = newY;
        x = newX;

        matrix[y][x] = "*";

        return true;
    }

    public int getHp() {
        return hp;
    }

    public boolean hasSword() {
        return hasSword;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean hasWon() {
        return won;
    }
}

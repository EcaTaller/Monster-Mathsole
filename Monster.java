
package org.example;

import java.util.Random;
import java.util.Scanner;

public class Monster {

    private int y;
    private int x;
    private boolean alive = true;

    private Random random;

    public Monster(String[][] matrix, Random random) {
        this.random = random;

        int rows = matrix.length;
        int columns = matrix[0].length;

        // Поиск свободной клетки
        do {
            y = 3 + 2 * random.nextInt((rows - 5) / 2);
            x = 3 + 2 * random.nextInt((columns - 5) / 2);
        } while (!matrix[y][x].equals(" "));

        matrix[y][x] = "#";
    }

    // Математическая атака | делал гпт
    public boolean askQuestion(Scanner scanner) {
        int a;
        int b;
        int answer;
        String question;

        int type = random.nextInt(5);

        if (type == 0) {
            // Сложение
            a = 10 + random.nextInt(90);
            b = 10 + random.nextInt(90);

            question = a + " + " + b + " = ?";
            answer = a + b;

        } else if (type == 1) {
            // Умножение
            a = 2 + random.nextInt(11);
            b = 2 + random.nextInt(11);

            question = a + " * " + b + " = ?";
            answer = a * b;

        } else if (type == 2) {
            // Деление без остатка
            b = 2 + random.nextInt(9);
            a = 2 + random.nextInt(11);
            int result = a * b;

            question = result + " / " + b + " = ?";
            answer = a;

        } else if (type == 3) {
            // Степень
            a = 2 + random.nextInt(4);
            b = 2 + random.nextInt(3);

            question = a + "^" + b + " = ?";
            answer = (int) Math.pow(a, b);

        } else {
            // Логарифм
            a = 2 + random.nextInt(4);
            b = 2 + random.nextInt(3);

            int result = (int) Math.pow(a, b);

            question = "log_" + a + "(" + result + ") = ?";
            answer = b;
        }

        System.out.println("Монстр атакует!");
        System.out.println(question);
        System.out.print("Ответ: ");

        String input = scanner.nextLine().trim();

        try {
            int playerAnswer = Integer.parseInt(input);
            return playerAnswer == answer;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}

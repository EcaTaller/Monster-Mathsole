
package org.example;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Настройка вывода текста | делал гпт
        System.setOut(new PrintStream(
                new FileOutputStream(FileDescriptor.out),
                true,
                StandardCharsets.UTF_8
        ));

        int floor = 10;
        int wall = 10;

        int rows = floor * 2 + 1;
        int columns = wall * 2 + 1;

        Random random = new Random();
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        // Создание поля
        String[][] matrix = generateMap(rows, columns);

        // Генерация воды
        generateWater(matrix);
        generateInnerWater(matrix, random);

        // Генерация замка
        int castleY = 3;
        int castleX = getFreeX(matrix, castleY, random);
        matrix[castleY][castleX] = "^";

        // Генерация игрока
        int playerY = rows - 4;
        int playerX = getFreeX(matrix, playerY, random);

        Person person = new Person(playerY, playerX);
        person.putOnMap(matrix);

        // Генерация меча
        int swordY = getFreeY(matrix, random);
        int swordX = getFreeX(matrix, swordY, random);
        matrix[swordY][swordX] = "!";

        // Генерация монстров
        List<Monster> monsters = new ArrayList<>();

        int freeCells = countFreeCells(matrix);
        int monsterCount = Math.max(2, freeCells / 8);
        monsterCount = Math.min(8, monsterCount);
        monsterCount = Math.min(monsterCount, freeCells);

        for (int i = 0; i < monsterCount; i++) {
            monsters.add(new Monster(matrix, random));
        }

        // Основной игровой цикл | делал гпт
        boolean game = true;
        String event = "Найди замок и постарайся выжить!";

        while (game) {
            drawMap(matrix);

            System.out.println("HP: " + person.getHp() + "/3");
            System.out.println("Меч: " + (person.hasSword() ? "есть" : "нет"));
            System.out.println("Монстров осталось: " + countMonsters(monsters));
            System.out.println(event);

            System.out.println("W - вверх | S - вниз | A - влево | D - вправо");
            System.out.println("Q - выход");
            System.out.print("Твой ход: ");

            String command = scanner.nextLine().trim().toLowerCase();

            if (command.equals("q")) {
                break;
            }

            event = "";
            game = person.move(command, matrix, monsters,
                    castleY, castleX, scanner);

            if (!person.isAlive()) {
                event = "Ты погиб!";
            } else if (person.hasWon()) {
                event = "Ты добрался до замка и победил!";
            }
        }

        drawMap(matrix);

        System.out.println("HP: " + person.getHp() + "/3");
        System.out.println(event.isEmpty() ? "Игра завершена!" : event);

        scanner.close();
    }

    // Генерация поля
    public static String[][] generateMap(int rows, int columns) {
        String[][] matrix = new String[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    matrix[i][j] = "+";
                } else if (i % 2 == 0) {
                    matrix[i][j] = "-";
                } else if (j % 2 == 0) {
                    matrix[i][j] = "|";
                } else {
                    matrix[i][j] = " ";
                }
            }
        }

        return matrix;
    }

    // Генерация воды по краям поля
    public static void generateWater(String[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        for (int i = 1; i < rows - 1; i++) {
            matrix[i][1] = "~";
            matrix[i][columns - 2] = "~";
        }

        for (int j = 1; j < columns - 1; j++) {
            matrix[1][j] = "~";
            matrix[rows - 2][j] = "~";
        }
    }

    // Генерация воды с ограничением 2кл макс на строку
    public static void generateInnerWater(String[][] matrix, Random random) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        for (int i = 3; i <= rows - 4; i += 2) {
            int waterCount = 1 + random.nextInt(2);

            for (int j = 0; j < waterCount; j++) {
                int x = 3 + 2 * random.nextInt((columns - 5) / 2);

                if (matrix[i][x].equals(" ")) {
                    matrix[i][x] = "~";
                }
            }
        }
    }

    // Поиск свободного столбца в строке | делал гпт
    public static int getFreeX(String[][] matrix, int y, Random random) {
        int x;

        do {
            x = 3 + 2 * random.nextInt((matrix[0].length - 5) / 2);
        } while (!matrix[y][x].equals(" "));

        return x;
    }

    // Поиск свободной строки | делал гпт
    public static int getFreeY(String[][] matrix, Random random) {
        int y;

        do {
            y = 3 + 2 * random.nextInt((matrix.length - 5) / 2);
        } while (!hasFreeX(matrix, y));

        return y;
    }

    // Проверка свободной клетки в строке | делал гпт
    public static boolean hasFreeX(String[][] matrix, int y) {
        for (int x = 3; x <= matrix[0].length - 4; x += 2) {
            if (matrix[y][x].equals(" ")) {
                return true;
            }
        }

        return false;
    }

    // Подсчёт пустых клеток | делал гпт
    public static int countFreeCells(String[][] matrix) {
        int count = 0;

        for (int y = 3; y <= matrix.length - 4; y += 2) {
            for (int x = 3; x <= matrix[0].length - 4; x += 2) {
                if (matrix[y][x].equals(" ")) {
                    count++;
                }
            }
        }

        return count;
    }

    // Подсчёт живых монстров | делал гпт
    public static int countMonsters(List<Monster> monsters) {
        int count = 0;

        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                count++;
            }
        }

        return count;
    }

    // Отрисовка поля | делал гпт
    public static void drawMap(String[][] matrix) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (String[] row : matrix) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}

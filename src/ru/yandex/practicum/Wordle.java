package ru.yandex.practicum;

import java.util.Random;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    public static final String WORDS_FILE = "words_ru.txt";
    public static final String LOG_FILE = "game_log.txt";
    public static final int STEPS = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        GameLogger logger = new GameLogger(LOG_FILE);
        logger.log("Игра началась");
        WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader(WORDS_FILE, "UTF-8", logger);

        WordleDictionary wordleDictionary = null;
        try {
            wordleDictionary = dictionaryLoader.loadDictionary();
        } catch (DictionaryLoadException e) {
            logger.log("Ошибка: " + e.getMessage());
            logger.close();
            return;
        }
        WordleGame wordleGame = new WordleGame(wordleDictionary, logger);

        int randomInRange = random.nextInt(wordleDictionary.getSize());
        try {
            wordleGame.setAnswer(randomInRange);
        } catch (WordleGameException e) {
            logger.log("Ошибка установки ответа " + e.getMessage());
            logger.close();
            return;
        }
        wordleGame.setSteps(STEPS);
        logger.log("Игра инициализирована. Максимально шагов: 6");

        runGame(scanner, wordleGame, wordleDictionary, logger);

        logger.log("Игра закончена");
        logger.close();
    }

    public static void runGame(Scanner scanner, WordleGame wordleGame, WordleDictionary wordleDictionary, GameLogger logger) {
        boolean rightAnswer = false;
        while ((wordleGame.getSteps() > 0) && (!rightAnswer)) {
            String check = "";
            System.out.println("Введите слово: ");
            String answer = scanner.nextLine();

            if (answer.isEmpty()) {
                answer = wordleGame.getHint();
                System.out.println(answer);
            }

            if (wordleDictionary.contains(answer)) {
                wordleGame.setSteps(wordleGame.getSteps() - 1);
                check = wordleGame.getNextStep(answer);
                System.out.println(check);
            } else {
                System.out.println("Такого слова в словаре нет");
            }

            if (check.equals("+++++")) {
                rightAnswer = true;
                logger.log("Выигрыш! Загаданное слово: " + answer);
            }
        }
        if (!rightAnswer) {
            System.out.println("Вы не угадали. Загаданное слово: " + wordleGame.getAnswer());
            logger.log("Проигрыш. Ответ был: " + wordleGame.getAnswer());
        }
    }
}

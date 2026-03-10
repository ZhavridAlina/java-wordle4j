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

    public static void main(String[] args) {
        String fileName = "words_ru.txt";
        String logFileName = "game_log.txt";
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        boolean rightAnswer = false;

        GameLogger logger = new GameLogger(logFileName);
        logger.log("Игра началась");

        WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader(fileName, "UTF-8", logger);
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
        wordleGame.setSteps(6);
        logger.log("Игра инициализирована. Максимально шагов: 6");

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
        logger.log("Игра закончена");
        logger.close();


    }
}

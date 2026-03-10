package ru.yandex.practicum;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;

    private int steps;

    private final WordleDictionary dictionary;
    private final GameLogger logger;
    private LinkedHashMap<String, String> history = new LinkedHashMap<>();
    private Map<Integer, Character> exactPositions = new HashMap<>();
    private Set<Character> correctLetters = new HashSet<>();      // (+)
    private Set<Character> presentLetters = new HashSet<>();      // (^)
    private Set<Character> absentLetters = new HashSet<>();       // (-)

    public WordleGame(WordleDictionary dictionary, GameLogger logger) {
        this.dictionary = dictionary;
        this.logger = logger;
    }

    public void setAnswer(int index) throws WordleGameException {
        if (index < 0 || index >= dictionary.getSize()) {
            logger.log("Попытка записать ответ с неправильным индексом: " + index);
            throw new WordleGameException("Неправильное значение индекса");
        } else {
            this.answer = dictionary.getWord(index);
            logger.log("Ответ установлен:" + answer);
        }
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getNextStep(String word) {
        if (word.length() != 5) {
            logger.log("Некорректная длина слова: " + word);
            throw new IllegalArgumentException("Слово должно состоять из 5 букв");
        }
        String step = "";
        for (int i = 0; i < 5; i++) {
            if (answer.indexOf(word.charAt(i)) != -1) {
                if (answer.charAt(i) == word.charAt(i)) {
                    step += "+";
                    correctLetters.add(word.charAt(i));
                    exactPositions.put(i, word.charAt(i));
                } else {
                    step += "^";
                    presentLetters.add(word.charAt(i));
                }
            } else {
                step += "-";
                absentLetters.add(word.charAt(i));
            }
        }
        history.put(word, step);
        logger.log("Слово=" + word + ", результат=" + step);
        return step;
    }

    public String getHint() {
        Random random = new Random();
        List<String> possibleWords = new ArrayList<>();

        if (history.isEmpty()) {
            String hint = dictionary.getDictionary().get(random.nextInt(dictionary.getSize()));
            logger.log("Нет истории попыток,записано: " + hint);
            return hint;
        }
        for (String word : dictionary.getDictionary()) {
            if (!history.containsKey(word) &&
                    matchesExactPositions(word) &&
                    !containsAbsentLetters(word) &&
                    containsAllPresentLetters(word)) {
                possibleWords.add(word);
            }
        }

        if (possibleWords.isEmpty()) {
            logger.log("Не найдено возможных слов для подсказки, возвращаем случайный");
            return dictionary.getDictionary().get(random.nextInt(dictionary.getSize()));
        }

        String hint = possibleWords.get(random.nextInt(possibleWords.size()));
        logger.log("Подсказка: " + hint);
        return hint;
    }

    private boolean matchesExactPositions(String word) {
        for (Map.Entry<Integer, Character> entry : exactPositions.entrySet()) {
            if (word.charAt(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private boolean containsAbsentLetters(String word) {
        for (char letter : absentLetters) {
            if (word.indexOf(letter) != -1) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAllPresentLetters(String word) {
        for (char letter : presentLetters) {
            if (word.indexOf(letter) == -1) {
                return false;
            }
        }
        return true;
    }
}

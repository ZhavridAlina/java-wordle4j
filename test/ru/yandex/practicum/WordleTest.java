package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    @Test
    public void testDictionaryFiltredByLength() {
        List<String> words = Arrays.asList("яблоко", "банан", "персик", "слива", "виноград", "киви");
        WordleDictionary dictionary = new WordleDictionary(words);

        assertEquals(2, dictionary.getSize());
        assertTrue(dictionary.contains("банан"));
        assertTrue(dictionary.contains("слива"));
        assertFalse(dictionary.contains("яблоко"));
        assertFalse(dictionary.contains("персик"));
        assertFalse(dictionary.contains("виноград"));
        assertFalse(dictionary.contains("киви"));
    }

    @Test
    public void testDictionaryContainsWord() {
        List<String> words = List.of("банан", "слива");
        WordleDictionary dictionary = new WordleDictionary(words);
        assertTrue(dictionary.contains("слива"));
        assertFalse(dictionary.contains("яблоко"));
    }

    @Test
    public void testDictionaryContainsNullReturnFalse() {
        List<String> words = List.of("банан");
        WordleDictionary dictionary = new WordleDictionary(words);
        assertFalse(dictionary.contains(null));
    }

    @Test
    public void testDictionarygetWordByIndex() {
        List<String> words = List.of("банан", "слива");
        WordleDictionary dictionary = new WordleDictionary(words);
        assertEquals("банан", dictionary.getWord(0));
        assertEquals("слива", dictionary.getWord(1));
    }

    @Test
    public void testDictionaryIndexOutOfBounds() {
        List<String> words = List.of("банан");
        WordleDictionary dictionary = new WordleDictionary(words);
        assertThrows(IndexOutOfBoundsException.class, () -> dictionary.getWord(5));
    }

    @Test
    public void testGameSetAnswerCorrectIndex() throws Exception {
        File logFile = File.createTempFile("game", ".log");
        logFile.deleteOnExit();
        GameLogger logger = new GameLogger(logFile.getAbsolutePath());

        List<String> words = List.of("банан", "слива");
        WordleDictionary dict = new WordleDictionary(words);
        WordleGame game = new WordleGame(dict, logger);

        game.setAnswer(0);
        assertEquals("банан", game.getAnswer());

        String logContent = Files.readString(logFile.toPath());
        assertTrue(logContent.contains("Ответ установлен:банан"));

        logFile.delete();
    }

    @Test
    public void testGameIncorrectWordLength() throws Exception {
        File logFile = File.createTempFile("game", ".log");
        logFile.deleteOnExit();
        GameLogger logger = new GameLogger(logFile.getAbsolutePath());

        List<String> words = List.of("слива");
        WordleDictionary dict = new WordleDictionary(words);
        WordleGame game = new WordleGame(dict, logger);
        game.setAnswer(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> game.getNextStep("яблоко"));
        assertEquals("Слово должно состоять из 5 букв", exception.getMessage());

        String logContent = Files.readString(logFile.toPath());
        assertTrue(logContent.contains("Некорректная длина слова: яблоко"));

        logFile.delete();
    }

    @Test
    public void testGameAbsoluteСoincidence() throws Exception {
        File logFile = File.createTempFile("game", ".log");
        logFile.deleteOnExit();
        GameLogger logger = new GameLogger(logFile.getAbsolutePath());

        List<String> words = List.of("слива");
        WordleDictionary dict = new WordleDictionary(words);
        WordleGame game = new WordleGame(dict, logger);
        game.setAnswer(0);

        String result = game.getNextStep("слива");
        assertEquals("+++++", result);

        String logContent = Files.readString(logFile.toPath());
        assertTrue(logContent.contains("Слово=слива, результат=+++++"));

        logFile.delete();
    }

    @Test
    public void testGamePartlyCoincidence() throws Exception {
        File logFile = File.createTempFile("game", ".log");
        logFile.deleteOnExit();
        GameLogger logger = new GameLogger(logFile.getAbsolutePath());

        List<String> words = List.of("банан");
        WordleDictionary dict = new WordleDictionary(words);
        WordleGame game = new WordleGame(dict, logger);
        game.setAnswer(0);
        String result = game.getNextStep("банка");
        assertEquals("+++-^", result);

        logFile.delete();
    }

    @Test
    public void testGameWithHint() throws Exception {
        File logFile = File.createTempFile("game", ".log");
        logFile.deleteOnExit();
        GameLogger logger = new GameLogger(logFile.getAbsolutePath());

        List<String> words = List.of("банан", "слива", "булка", "банка");
        WordleDictionary dict = new WordleDictionary(words);
        WordleGame game = new WordleGame(dict, logger);
        game.setAnswer(0);

        game.getNextStep("булка");
        String hint = game.getHint();
        assertEquals("банан", hint);

        logFile.delete();
    }
}


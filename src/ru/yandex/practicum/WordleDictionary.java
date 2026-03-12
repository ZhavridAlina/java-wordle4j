package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private final int WORD_LENGTH = 5;
    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = cleanWords(words);
    }

    public String getWord(int index) {
        return words.get(index);
    }

    public int getSize() {
        return words.size();
    }

    public List<String> getDictionary() {
        return words;
    }

    public boolean contains(String word) {
        return word != null && words.contains(word);
    }

    private List<String> cleanWords(List<String> words) {
        List<String> cleanWords = new ArrayList<>();

        for (String word : words) {
            if (word.length() == WORD_LENGTH) {
                word = word.toLowerCase();
                cleanWords.add(word);
            }
        }
        return cleanWords;
    }

}

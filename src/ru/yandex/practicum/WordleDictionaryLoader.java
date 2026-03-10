package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private final String fileName;
    private final String encoding;
    private final GameLogger logger;


    public WordleDictionaryLoader(String fileName, String encoding, GameLogger logger) {
        this.fileName = fileName;
        this.encoding = encoding;
        this.logger = logger;
    }

    public WordleDictionary loadDictionary() throws DictionaryLoadException {
        List<String> words = new ArrayList<>();
        logger.log("Загружаем данные из файла " + fileName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), Charset.forName(encoding)))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    words.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            logger.log("Файл не найден - " + fileName);
            throw new DictionaryLoadException(e.getMessage());
        } catch (IOException e) {
            logger.log("Ошибка чтения файла - " + fileName);
            throw new DictionaryLoadException(e.getMessage());
        }
        logger.log("Загружено " + words.size() + " слов из файла");
        return new WordleDictionary(words);
    }
}

package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameLogger {
    private final PrintWriter writer;

    public GameLogger(String logFile) {
        PrintWriter tmp = null;
        try {
            tmp = new PrintWriter(new FileWriter(logFile, true));
        } catch (IOException e) {
            System.err.println("Ошибка при создании лог файла: " + e.getMessage());
        }
        writer = tmp;
    }

    public void log(String message) {
        if (writer != null) {
            writer.println(message);
            writer.flush();
        }
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}

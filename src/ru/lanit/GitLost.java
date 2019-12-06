package ru.lanit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * В этой программе можно оставлять вопросы в виде комментариев. На все вопросы я буду отвечать с удалением вопроса.
 * Комментарии кроме вопросов можно удалять. Таким образом можно будет что-то узнать пролистывая историю комммитов ;)
 */
public class GitLost {
    // все литералы лучше держать в константах
    // static инициализируются только один раз и в памяти занимают меньше места
    // все константы чаще всего делаются static
    // кроме того, принято писать названия констант в верхнем регистре через подчёркивание, НАПРИМЕР_ВОТ_ТАК
    // ключевое слово final не даёт изменить значение переменной. Для объектных типов это ссылка на объект.
    public static final String FILE_NAME = "src/ru/lanit/names.txt";

    public static void main(String[] args) throws IOException {
        // Эта функция (статические методы являются, по сути, функциями) считывает все строки из файла,
        // как и следует из её названия
        final List<String> names = Files.readAllLines(Paths.get(FILE_NAME));
        // этот метод удаляет при помощи лямбды пустые записи. он перебирает всю коллекцию и если при выполнении функции
        // (лямбда это функция как объект), она возвращает true, объект удаляется из коллекции. очень удобно.
        names.removeIf(name -> name.trim().isEmpty());
        // тут имён может просто не остаться
        if (names.isEmpty()) {
            // поэтому советуем единственный возможный вариант
            System.out.println("Добавь своё имя в " + FILE_NAME);
        } else {
            // используем удобный метод объекта класса Random, используя размер коллекции
            // в качестве максимального значения случайного числа (не включительно)
            final int randomIndex = new Random().nextInt(names.size());
            System.out.println("Мы удалили \"" + names.get(randomIndex) + "\". Теперь добавь своё имя в любой строке " + FILE_NAME);
            names.remove(randomIndex);
            Files.write(Paths.get(FILE_NAME), names);
        }
        // а здесь выводим победителя(лей) при помощи статического метода getWinnersList
        System.out.println(getWinnersList(names));
    }

    /**
     * статический метод getWinnersList возвращает информацию по самы часто встречающимся именам. Либо нет.
     * Попробуй сделать его не статическим. Что нужно сделать для того, чтобы его можно было использовать в методе
     * main?
     */
    private static String getWinnersList(List<String> names) {
        // собираем список в ассоциативный-массив - карту (мапу), имя - количество
        // я бы хотел попроще, но у меня стримоз :(
        final Map<String, Long> grouped = names.stream()
                .collect(Collectors.groupingBy(name -> name.toUpperCase().trim(), Collectors.counting()));
        // просто находим самое большое число. можно и циклом сделать, но вдруг коллекция пуста?
        final OptionalInt max = grouped.values().stream()
                .mapToInt(Long::intValue)
                .max();
        // ну если всё-таки что-то есть, победители найдутся
        if (max.isPresent()) {
            return "Победители: " +
                    grouped.entrySet()
                            .stream().filter(e -> e.getValue().intValue() == max.getAsInt())
                            .map(Map.Entry::getKey)
                            .collect(Collectors.joining(", "))
                    + "\n\tс результатом: " + max.getAsInt();
        } else {
            // либо нет
            return "Победителей нет :(";
        }
    }
}

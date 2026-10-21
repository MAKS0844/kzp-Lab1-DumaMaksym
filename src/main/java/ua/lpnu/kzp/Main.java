package ua.lpnu.kzp;

public final class Main {
    
    private Main() {
        // Приховуємо конструктор утилітного класу
    }

    public static void main(String[] args) {
        try {
            // 1. Делегуємо парсинг аргументів
            ArgumentParser arguments = ArgumentParser.parse(args);

            // 2. Передаємо управління контролеру
            MovieController controller = new MovieController();
            controller.process(arguments.getInputPath(), arguments.getOutputPath());

        } catch (Exception e) {
            // Обробляємо будь-які помилки й повертаємо код неуспішного завершення
            System.err.println("Помилка виконання: " + e.getMessage());
            System.exit(1);
        }
    }
}

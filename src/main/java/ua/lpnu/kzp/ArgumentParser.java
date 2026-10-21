package ua.lpnu.kzp;

public final class ArgumentParser {
    private final String inputPath;
    private final String outputPath;

    private ArgumentParser(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public String getInputPath() { return inputPath; }
    public String getOutputPath() { return outputPath; }

    public static ArgumentParser parse(String[] args) {
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.out.printf("Використання: java -jar lab01-1.1.0.jar [--help] [--version] [--input <файл>] [--output <файл>]%n");
                System.exit(0);
            }
            if ("--version".equals(args[0])) {
                System.out.printf("Версія програми: %s%n", VersionProvider.getVersion());
                System.exit(0);
            }
        }

        String input = "data/input.csv"; // Значення за замовчуванням
        String output = null;

        for (int i = 0; i < args.length; i++) {
            if ("--input".equals(args[i]) && i + 1 < args.length) {
                input = args[++i];
            } else if ("--output".equals(args[i]) && i + 1 < args.length) {
                output = args[++i];
            }
        }
        return new ArgumentParser(input, output);
    }
}

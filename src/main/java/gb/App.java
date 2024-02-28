package gb;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        do {
            ConsoleUtils.printlnEmphasized("\nПАРАДОКС МОНТИ ХОЛЛА\n");
            OptionalInt answer = ConsoleUtils.askInteger(
                    "Введите число итераций"
                            + "(или пустой ввод чтобы завершить): ",
                    1, Integer.MAX_VALUE);
            if (answer.isEmpty()) {
                break;
            }
            int iterations = answer.getAsInt();
            ConsoleUtils
                    .printlnEmphasized("\nЭксперимент №1.\nИгрок остается при своем выборе.\n");
            HashMap<GameDescriptor, Boolean> exp1GameResults = doExperiment(false, iterations);
            printStatistics(exp1GameResults);
            ConsoleUtils
                    .printlnEmphasized(
                            "\nЭксперимент №2.\nИгрок меняет дверь на другую.\n");
            HashMap<GameDescriptor, Boolean> exp2GameResults = doExperiment(true, iterations);
            printStatistics(exp2GameResults);
        } while (ConsoleUtils.askYesNo("-\nПродолжить (Y/n)? ", true));
        ConsoleUtils.println("-\nПриложение завершено.");
    }
    //Обработка игр
    private static HashMap<GameDescriptor, Boolean> doExperiment(boolean swapDoor, int iterations) {
        HashMap<GameDescriptor, Boolean> results = IntStream.range(1, iterations + 1)
                .mapToObj(Game::new)
                .map(game -> {
                    game.playerMakeFirstChoice();
                    game.masterOpensABadDoor();
                    game.playerMakeSecondChoice(swapDoor);
                    return game;
                })
                .collect(Collectors.toMap(Game::getGameDescriptor, Game::isPlayerWinner, (g1, g2) -> g1, HashMap::new));
        return results;
    }
    private static void printStatistics(HashMap<GameDescriptor, Boolean> gameResults) {
        int iterations = gameResults.size();
        int wins = (int) gameResults.values().stream().filter(Boolean::booleanValue).count();
        int losses = iterations - wins;
        double winRatio = wins / (double) iterations;
        String stat1 = String.format("Всего игр: %d,\tИз них выигрышных: %d, \tпроигрышных: %d", iterations, wins,
                losses);
        String stat2 = String.format("Итого: статистическая вероятность выигрыша (по выборке): %s",
                NumberFormat.getPercentInstance().format(winRatio));
        ConsoleUtils.println(stat1);
        ConsoleUtils.println(stat2);
    }
}


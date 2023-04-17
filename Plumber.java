package plumber;

import org.jetbrains.annotations.NotNull;
import plumber.plumber_product.Drain;
import plumber.plumber_product.Pipe;
import plumber.plumber_product.PlumbingProduct;
import plumber.plumber_product.Source;

import java.util.*;

public class Plumber {


    /**
     * Игровое поле
     */
    private GameField gameField;
    /**
     * Источник и слив
     */
    PlumbingProduct source = null, drain = null;

    /**
     * Список всех труб
     */
    List<PlumbingProduct> pipeList;

    /**
     * Конструктор
     * @param gameField - игровое поле
     */
    Plumber(GameField gameField) {
        this.gameField = gameField;
    }


    /**
     * Создать конфигруации для водопровожда
     */
    public void configure() {

        createPipeline();
        source.fill(new Water());
        shufflePipeline();
    }


    /**
     * Создать собранный водопровод. По данному водопровооду вода может дойти от источника до слива
     */
    void createPipeline() {

        if (gameField.height() == gameField.width() && gameField.width() == 1) {
            throw new IllegalArgumentException("GameField is verysmall");
        }

        Cell startCell = gameField.cell(random(gameField.height()), 0);
        Cell finishCell = gameField.cell(random(gameField.height()), gameField.width() - 1);

        while (startCell == finishCell) {
            finishCell = gameField.cell(random(gameField.height()), gameField.width() - 1);
        }


        List<Cell> cellPath = createCellPath(startCell, finishCell);

        List<Direction> directionList = convertCellPathToDirectionPath(cellPath);
        source = new Source(directionList.get(0), cellPath.get(0));
        drain = new Drain(directionList.get(directionList.size() - 1).opposite(),
                cellPath.get(cellPath.size() - 1));

        pipeList = createPipePath(startCell, directionList);

    }


    /**
     * Нарушить целостность водопровода путем поворота некоторых труб
     */
    void shufflePipeline() {

        for (PlumbingProduct plumbingProduct : pipeList) {

            int rotationCnt = random(4);

            for (int i = 0; i < rotationCnt; i++) {
                Pipe pipe = (Pipe) plumbingProduct;
                pipe.rotate();
            }

        }
    }

    /**
     * Функция рандома. Диапозон значений [0, n)
     * @param n - правая граница для рандома
     * @return рандомное число
     */
    private int random(int n) {
		if( n <= 0){
			throw new IllegalAccessException("illegal argument");
		}
        return new Random().nextInt(n);
    }


    /**
     * Создать путь от одной клетки до другой
     * @param startCell - первая клетка
     * @param finishCell - конечная клетка
     * @return список клеток, образующий путь от первой клетки до конечной
     */
    private List<Cell> createCellPath(@NotNull Cell startCell, @NotNull Cell finishCell) {


        Map<Cell, Cell> preCell = new HashMap<>();

        dfs(startCell, preCell, startCell);


        if (preCell.get(finishCell) == null) {
            return null;
        }

        List<Cell> cellPath = new ArrayList<>();

        Cell cell = finishCell;


        while (cell != null) {
            cellPath.add(cell);
            cell = preCell.get(cell);
        }

        Collections.reverse(cellPath);

        System.out.println(cellPath.size());

        for (Cell cell1 : cellPath) {
            System.out.println(cell1.row() + " " + cell1.col());
        }

        return cellPath;
    }


    /**
     * Обход в ширину, который определяет для каждой клетки ее предка
     * @param currentCell - текущая клетка
     * @param preCell - словарь. Ключ - клетка. Значение - клетка предок
     * @param startCell - Самая первая клетка последовательности
     */
    private void dfs(@NotNull Cell currentCell, @NotNull Map<Cell, Cell> preCell, @NotNull Cell startCell) {

        List<Cell> neighborList = new ArrayList<>(currentCell.neighbors().values());
        Collections.shuffle(neighborList);

        for (Cell neighbor : neighborList) {

            boolean isVisited = neighbor.equals(startCell) || preCell.get(neighbor) != null;

            if (isVisited == false) {
                preCell.put(neighbor, currentCell);
                dfs(neighbor, preCell, startCell);
            }
        }
    }

    /**
     * Конвертировать путь из клеток в путь направлений
     * @param cellPath - путь из клеток
     * @return список направлений, который представляет собой путь направлений
     */
    private List<Direction> convertCellPathToDirectionPath(@NotNull List<Cell> cellPath) {

        List<Direction> directionList = new ArrayList<>();

        for (int i = 1; i < cellPath.size(); i++) {
            Direction direction = cellPath.get(i - 1).neighborDirection(cellPath.get(i));

            if (direction == null) {
                throw new IllegalArgumentException("Illegal createCellPath");
            }

            directionList.add(direction);
        }

        return directionList;
    }

    /**
     * Создать путь из труб на основе стартовой клетки, в которой распологается источник и пути направлений
     * @param cell - стартовая клетка
     * @param directionList - путь направлений
     * @return Список труб, которые образуют собой полноценный целостный путь
     */
    private List createPipePath(@NotNull Cell cell, @NotNull List<Direction> directionList) {

        List<PlumbingProduct> pipeList = new ArrayList<>();
        cell = cell.neighbor(directionList.get(0));

        for (int i = 1; i < directionList.size(); i++) {
            Set<Direction> directions = new HashSet<>(List.of(new Direction[]{directionList.get(i - 1).opposite(), directionList.get(i)}));
            PlumbingProduct pipe = new Pipe(directions, cell);
            pipeList.add(pipe);
            cell = cell.neighbor(directionList.get(i));
        }

        return pipeList;
    }

}

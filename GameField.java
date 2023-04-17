package plumber;

import java.util.*;

public class GameField {

    /**
     * Высота игрового поля
     */
    private int height = 20;
    /**
     * Ширина игрового поля
     */
    private int width = 20;

    /**
     * Список всех клеток
     */
    private List<Cell> cellList = new ArrayList<>();

    /**
     * Сантехник, что раставляет водопровод на поле
     */
    private Plumber plumber;


//   ------------------------------ Конструктор ---------------------------------------

    /**
     * Конструктор
     */
    public GameField() {
        new GameField(10, 10);
    }

    /**
     * Конструктор
     *
     * @param height - высота поля
     * @param width  - ширина поля
     */
    public GameField(int height, int width) {

        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("illegal height or illegal width");
        }

        this.height = height;
        this.width = width;

        buildField();
    }


//------------------------------Геттеры --------------------------------------------

    /**
     * Получить ширину поля
     *
     * @return ширина поля
     */
    public int width() {
        return width;
    }

    /**
     * Получить высоту поля
     *
     * @return высота поля
     */
    public int height() {
        return height;
    }

    /**
     * Построить поле из клеток
     */
    private void buildField() {

        // Создаем ячейки
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                Cell cell = new Cell(row, col);
                cellList.add(cell);
            }
        }

        // Связываем ячейки
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {

                Cell cell = cell(row, col);

                if (height() > 1 && row < height() - 1) {
                    cell.setNeighbor(Direction.south(), cell(row + 1, col));
                }
                if (row > 0) {
                    cell.setNeighbor(Direction.north(), cell(row - 1, col));
                }
                if (width() > 1 && col < width() - 1) {
                    cell.setNeighbor(Direction.east(), cell(row, col + 1));
                }
                if (col > 0) {
                    cell.setNeighbor(Direction.west(), cell(row, col - 1));
                }
            }
        }
    }


    /**
     * Получить клетке по строке и колонке
     * @param row - строка
     * @param col - ширина
     * @return клетка
     */
    public Cell cell(int row, int col) {

        if (row >= height() || col >= width() || row < 0 || col < 0) {
            throw new IllegalArgumentException("Illegal arguments");
        }

        int index = row * width() + col;
        return cellList.get(row * width() + col);
    }


}

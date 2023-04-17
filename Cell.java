package plumber;

import org.jetbrains.annotations.NotNull;
import plumber.plumber_product.PlumbingProduct;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Cell {

    /**
     * строка в которой расположена клетка
     */
    private int row;

    /**
     * колонка в которой расположена клетка
     */
    private int col;


    //------------------------------- Конструктор ---------------------------------------


    /**
     *  геттер для строки
     * @return строка в которой расположена клетка
     */
    public int row() {
        return row;
    }

    /**
     *  геттер для колонки
     * @return колонка в которой расположена клетка
     */
    public int col() {
        return col;
    }

    /**
     * Конструктор
     * @param row строка
     * @param col колонка
     */
    public Cell(int row, int col) {

        if (!isValid(row, col)){
            throw  new IllegalArgumentException("Illegal row or col");
        }

        this.row = row;
        this.col = col;
    }

    /**
     * Проверяет валидная ли строка и колонка для клетки
     * @param row строка
     * @param col колонка
     * @return
     */
    public static boolean isValid(int row, int col) {
        return col >= 0 && row >= 0;
    }

    //------------------------ работа с содержимым -------------------------------------------------

    /**
     * сантехническое изделие которое хранится в клетке
     */
    private PlumbingProduct plumbingProduct;

    /**
     * Заполнить клетку сантехническим изделием
     * @param plumbingProduct - сантехничеческое изделие
     */
    public void fill(PlumbingProduct plumbingProduct) {

        this.plumbingProduct = plumbingProduct;
    }

    /**
     * очистить клетку
     */
    public void clear() {
        this.plumbingProduct = null;
    }

    /**
     * Проверяет заполнена ли клетка
     * @return true, если клетка заполнена, иначе false
     */
    public boolean isFilled() {
        return plumbingProduct != null;
    }

    /**
     * получить сантехническое изделие
     * @return сантехническое изделие, хранящееся в клетке
     */
    public PlumbingProduct getPlumbingProduct() {
        return plumbingProduct;
    }


    // ---------------------- Соседи -----------------------

    /**
     * Словарь Соседей. Ключ - направление, в котором расположен сосед. Значение - сам сосед
     */
    private final Map<Direction, Cell> _neighbors = new HashMap<>();

    /**
     * Получить соседа по заданному направлению
     * @param direct - направление
     * @return сосед
     */
    public Cell neighbor(Direction direct) {

        if (_neighbors.containsKey(direct)) {
            return _neighbors.get(direct);
        }

        return null;
    }

    /**
     * Получить Словарь соседей
     * @return словарь соседей
     */
    public Map<Direction, Cell> neighbors() {
        return Collections.unmodifiableMap(_neighbors);
    }

    /**
     * Присвоить соседа по направлению
     * @param direct - направление
     * @param neighbor - сосед
     */
    public void setNeighbor(@NotNull Direction direct, @NotNull Cell neighbor) {
        if (neighbor != this && !isNeighbor(neighbor) && this.neighbor(direct) == null) {
            _neighbors.put(direct, neighbor);
            neighbor.setNeighbor(direct.opposite(), this);
        }
    }

    /**
     * Проверяет является ли клетка соседом
     * @param other - другая клетка
     * @return true - если клетки являются соседями , иначе false
     */
    public boolean isNeighbor(Cell other) {
        return _neighbors.containsValue(other);
    }


    /**
     * Получить направление, в котором расположен сосед
     * @param neighbor - сосед
     * @return направление, в котором расположен сосед.
     */
    public Direction neighborDirection(@NotNull Cell neighbor) {

        Direction direction = null;
        for (Map.Entry<Direction, Cell> entry : this.neighbors().entrySet()) {

            if (neighbor.equals(entry.getValue()))
                direction = entry.getKey();
        }

        return direction;
    }


    /**
     * Проверяет эквивалентность объектов
     * @param other - другой объект
     * @return true - если объекты эквивалентны, иначе false
     */
    @Override
    public boolean equals(Object other) {

        if (other instanceof Cell) {
            // “ипы совместимы, можно провести преобразование
            Cell otherCell = (Cell) other;
            // ¬озвращаем результат сравнени¤ углов
            return this.row == otherCell.row && this.col == otherCell.col;
        }

        return false;
    }

    /**
     * Получить хэш
     * @return хэш
     */
    @Override
    public int hashCode() {
        return row() * 1000 + col();
    }

    /**
     * Конвертировать в строку
     * @return строка
     */
    @Override
    public String toString() {
        return "(" + this.row + ";" + this.col + ")";
    }
}

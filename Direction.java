package plumber;

/**
 * Direction - абстракци¤ направлени¤ в системе координат "север-юг-восток-запад";
 * позвол¤ет сравнивать направлени¤ и порождать новые направлени¤ относительно
 * текущего
 */
public class Direction {

    /**
     *  группа градусы
     */
    private int _angle = 90;

    /**
     * Констурктор
     * @param angle -градусы
     */
    private Direction(int angle) {
        // ѕриводим заданный угол к допустимому диапазону
        angle = angle % 360;
        if (angle < 0) angle += 360;

        this._angle = angle;
    }

    // ------------------ ¬озможные направлени¤ ---------------------


    /**
     * Вернуть направление-севеир
     * @return направление-север
     */
    public static Direction north() {
        return new Direction(90);
    }

    /**
     * Вернуть направление-юг
     * @return направление-юг
     */
    public static Direction south() {
        return new Direction(270);
    }

    /**
     * Вернуть направление-восток
     * @return направление-восток
     */
    public static Direction east() {
        return new Direction(0);
    }

    /**
     * Вернуть направление-запад
     * @return направление-запад
     */
    public static Direction west() {
        return new Direction(180);
    }


    // ------------------ Ќовые направлени¤ ---------------------

    /**
     * Создать клон объекта
     * @return клон объекта
     */
    @Override
    public Direction clone() {
        return new Direction(this._angle);
    }

    /**
     * Повернуть направление по часовой
     * @return направление
     */
    public Direction clockwise() {
        return new Direction(this._angle - 90);
    }

    /**
     * Повернуть направление против часовой
     * @return направление
     */
    public Direction anticlockwise() {
        return new Direction(this._angle + 90);
    }

    /**
     * Получить противоположное направление
     * @return направление
     */
    public Direction opposite() {
        return new Direction(this._angle + 180);
    }

    /**
     * Проверяет является ли данное направление противопожное другому
     * @param other - другое направление
     * @return
     */
    public boolean isOpposite(Direction other) {
        return this.opposite().equals(other);
    }

    // ------------------ —равнить направлени¤ ---------------------

    /**
     * Проверяет эквивалентность объектов
     * @param other - другой объект
     * @return true - если объекты эквивалентны, иначе false
     */
    @Override
    public boolean equals(Object other) {

        if (other instanceof Direction) {
            // “ипы совместимы, можно провести преобразование
            Direction otherDirect = (Direction) other;
            // ¬озвращаем результат сравнени¤ углов
            return _angle == otherDirect._angle;
        }

        return false;
    }

    /**
     * Получить хэш
     * @return хэш
     */
    @Override
    public int hashCode() {
        return this._angle;
    }


    /**
     * Конвертировать в строку
     * @return строка
     */
    @Override
    public String toString() {
        String res = "South";

        if(this.equals(Direction.north())){
            res = "North";
        }

        if (this.equals(Direction.east())){
            res = "East";
        }

        if(this.equals(Direction.west())){
            res = "West";
        }

        return res;
    }

}
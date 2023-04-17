package plumber;

import org.jetbrains.annotations.NotNull;
import plumber.events.WaterStoppedActionEvent;
import plumber.events.WaterStoppedActionListener;
import plumber.plumber_product.PlumbingProduct;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Water implements ActionListener {

    /**
     * Последняя труба, заполненная водой
     */
    private PlumbingProduct lastFillingPlumbingProduct;

    /**
     * Таймер
     */
    private Timer timer;

    /**
     * таймаут
     */
    private int timeout = 2000;

    /**
     * Переменная, коазывающая остановлена ли вода или нет
     */
    private boolean isStopped = false;

    /**
     * Конструктор
     */
    public Water() {
        this(2000);
    }

    /**
     * Конструктор
     *
     * @param timeout - таймаут
     */
    public Water(int timeout) {
        if (timeout < 1)
            throw new IllegalArgumentException("Illegal timeoutargument");

        this.timeout = timeout;
        timer = new Timer(timeout, this);
    }

    public PlumbingProduct getLastFillingPlumbingProduct() {
        return lastFillingPlumbingProduct;
    }


    /**
     * Заставить воду течь
     */
    public void flow() {

        boolean result = false;
        for (Direction direction : getLastFillingPlumbingProduct().getEnds()) {
            result = nextConnection(direction);

            if (result == true) {
                break;
            }
        }

        isStopped = !result || isStopped;
    }


    /**
     * Запустить таймнер, по которому работает вода
     */
    public void start() {
        timer.start();
    }

    /**
     * Установить новое значение для последней посещенной клетки
     *
     * @param plumbingProduct
     */
    public void nextPlumbingProduct(@NotNull PlumbingProduct plumbingProduct) {


        if (plumbingProduct.isFilled() == false) {
            plumbingProduct.fill(this);
        }

        if (this.equals(plumbingProduct.water()))
            this.lastFillingPlumbingProduct = plumbingProduct;

    }

    /**
     * Перейти к следующему соединенному участку водопрова
     *
     * @param direction направление в которое мы движемся
     * @return true - если мы смогли прийти, иначе false
     */
    private boolean nextConnection(@NotNull Direction direction) {

        if (getLastFillingPlumbingProduct() == null)
            return false;

        boolean result = true;
        PlumbingProduct neighbor = getLastFillingPlumbingProduct().neighbor(direction);

        if (neighbor != null) {

            boolean flag = getLastFillingPlumbingProduct().isCanFilled(neighbor);

            if (flag) {
                nextPlumbingProduct(neighbor);
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        return result;
    }

    private void stop() {
        timer.stop();
        fireWaterAction();
    }


    /**
     * Обработчик событий, необходимый для обработки событий таймер
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isStopped) {
            stop();
        } else {
            flow();
        }
    }

    // ---------------------- геттеры -------------------

    /**
     * Геттер для таймаута
     *
     * @return таймаут
     */
    public int timeout() {
        return timeout;
    }

    /**
     * Проверить, остановлена ли вода
     *
     * @return true - если вода остановле, иначе false
     */
    public boolean isStopped() {
        return isStopped;
    }

    //------  Работа со слушателями------------------------

    //TODO    !!!
    List<WaterStoppedActionListener> FlowActionListeners = new ArrayList<>();

    // присоединяет слушателя
    public void addWaterStoppedActionListener(WaterStoppedActionListener l) {

        if (FlowActionListeners.contains(l) == false)
            FlowActionListeners.add(l);
    }

    // отсоединяет слушателя
    public void removeFlowActionListener(WaterStoppedActionListener l) {
        if (FlowActionListeners.contains(l)) {
            FlowActionListeners.remove(l);
        }
    }

    // оповещает слушателей о событии
    protected void fireWaterAction() {
        for (WaterStoppedActionListener FlowActionListener : FlowActionListeners) {
            FlowActionListener.waterStopped(new WaterStoppedActionEvent(this));
        }
    }


}

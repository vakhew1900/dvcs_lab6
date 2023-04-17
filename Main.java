package plumber;

import plumber.plumber_product.Drain;
import plumber.plumber_product.PlumbingProduct;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Game game = new Game();

        Scanner scanner = new Scanner(System.in);

        while (true) {


            game.printAllPipeline();
            System.out.println("Enter 1 x y for rotate plumber in cell (x; y). Enter 0 for launch water");
            int typeCommand = scanner.nextInt();

            if (typeCommand == 1) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                try {
                    game.rotate(x, y);
                } catch (IllegalArgumentException e) {
                    System.out.println("ошибка неверные данные");
                }

            } else {
                game.flowWater();
                break;
            }
        }
    }

}
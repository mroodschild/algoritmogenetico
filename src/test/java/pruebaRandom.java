
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class pruebaRandom {

    public static void main(String[] args) {
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            System.out.println("r:\t" + r.nextInt(10));

        }

    }
}

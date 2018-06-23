package org.gitia.algoritmogenetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Main {

    Random r = new Random();

    public static void main(String[] args) {

        //variables
        int largo = 10;
        int numPoblacion = 20;
        int pressure = 3;
        double mutation_chance = 0.2;

        double[] m = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        SimpleMatrix modelo = new SimpleMatrix(1, largo, true, m);
        modelo.print();

        Main main = new Main();
        //main.individual(1, 10, 10).print();

        List<SimpleMatrix> poblacion = main.crearPoblacion(numPoblacion, 1, 10, largo);
        System.out.println("Poblacion inicial");
        main.printPoblacion(poblacion);

        for (int i = 0; i < 1000; i++) {
            poblacion = main.selection_and_reproduction(poblacion, modelo, pressure, mutation_chance);
        }

        System.out.println("Poblacion Final");
        main.printPoblacion(poblacion);

    }

    public SimpleMatrix individual(int min, int max, int largo) {

        SimpleMatrix gen = new SimpleMatrix(1, largo);

        for (int i = 0; i < largo; i++) {
            gen.set(i, r.nextInt(max - min) + min);
        }

        return gen;
    }

    public List<SimpleMatrix> crearPoblacion(int cantidad, int min, int max, int largo) {
        List<SimpleMatrix> poblacion = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            poblacion.add(individual(min, max, largo));
        }

        return poblacion;
    }

    
    public void printPoblacion(List<SimpleMatrix> poblacion) {
        SimpleMatrix p = new SimpleMatrix(poblacion.size(), poblacion.get(0).numCols());

        for (int i = 0; i < poblacion.size(); i++) {
            SimpleMatrix pop = poblacion.get(i);
            p.setRow(i, 0, pop.getDDRM().getData());
        }

        p.print();
    }

    public double calcularFitness(SimpleMatrix individuo, SimpleMatrix modelo) {
        double fitness = 0;
        for (int i = 0; i < individuo.numCols(); i++) {
            if (individuo.get(i) == modelo.get(i)) {
                fitness++;
            }
        }
        return fitness;
    }

    public List<SimpleMatrix> selection_and_reproduction(List<SimpleMatrix> population,
            SimpleMatrix modelo, int pressure, double mutation_chance) {

        List<Fit> clasificacion = new ArrayList<>();

        //realizamos la puntuacion
        for (int i = 0; i < population.size(); i++) {
            clasificacion.add(new Fit(i, calcularFitness(population.get(i), modelo)));
        }

        Collections.sort(clasificacion, new FitComparatorMaxMin());
        
        //cruzamiento
        int sizeCruzamiento = population.get(0).numCols();
        for (int i = pressure; i < clasificacion.size(); i++) {
            int punto = r.nextInt(sizeCruzamiento - 1) + 1;
            int padre1 = r.nextInt(clasificacion.size() - pressure);
            int padre2 = r.nextInt(clasificacion.size() - pressure);
            while (padre1 == padre2) {
                padre2 = r.nextInt(clasificacion.size() - pressure);
            }

            SimpleMatrix hijo = population.get(clasificacion.get(i).getKey());
            SimpleMatrix p1 = population.get(clasificacion.get(padre1).getKey());
            SimpleMatrix p2 = population.get(clasificacion.get(padre2).getKey());

            hijo.setRow(0, 0, p1.extractMatrix(0, SimpleMatrix.END, 0, punto).getDDRM().getData());
            hijo.setRow(0, punto, p2.extractMatrix(0, SimpleMatrix.END, punto, SimpleMatrix.END).getDDRM().getData());

            population.set(clasificacion.get(i).getKey(), hijo);

//            System.out.println("punto:\t" + punto);
//            p1.print();
//            p2.print();
//            hijo.print();
        }

        //mutacion
        for (int i = pressure; i < clasificacion.size(); i++) {
            if (r.nextDouble() <= mutation_chance) {
                int punto = r.nextInt(population.get(0).numCols());
                int new_val = r.nextInt(9);
                SimpleMatrix hijo = population.get(clasificacion.get(i).getKey());

                hijo.set(punto, new_val);

                population.set(clasificacion.get(i).getKey(), hijo);
            }
        }
        return population;
    }

}

/*
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitia.algoritmogenetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.gitia.tipo3.crossover.Crossover;
import org.gitia.tipo3.fitness.Fitness;
import org.gitia.tipo3.fitness.FitnessCuadratic;
import org.gitia.tipo3.mutation.Mutation;
import org.gitia.tipo3.population.Individuo;
import org.gitia.tipo3.population.IndividuoComparator;
import org.gitia.tipo3.population.Population;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class AG1 {

    int epoch;
    int populationSize;
    int dnaSize;
    double elite;
    double mutation;
    double min = -3;
    double max = 3;
    List<Individuo> population;
    Fitness fitness = new FitnessCuadratic();

    public AG1() {
        this(50, 20, 10, 0.2, 0.2);
    }

    public AG1(int epoch, int populationSize, int dnaSize, double elite, double mutation) {
        this.epoch = epoch;
        this.populationSize = populationSize;
        this.elite = elite;
        this.dnaSize = dnaSize;
        this.mutation = mutation;
    }

    public AG1(int epoch, int populationSize, int dnaSize, double elite, double mutation, double min, double max) {
        this.epoch = epoch;
        this.populationSize = populationSize;
        this.dnaSize = dnaSize;
        this.elite = elite;
        this.mutation = mutation;
        this.min = min;
        this.max = max;
    }

    public void run() {
        //initial population
        int numElite = (int) (populationSize * elite);
        int numMutacion = (int) (populationSize * mutation);
        int numSeleccion = (int) (populationSize * 0.05);
        int offspring = (int) (populationSize - (numElite + numMutacion + numSeleccion));
        List<Individuo> listElite = new ArrayList<>();
        List<Individuo> poblacionSeleccionada = new ArrayList<>();

        population = Population.generate(populationSize, dnaSize, min, max);
        //iterations
        fitness.fit(population);
        // separar la elite

        for (int i = 0; i < epoch; i++) {
            //fit y orden de menor a mayor
            Collections.sort(population, new IndividuoComparator());
            listElite.clear();
            separarElite(population, listElite, numElite, poblacionSeleccionada);
            //elite
            //seleccionamos para hacer el cruzamiento
            double[][] paring = Ruleta.getParing(offspring, population);
            //realizamos el cruzamiento
            //guardamos el cruzamiento
            //de la parte que no se cruzará
            //seleccionamos al azar los que se mutarán
            //mutamos y dejamos pasar a los que no se mutan
            //unimos el offspring, con los mutados, los seleccionados y la elite
            Crossover.crossover(population, elite);
            //offspring
            //mutation
            Mutation.mutation(population, mutation, elite, min, max);
            //end?
            fitness.fit(population);
            fitness.printResume(population, i);
        }
        //return solution
    }

    /**
     *
     * @param population poblacion a guardar la elite
     * @param listElite individuos guardados como elite
     * @param numElite cantidad de individuos a guardar
     * @param poblacionSeleccionada poblacion para el resto de las operaciones
     */
    private void separarElite(List<Individuo> population,
            List<Individuo> listElite, int numElite,
            List<Individuo> poblacionSeleccionada) {
        for (int i = populationSize - 1; i >= 0; i--) {
            if (i >= (populationSize - elite - 1)) {
                Individuo ind = new Individuo(population.get(i).getDna().copy(), population.get(i).getFitness());
                listElite.add(ind);
            }else{
                Individuo ind = new Individuo(population.get(i).getDna().copy(), population.get(i).getFitness());
                poblacionSeleccionada.add(ind);
            }
        }
    }

    public void setElite(double elite) {
        this.elite = elite;
    }

    public void setMutation(double mutation) {
        this.mutation = mutation;
    }

    public double getElite() {
        return elite;
    }

    public double getMutation() {
        return mutation;
    }

    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setPopulation(List<Individuo> population) {
        this.population = population;
    }

    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

}

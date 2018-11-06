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
package org.gitia.tipo3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.gitia.tipo3.crossover.CubeCrossOver;
import org.gitia.tipo3.fitness.Fitness;
import org.gitia.tipo3.fitness.FitnessCuadratic;
import org.gitia.tipo3.mutation.MultiNonUniformMutation;
import org.gitia.tipo3.population.Individuo;
import org.gitia.tipo3.population.IndividuoComparator;
import org.gitia.tipo3.population.Population;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class AG1 {

    int genMax;
    int populationSize;
    int dnaSize;
    double elite_porcentaje;
    double mutation;
    double min = -1;
    double max = 1;
    List<Individuo> population;
    Fitness fitness = new FitnessCuadratic();

    public AG1() {
        this(50, 20, 10, 0.2, 0.2);
    }

    /**
     * 
     * @param epoch cantidad de epocas
     * @param populationSize tamaño de la población
     * @param dnaSize tamaño del adn
     * @param elite porcentaje de elite
     * @param mutation porcentaje de mutación
     */
    public AG1(int epoch, int populationSize, int dnaSize, double elite, double mutation) {
        this.genMax = epoch;
        this.populationSize = populationSize;
        this.elite_porcentaje = elite;
        this.dnaSize = dnaSize;
        this.mutation = mutation;
    }

    /**
     * 
     * @param epoch cantidad de epocas
     * @param populationSize tamaño de la población
     * @param dnaSize tamaño del adn
     * @param elite porcentaje de elite
     * @param mutation porcentaje de mutación
     * @param min valores minimos
     * @param max valores maximos
     */
    public AG1(int epoch, int populationSize, int dnaSize, double elite, double mutation, double min, double max) {
        this.genMax = epoch;
        this.populationSize = populationSize;
        this.dnaSize = dnaSize;
        this.elite_porcentaje = elite;
        this.mutation = mutation;
        this.min = min;
        this.max = max;
    }

    public void run() {
        //initial population
        int numElite = (int) (populationSize * elite_porcentaje);
        int numMutacion = (int) (populationSize * mutation);
        int numSeleccion = (int) (populationSize * 0.05);
        int offspring = (int) (populationSize - (numElite + numMutacion + numSeleccion));
        List<Individuo> listElite = new ArrayList<>();
        List<Individuo> listPoblacionSeleccionada = new ArrayList<>();
        List<Individuo> listOffspring = new ArrayList<>();
        List<Individuo> listNoCruzada = new ArrayList<>();

        population = Population.generate(populationSize, dnaSize, min, max);
        //iterations
        fitness.fit(population);
        // separar la elite

        for (int genAct = 0; genAct < genMax; genAct++) {
            //fit y orden de menor a mayor
            Collections.sort(population, new IndividuoComparator());
            listElite.clear();
            separarElite(population, listElite, numElite, listPoblacionSeleccionada);
            //elite
            //seleccionamos para hacer el cruzamiento
            int[][] paring = Ruleta.getParing(offspring, population);
            //separarOffspringSelNoCruzada(paring, population, listPoblacionSeleccionada, listCrossOver, listNoCruzada);
            //realizamos el cruzamiento
            //guardamos el cruzamiento
            //de la parte que no se cruzará
            //seleccionamos al azar los que se mutarán
            //mutamos y dejamos pasar a los que no se mutan
            //unimos el offspring, con los mutados, los seleccionados y la elite
            listOffspring = CubeCrossOver.crossover(population, paring);
            listNoCruzada = separarSeleccionNoCruzada(paring, population, numElite);
            MultiNonUniformMutation.mutacion(listNoCruzada, numMutacion, min, max, genAct, genMax);

            //unimos las partes y reemplazamos por la nueva generación de individuos
            population = joinListas(listOffspring, listNoCruzada, listElite);
            cleanListas(listOffspring, listNoCruzada, listElite);
            //evaluamos los individuos nuevamente
            fitness.fit(population);
            // mostramos los resultados
            fitness.printResume(population, genAct);
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
            if (i >= (populationSize - numElite - 1)) {
                Individuo ind = new Individuo(population.get(i).getDna().copy(), population.get(i).getFitness());
                listElite.add(ind);
            } else {
                Individuo ind = new Individuo(population.get(i).getDna().copy(), population.get(i).getFitness());
                poblacionSeleccionada.add(ind);
            }
        }
    }

    /**
     * unimos las partes y devolvemos la lista completa
     * @param listOffspring
     * @param listNoCruzada
     * @param listElite
     * @return 
     */
    private List<Individuo> joinListas(List<Individuo> listOffspring, List<Individuo> listNoCruzada, List<Individuo> listElite) {
        List<Individuo> aux = new ArrayList<>();
        aux.addAll(listOffspring);
        aux.addAll(listNoCruzada);
        aux.addAll(listElite);
        return aux;
    }
    
     private void cleanListas(List<Individuo> listOffspring, List<Individuo> listNoCruzada, List<Individuo> listElite) {
        listElite.clear();
        listNoCruzada.clear();
        listOffspring.clear();
    }


    private List<Individuo> separarSeleccionNoCruzada(int[][] paring, List<Individuo> population, int numElite) {
        List<Individuo> selNoCruzada = new ArrayList<>();
        int[] indicesCruzados = new int[paring.length * 2];
        int[] indicesPoblacion = new int[population.size()];
        int[] idxNoCruzados;
        int k = 0;
        //ponemos los padres en un listado
        for (int[] paring1 : paring) {
            for (int j = 0; j < paring[0].length; j++) {
                indicesCruzados[k++] = paring1[j];
            }
        }
        //quitamos los padres repetidos
        indicesCruzados = java.util.stream.IntStream.of(indicesCruzados).distinct().toArray();

        //creamos los indices de la población
        for (int i = 0; i < population.size(); i++) {
            indicesPoblacion[i] = i;
        }

        // removemos los indices que ya fueron cruzados
        idxNoCruzados = ArrayUtils.removeElements(indicesPoblacion, indicesCruzados);

        //generamos la lista de los no cruzados
        for (int i = 0; i < idxNoCruzados.length; i++) {
            selNoCruzada.add(new Individuo(population.get(idxNoCruzados[i]).getDna(), population.get(idxNoCruzados[i]).getFitness()));
        }
        return selNoCruzada;
    }

    public void setElite(double elite) {
        this.elite_porcentaje = elite;
    }

    public void setMutation(double mutation) {
        this.mutation = mutation;
    }

    public double getElite() {
        return elite_porcentaje;
    }

    public double getMutation() {
        return mutation;
    }

    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
    }

    public void setEpoch(int epoch) {
        this.genMax = epoch;
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
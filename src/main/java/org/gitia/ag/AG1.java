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
package org.gitia.ag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.gitia.ag.compite.Tournament;
import org.gitia.ag.crossover.CubeCrossOver;
import org.gitia.ag.fitness.Fitness;
import org.gitia.ag.fitness.FitnessCuadratic;
import org.gitia.ag.mutation.MultiNonUniformMutation;
import org.gitia.ag.population.Individuo;
import org.gitia.ag.population.IndividuoComparator;
import org.gitia.ag.population.Population;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class AG1 {

    protected int genMax;
    protected int popSize;
    protected int dnaSize;
    protected double elite_porcentaje;
    protected double offspring_porcentaje;
    protected double mutation;
    protected double min = -1;
    protected double max = 1;
    protected int tournament_size;
    protected List<Individuo> population;
    protected List<Individuo> listElite = new ArrayList<>();
    protected List<Individuo> listPoblacionSeleccionada = new ArrayList<>();
    protected List<Individuo> listOffspring;
    protected List<Individuo> listNoCruzada;
    protected Fitness fitness = new FitnessCuadratic();
    protected Random r = new Random();
    protected int numElite;
    protected int numMutacion;
    protected int numOffspring;
    protected int numSeleccion;

    public AG1() {
        //this(50, 20, 10, 0.6, 0.2, 0.2, 2);
    }

    /**
     *
     * @param epoch cantidad de epocas
     * @param populationSize tamaño de la población
     * @param dnaSize tamaño del adn
     * @param offspring porcentaje de numOffspring
     * @param elite porcentaje de elite
     * @param mutation porcentaje de mutación
     * @param tournament_size tamaño del torneo (no mayor al diez por ciento) en cantidad
     */
    public AG1(int epoch, int populationSize, int dnaSize, double offspring, double elite, double mutation, int tournament_size) {
        this(epoch, populationSize, dnaSize, offspring, elite, mutation, tournament_size, -1, 1);
    }

    /**
     *
     * @param epoch cantidad de epocas
     * @param populationSize tamaño de la población
     * @param dnaSize tamaño del adn
     * @param offspring porcentaje de numOffspring
     * @param elite porcentaje de elite
     * @param mutation porcentaje de mutación
     * @param tournament_size tamaño del torneo (no mayor al diez por ciento) en cantidad
     * @param min valores minimos
     * @param max valores maximos
     */
    public AG1(int epoch, int populationSize, int dnaSize, double offspring, double elite, double mutation, int tournament_size, double min, double max) {
        this.genMax = epoch;
        this.popSize = populationSize;
        this.dnaSize = dnaSize;
        this.elite_porcentaje = elite;
        this.mutation = mutation;
        this.min = min;
        this.max = max;
        this.tournament_size = tournament_size;
        this.offspring_porcentaje = offspring;
        //initial population
        numElite = (int) (popSize * elite_porcentaje);
        numMutacion = (int) (popSize * mutation);
        numOffspring = (int) (popSize * offspring_porcentaje);
        numSeleccion = popSize - (numElite + numMutacion + numOffspring);
        population = Population.generate(popSize, dnaSize, min, max);

        System.out.println("elite porcentaje:\t" + elite_porcentaje
                + "\tmutación porcentaje\t" + mutation
        );
        System.out.println("Poblacion\t" + population.size() + "\telite\t" + numElite
                + "\tnumMutacion\t" + numMutacion
                + "\tnumSeleccion\t" + numSeleccion
                + "\tnumOffspring\t" + numOffspring
        );
    }

    public void run() {

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
            int[][] paring = Tournament.getParing(numOffspring, population, tournament_size);
            //separarOffspringSelNoCruzada(paring, population, listPoblacionSeleccionada, listCrossOver, listNoCruzada);
            //realizamos el cruzamiento
            //guardamos el cruzamiento
            //de la parte que no se cruzará
            //seleccionamos al azar los que se mutarán
            //mutamos y dejamos pasar a los que no se mutan
            //unimos el numOffspring, con los mutados, los seleccionados y la elite
            listOffspring = CubeCrossOver.crossover(population, paring);
            // a la poblacion le quitamos la elite, quitamos los que se cruzaron
            // y indicamos cuantos deben pasar (num Seleccion + num mutacion)
            listNoCruzada = separarSeleccionNoCruzada(paring, population, numElite, numSeleccion + numMutacion);
            MultiNonUniformMutation.mutacion(listNoCruzada, numMutacion, min, max, genAct, genMax);

            //unimos las partes y reemplazamos por la nueva generación de individuos
            population.clear();
            population = joinListas(listOffspring, listNoCruzada, listElite);
            cleanListas(listOffspring, listNoCruzada, listElite, listPoblacionSeleccionada);
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
    protected void separarElite(List<Individuo> population,
            List<Individuo> listElite, int numElite,
            List<Individuo> poblacionSeleccionada) {
        for (int i = popSize - 1; i >= 0; i--) {
            if (i >= (popSize - numElite)) {
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
     *
     * @param listOffspring
     * @param listNoCruzada
     * @param listElite
     * @return
     */
    protected List<Individuo> joinListas(List<Individuo> listOffspring, List<Individuo> listNoCruzada, List<Individuo> listElite) {
        List<Individuo> aux = new ArrayList<>();
        aux.addAll(listOffspring);
        aux.addAll(listNoCruzada);
        aux.addAll(listElite);
        return aux;
    }

    protected void cleanListas(List<Individuo> listOffspring, List<Individuo> listNoCruzada, List<Individuo> listElite, List<Individuo> listPoblacionSeleccionada) {
        listElite.clear();
        listNoCruzada.clear();
        listOffspring.clear();
        listPoblacionSeleccionada.clear();
    }

    protected List<Individuo> separarSeleccionNoCruzada(int[][] paring, List<Individuo> population, int numElite, int sobreviven) {
        List<Individuo> selNoCruzada = new ArrayList<>();
        List<Individuo> sobrevivientes = new ArrayList<>();
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
        //seleccionamos los sobrevivientes
        int[] idxSobreviven = new int[sobreviven];
        for (int i = 0; i < sobreviven; i++) {
            int idx = r.nextInt(selNoCruzada.size());
            while (ArrayUtils.contains(idxSobreviven, idx)) {
                idx = r.nextInt(selNoCruzada.size());
            }
            idxSobreviven[i] = idx;
        }
        //capturamos a los sobrevivientes
        for (int i = 0; i < idxSobreviven.length; i++) {
            sobrevivientes.add(selNoCruzada.get(idxSobreviven[i]));
        }
        return sobrevivientes;
    }
    
    protected void print(int[][] paring) {
        int i = 0;
        for (int[] is : paring) {
            System.out.printf("%d: ", i++);
            for (int j = 0; j < is.length; j++) {
                int k = is[j];
                System.out.printf("%d ", k);
            }
            System.out.printf("\n");
        }
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
        this.popSize = populationSize;
    }

    public void setPopulation(List<Individuo> population) {
        this.population = population;
    }

    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    private void print(List<Individuo> population) {
        for (int i = 0; i < population.size(); i++) {
            System.out.println("score: " + population.get(i).getFitness());
        }
    }
}

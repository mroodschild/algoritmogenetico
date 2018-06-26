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

import java.util.List;
import org.gitia.tipo3.crossover.Crossover;
import org.gitia.tipo3.fitness.Fitness;
import org.gitia.tipo3.fitness.FitnessCuadratic;
import org.gitia.tipo3.mutation.Mutation;
import org.gitia.tipo3.population.Individuo;
import org.gitia.tipo3.population.Population;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class AG {

    int epoch;
    int populationSize;
    int dnaSize;
    double elite;
    double mutation;
    double min = -3;
    double max = 3;
    List<Individuo> population;
    Fitness fitness = new FitnessCuadratic();

    public AG() {
        this(50, 20, 10, 0.2, 0.2);
    }

    public AG(int epoch, int populationSize, int dnaSize, double elite, double mutation) {
        this.epoch = epoch;
        this.populationSize = populationSize;
        this.elite = elite;
        this.dnaSize = dnaSize;
        this.mutation = mutation;
    }

    public void run() {
        //initial population
        population = Population.generate(populationSize, dnaSize, min, max);
        //iterations
        fitness.fit(population);
        for (int i = 0; i < epoch; i++) {
            //fit y orden
            //elite
            //selection
            //crossover
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

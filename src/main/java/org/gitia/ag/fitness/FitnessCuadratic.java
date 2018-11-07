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
package org.gitia.ag.fitness;

import java.util.Collections;
import java.util.List;
import org.gitia.ag.population.Individuo;
import org.gitia.ag.population.IndividuoComparator;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class FitnessCuadratic implements Fitness{

    /**
     * agrega el fit y ordena la población
     *
     * @param pop
     */
    @Override
    public void fit(List<Individuo> pop) {
        for (int i = 0; i < pop.size(); i++) {
            Individuo gen = pop.get(i);
            double fit = gen.getDna().elementPower(2).elementSum() * -1;
            gen.setFitness(fit);
        }
        Collections.sort(pop, new IndividuoComparator());
    }

    @Override
    public void printResume(List<Individuo> population, int iteration) {
        double mean;
        double sum = 0;
        double best = population.get(0).getFitness();
        int idxBest = 0;
        for (int i = 0; i < population.size(); i++) {
            Individuo gen = population.get(i);
            sum += gen.getFitness();
            if (best < gen.getFitness()) {
                best = gen.getFitness();
                idxBest = i;
            }
        }
        mean = sum / (double) population.size();
        System.out.println("it:\t" + iteration +"\tBest:\t" + best + "\tmean:\t" + mean + "\tidx best:\t" + idxBest);
    }
}

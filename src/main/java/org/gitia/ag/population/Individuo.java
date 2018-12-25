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
package org.gitia.ag.population;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Individuo {

    SimpleMatrix dna;
    List<Double> fit = new ArrayList<>();
    double fitness;

    public Individuo() {
    }

    public Individuo(SimpleMatrix dna, double fitness) {
        this.dna = dna;
        this.fitness = fitness;
    }

    public Individuo(SimpleMatrix dna, double fitness, List<Double> fit) {
        this.dna = dna;
        this.fitness = fitness;
        this.fit.addAll(fit);
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
        fit.add(fitness);
    }

    /**
     * suma el total de todos los fitness obtenidos y devuelve el promedio
     *
     * @return
     */
    public double getFitnessMean() {
        double total = 0;
        double size = fit.size();
        double promedio = 0;
        for (int i = 0; i < size; i++) {
            total += fit.get(i);
        }
        if (!fit.isEmpty()) {
            promedio = total / size;
        }
        return promedio;
    }

    //    public void addFitness(double fitness){
//        this.fitness += fitness;
//    }
    public SimpleMatrix getDna() {
        return dna;
    }

    /**
     * devuelve el fitness actual
     *
     * @return
     */
    public double getFitness() {
        return fitness;
    }

    public void setDna(SimpleMatrix dna) {
        this.dna = dna;
    }

    public List<Double> getFit() {
        return fit;
    }

    public void setFit(List<Double> fit) {
        this.fit = fit;
    }
}

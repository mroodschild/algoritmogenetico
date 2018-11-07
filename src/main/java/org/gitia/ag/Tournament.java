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

import java.util.List;
import java.util.Random;
import org.gitia.ag.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
class Tournament {

    static Random r = new Random();

    /**
     *
     * @param offspring para saber cuantos cruzamientos se requieren
     * @param population para poder seleccionar la elite en el cruzamiento
     * @return los indices de p1 y p2
     */
    static int[][] getParing(int offspring, List<Individuo> population, int size) {
        int cant = (int) (offspring / 2); // porque salen dos hijos cada dos padres
        int[][] indices = new int[cant][2];
        int p1, p2;
        for (int i = 0; i < cant; i++) {
            p1 = torneo(population, size);
            p2 = torneo(population, size);
            while (p1 == p2) {
                p2 = torneo(population, size);
            }
            indices[i][0] = p1;
            indices[i][1] = p2;
        }
        return indices;
    }

    private static int torneo(List<Individuo> population, int size) {
        int[] candidatos = new int[size];
        int ganador;
        for (int i = 0; i < size; i++) {
            int idx = r.nextInt(population.size());
            candidatos[i] = idx;
        }
        ganador = candidatos[0];
        double score = population.get(ganador).getFitness();
        for (int i = 0; i < candidatos.length; i++) {
            Individuo candidato = population.get(candidatos[i]);
            if (score < candidato.getFitness()) {
                ganador = candidatos[i];
                score = candidato.getFitness();
            }
        }
        return ganador;
    }

}

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
package org.gitia.ag.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.ag.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class MultiNonUniformMutationPercentNeurona {

    static double porcentaje = 0.05;
    static int capaAMutar = 0;
    static boolean mutBias = false;

    static Random r = new Random();

    /**
     *
     * Esta funcion reemplaza en la lista de no cruzados a los idividuos
     * selecionados, por el mutado, ademas los devuelve en un listado
     *
     * @param population_nocruzada // poblacion que no participo en el
     * cruzamiento
     * @param mutacion_size //cantidad de individuos a mutar
     * @param neuronasCapas cantidad de neuronas que tiene determinada capa
     * @param pesosNeuronaCapa cantidad de pesos que tiene una neurona en
     * determinada capa
     * @param porcentajeMutacion porcentaje de mutación
     * @param capaSelMutar capa seleccionada para hacer la mutación
     * @param mutarBias se mutarán los bias? true = si, false = no
     * @return un listado con los idividuos mutados
     */
    public static List<Individuo> mutacion(List<Individuo> population_nocruzada,
            int mutacion_size, int[] neuronasCapas, int[] pesosNeuronaCapa,
            double porcentajeMutacion, int capaSelMutar, boolean mutarBias
    ) {
        porcentaje = porcentajeMutacion;
        capaAMutar = capaSelMutar;
        mutBias = mutarBias;

        //direccion de la mutación
        SimpleMatrix vdir = population_nocruzada.get(0).getDna().copy();
        SimpleMatrix step = vdir.copy();
        //máscara de mutación
        SimpleMatrix vMaskMutation = vdir.copy();

        List<Individuo> aux = new ArrayList<>();
        
        for (int i = 0; i < mutacion_size; i++) {
            int idx = r.nextInt(population_nocruzada.size());
            Individuo ind = population_nocruzada.get(idx);//seleccionamos al azar algún individuo
            //nos movemos izq o derecha?
            setDir(vdir);
            //calculamos el paso
            step(step);
            //preparamos la máscara de mutación con los bias si son solicitados
            selectLayer(vMaskMutation, porcentaje, neuronasCapas, pesosNeuronaCapa, capaAMutar, mutBias);
            //nos movemos
            ind.setDna(sol(ind.getDna(), vdir, step, vMaskMutation));
            aux.add(ind);
            //reemplazamos al individuo
            population_nocruzada.set(idx, ind);
        }
        return aux;
    }

    /**
     *
     * @param vPorcentaje mascara para la mutación de los pesos de la neurona
     * seleccionada
     * @param porcentajeMutacion incida el porcentaje de mutación
     * @param neuronasCapas vector de neuronas de la arquitectura
     * @param pesosNeuronaCapa pesos que tienen las neuronas segun la capa
     * @param layerSeleccionada capa de la que se seleccionará la neurona a
     * mutar
     */
    private static void selectLayer(
            SimpleMatrix vPorcentaje, double porcentajeMutacion,
            int[] neuronasCapas, int[] pesosNeuronaCapa, int layerSeleccionada, boolean mutBias) {

        int cantidadPesosCapasAnteriores = 0;
        int pesosTotales = 0;
        //pesos anteriores a la capa seleccionada
        for (int i = 0; i < layerSeleccionada; i++) {
            cantidadPesosCapasAnteriores += neuronasCapas[i] * pesosNeuronaCapa[i];
        }
        //calculamos el total de pesos antes de iniciar los Bias
        for (int i = 0; i < neuronasCapas.length; i++) {
            pesosTotales += neuronasCapas[i] * pesosNeuronaCapa[i];
        }
        int neuronas = neuronasCapas[layerSeleccionada];// neuronas capa de salida
        int pesos = pesosNeuronaCapa[layerSeleccionada];// Pesos por neurona capa salida
        int neuronaSeleccionada = r.nextInt(neuronas);//seleccionada de la ultima capa
        int first_gen = neuronaSeleccionada * pesos + cantidadPesosCapasAnteriores;//posiciones para la ultima capa
        int last_gen = first_gen + pesos;//ultimo peso
        for (int i = first_gen; i < last_gen; i++) {
            if (r.nextDouble() < porcentajeMutacion) {
                vPorcentaje.set(i, 1);
            }
        }
        //mutamos los bias?
        if (mutBias) {
            int size = vPorcentaje.getNumElements();
            //desde el ultimo peso
            for (int i = pesosTotales; i < size; i++) {
                if (r.nextDouble() < porcentajeMutacion) {
                    vPorcentaje.set(i, 1);
                }
            }
        }
    }

    /**
     * determina el vector de direcciones de cambios
     *
     * @param dir
     */
    static public void setDir(SimpleMatrix dir) {
        for (int i = 0; i < dir.getNumElements(); i++) {
            dir.set(i, Math.pow(-1, Math.round(r.nextDouble())));
        }
    }

    /**
     * El paso es de random * 0.5 + 0.15 , es decir garantizamos un movimiento
     * de 0.15, con valores de 0.15 a 0.65
     *
     * @param step
     */
    static public void step(SimpleMatrix step) {
        for (int i = 0; i < step.getNumElements(); i++) {
            step.set(i, r.nextDouble() * 0.5 + 0.15);
        }
    }

    /**
     *
     * La solución resultante es: a la posición actual se le mueven los pesos,
     * según la máscara de mutación
     *
     * @param ind
     * @param dir
     * @param step
     * @param vMaskMutation
     * @return
     */
    static public SimpleMatrix sol(SimpleMatrix ind, SimpleMatrix dir, SimpleMatrix step, SimpleMatrix vMaskMutation) {
        SimpleMatrix sol = ind.copy();
        sol = sol.plus(dir.elementMult(vMaskMutation).elementMult(step));
        return sol;
    }
}

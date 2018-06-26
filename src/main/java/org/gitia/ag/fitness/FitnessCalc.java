/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gitia.ag.fitness;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class FitnessCalc {

    static double[] solution = new double[64];

    /* Public methods */
    // Set a candidate solution as a double array
    public static void setSolution(double[] newSolution) {
        solution = newSolution;
    }

    // To make it easier we can use this method to set our candidate solution
    // with string of 0s and 1s
    public static void setSolution(String newSolution) {
        solution = new double[newSolution.length()];
        // Loop through each character of our string and save it in our double
        // array
        for (int i = 0; i < newSolution.length(); i++) {
            String character = newSolution.substring(i, i + 1);
            solution[i] = Double.parseDouble(character);
        }
    }

    // Calculate inidividuals fittness by comparing it to our candidate solution
    public static int getFitness(Individual individual) {
        int fitness = 0;
        // Loop through our individuals genes and compare them to our cadidates
        for (int i = 0; i < individual.size() && i < solution.length; i++) {
            fitness += Math.pow(individual.getGene(i) - solution[i], 2);
        }
        return fitness * -1;
    }

    // Get optimum fitness
    public static int getMaxFitness() {
        int maxFitness = solution.length;
        return maxFitness;
    }
}

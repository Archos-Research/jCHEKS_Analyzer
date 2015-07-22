package mainAnalyser;

import Utils.Utils;
import cheksAnalyse.*;
import cheksAnalyse.AbstractCheksAnalyser.AnalyserType;
import com.archosResearch.jCHEKS.chaoticSystem.*;
import com.archosResearch.jCHEKS.chaoticSystem.exception.*;
import com.archosResearch.jCHEKS.concept.exception.ChaoticSystemException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class MainAnalyser {

    private final Saver saver;
    private final int iterations;
    private final int numberOfAgent;
    private ChaoticSystem currentChaoticSystem;
    private ChaoticSystem backupChaoticSystem;
    
    private HashSet<AbstractCheksAnalyser> analysers = new HashSet();
    private HashSet<AnalyserType> types = new HashSet();
    
    public MainAnalyser(int iterations, int numberOfAgent, HashSet<AnalyserType> types) {                
        this.iterations = iterations;
        this.numberOfAgent = numberOfAgent;
        this.types = types;
        
        this.saver = new Saver();
        saver.initDatabase(types);
        
        try {
            currentChaoticSystem = new CryptoChaoticSystem(this.numberOfAgent * Byte.SIZE, "test");

            this.analysers = AbstractCheksAnalyser.createAnalyser(types, currentChaoticSystem);

        } catch (Exception ex) {
            Logger.getLogger(MainAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void analyse() throws Exception {

        for (int i = 0; i < iterations; i++) {
            currentChaoticSystem = new CryptoChaoticSystem(this.numberOfAgent * Byte.SIZE, "test" + i);
            this.analysers = AbstractCheksAnalyser.createAnalyser(types, currentChaoticSystem);
            while(!this.analysers.isEmpty()) {                
                for(Iterator<AbstractCheksAnalyser> iterator = this.analysers.iterator(); iterator.hasNext();) {
                    AbstractCheksAnalyser analyser = iterator.next();
                    if(!analyser.isComplete()) {
                        analyser.analyse(currentChaoticSystem);
                    } else {
                        analyser.saveResult(saver);
                        iterator.remove();
                    }
                }
                
                currentChaoticSystem.evolveSystem();
            }

        }
        //displayStatsOfADistributionTable("variations");
        //displayStatsOfADistributionTable("occurences");
        //displayStatsOfATable("KEY_BITS");
        //displayStatsOfATable("AGENT_LEVELS");
    }
/*
    private void displayStatsOfADistributionTable(String tableName) {
        try {
            
            Distribution[][] systems = saver.getDistributionsOf(tableName, iterations);
            int[] sums[] = new int[systems.length][numberOfAgent];
            for (int i = 0; i < systems.length; i++) {
                sums[i] = Distribution.getSum(systems[i]);
            }
            System.out.println(Arrays.toString(sums));
            
        } catch (SQLException ex) {
            Logger.getLogger(MainAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    private void displayStatsOfATable(String tableName) throws SQLException {
        double[] evolutions = saver.getEvolutionsOf(tableName, iterations);
        System.out.println("|------STATS-OF-" + tableName.toUpperCase() + "----|");
        System.out.println("| Sum: " + Utils.getSumInArray(evolutions));
        System.out.println("| Average: " + Utils.getAverageInArray(evolutions));
        System.out.println("| Minimum: " + Utils.getMinimumInArray(evolutions));
        System.out.println("| Maximum: " + Utils.getMaximumInArray(evolutions));
        System.out.println("| Median: " + Utils.getMedianInArray(evolutions));
        System.out.println("| Standart deviation: " + Utils.getStandartDeviationInArray(evolutions));
        System.out.println("|------------------------------|");
    }

    private void reinitChaoticSystem() {
        try {
            this.currentChaoticSystem = this.backupChaoticSystem.cloneSystem();
        } catch (CloningException ex) {
            Logger.getLogger(MainAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int brent() throws Exception {
        ChaoticSystem turtle = new CryptoChaoticSystem(128, "test");
        ChaoticSystem rabbit = turtle.clone();
        int rabbitState = 0;
        int steps_taken = 0;
        int step_limit = 2;
        while (true) {
            rabbit.evolveSystem();
            rabbitState++;
            steps_taken++;
            if (rabbit.equals(turtle)) {
                System.out.println("CYCLE FOUND AT: " + rabbitState);
                return rabbitState;
            }
            if (steps_taken == step_limit) {
                steps_taken = 0;
                step_limit *= 2;
                turtle = rabbit.clone();
                System.out.println(rabbitState);
            }
        }
    }

    private void analyseAgentLevelOccurenceBetweenSystem(int agentCount) throws ChaoticSystemException, KeyLenghtException, NoSuchAlgorithmException {
        Distribution[] distributions = new Distribution[agentCount];
        for (int j = 0; j < agentCount; j++) {
            distributions[j] = new Distribution();
        }
        for (int i = 0; i < 1000; i++) {
            ChaoticSystem system = new CryptoChaoticSystem(agentCount * Byte.SIZE, "test");
            byte[] key = system.getKey();
            for (int k = 0; k < agentCount; k++) {
                distributions[k].registerValue(key[k]);
            }
            if (i % 10 == 0) {
                System.out.println(Arrays.toString(distributions));
            }
        }
        System.out.println(Arrays.toString(Distribution.getSum(distributions)));
    }
    
    public static void main(String[] args) throws Exception {
        HashSet<AnalyserType> types = new HashSet();
        //types.add(AnalyserType.BOOLEANS);
        //types.add(AnalyserType.BYTESPERBYTES);
        //types.add(AnalyserType.BUTTERFLY);
        //types.add(AnalyserType.OCCURENCE);
        //types.add(AnalyserType.VARIATION);
        types.add(AnalyserType.NIST_1);
        types.add(AnalyserType.NIST_2);
        types.add(AnalyserType.NIST_3);
        
        MainAnalyser analyser = new MainAnalyser(200, 32, types);
        analyser.analyse();
       
    }

}

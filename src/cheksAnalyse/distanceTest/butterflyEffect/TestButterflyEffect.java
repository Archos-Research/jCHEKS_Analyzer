package cheksAnalyse.distanceTest.butterflyEffect;

import Utils.Utils;
import cheksAnalyse.AbstractCheksAnalyser.AnalyserType;
import cheksAnalyse.distanceTest.AbstractDistanceTest;
import com.archosResearch.jCHEKS.chaoticSystem.*;
import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import java.util.*;
import java.util.logging.*;
import mainAnalyser.AbstractSaver;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class TestButterflyEffect extends AbstractDistanceTest {

    private final HashMap<Integer, CryptoChaoticSystem> clones = new HashMap();
    private final int[][] distances;
    
    public static final String TABLE_NAME = "butterfly_effect";
    
    public TestButterflyEffect(boolean enableLog, AbstractChaoticSystem chaoticSystem) throws Exception {
        super(enableLog, chaoticSystem);
        this.iteration = 150;
        this.distances = new int[chaoticSystem.getAgentsCount()][150];               
        this.generateClones(chaoticSystem); 
        this.type = AnalyserType.BUTTERFLY;

    }
    
    private void generateClones(AbstractChaoticSystem system) {
        for(int i = 0; i < this.distances.length; i++) {
            TempChaoticSystem clone = new TempChaoticSystem();
            clone.deserialize(system.serialize());
            HashMap<Integer, Agent> agents = clone.getAgents();
            TempAgent agent = new TempAgent(agents.get(i));            
            agent.setKeyPart(Utils.isPair((int)agent.getKeyPart())?(int)agent.getKeyPart() + 1:(int)agent.getKeyPart() - 1);
            agents.replace(i, agent);
            clone.setAgents(agents);
            
            this.clones.put(i, clone);
        }
    }

    @Override
    protected void scan(AbstractChaoticSystem chaoticSystem) {       
        for(int i = 0; i < this.clones.size(); i++) {
            CryptoChaoticSystem clone = this.clones.get(i);
            try {
                this.distances[i][this.getEvolutionCount()] = getDistance(chaoticSystem.getKey(), clone.getKey());
            } catch (Exception ex) {
                Logger.getLogger(TestButterflyEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                clone.evolveSystem();
            } catch (Exception ex) {
                Logger.getLogger(TestButterflyEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void verify() {        
        if(this.getEvolutionCount() == 150 - 1) {
            this.complete();
            this.clones.clear();
        }
    }

    @Override
    public void saveResult(AbstractSaver saver) {        
        saver.saveButterflyEffect(this.getSystemId(), this.distances);
    }
    
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}

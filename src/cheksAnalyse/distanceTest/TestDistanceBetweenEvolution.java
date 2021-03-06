package cheksAnalyse.distanceTest;

import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import com.archosResearch.jCHEKS.concept.exception.ChaoticSystemException;
import java.util.logging.*;
import mainAnalyser.AbstractSaver;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class TestDistanceBetweenEvolution extends AbstractDistanceTest{

    private final int[] distances;
    private byte[] lastKey;
    public static final String TABLE_NAME = "distance_between_evolution";
    
    public TestDistanceBetweenEvolution(boolean enableLog, AbstractChaoticSystem chaoticSystem) throws Exception {
        super(enableLog, chaoticSystem);
        this.distances = new int[this.iteration];
        this.type = AnalyserType.DISTANCE_EVOLUTION;
    }

    @Override
    public void saveResult(AbstractSaver saver) {
        saver.saveDistance(this.getSystemId(), this.getTableName(), distances);
    }

    @Override
    protected void scan(AbstractChaoticSystem system) {
        if(lastKey != null) {
            try {
                byte[] key = system.getKey();
                this.distances[this.getEvolutionCount()] = getDistance(lastKey, key);
                this.lastKey = key;
            } catch (Exception ex) {
                Logger.getLogger(TestDistanceBetweenEvolution.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                lastKey = system.getKey();
            } catch (ChaoticSystemException ex) {
                Logger.getLogger(TestDistanceBetweenEvolution.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}

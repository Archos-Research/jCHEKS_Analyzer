package cheksAnalyse;

import cheksAnalyse.evolutionTest.TestNbEvolutionsAllAgentLevels;
import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class CheksAnalyserBytesPerBytesTest {
    
    @Test
    public void testCheksAnalyserBytesPerBytes() throws Exception {
        ArrayList<byte[]> keys = new ArrayList();
        byte i = -128;
        boolean completed = false;
        while(!completed) {
            System.out.println(i);
            keys.add(new byte[]{i,i,i,i,i,i,i,i,i,i,i,i,i,i,i,i});
            if(i == 127){
                completed = true;
            }
            i++;
        }
        AbstractChaoticSystem sys = new FakeChaoticSystem(keys, 16);
        TestNbEvolutionsAllAgentLevels analyser = new TestNbEvolutionsAllAgentLevels(true, sys);
        while(!analyser.isComplete()){
            analyser.analyse(sys);
            sys.evolveSystem();
        }
        
        assertEquals(255, analyser.getEvolutionCount());
    }

    @Test
    public void testCheksAnalyserBytesPerBytes2() throws Exception {
        ArrayList<byte[]> keys = new ArrayList();
        byte i = -128;
        boolean completed = false;
        while(!completed) {
            System.out.println(i);
            keys.add(new byte[]{i,i,i,i,i,i,i,i,i,i,i,i,i,i,i,i});
            keys.add(new byte[]{i,i,i,i,i,i,i,i,i,i,i,i,i,i,i,i});
            if(i == 127){
                completed = true;
            }
            i++;
        }
        AbstractChaoticSystem sys = new FakeChaoticSystem(keys, 16);
        TestNbEvolutionsAllAgentLevels analyser = new TestNbEvolutionsAllAgentLevels(true, sys);
        while(!analyser.isComplete()){
            analyser.analyse(sys);
            sys.evolveSystem();
        }
        
        assertEquals(510, analyser.getEvolutionCount());
    }
}

package cheksAnalyse.evolutionTest;

import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import com.archosResearch.jCHEKS.concept.exception.ChaoticSystemException;
import java.util.*;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class TestNbEvolutionsAllAgentLevels extends AbstractEvolutionTest{
    
    private HashSet<Byte>[] bytesSaw;
    private int ammountOfByte;
    public static final String TABLE_NAME = "nbEvolutions_allAgentLevels";

    public TestNbEvolutionsAllAgentLevels(AbstractChaoticSystem chaoticSystem) throws Exception{
        super(true, chaoticSystem);
        this.initAnalyser(chaoticSystem);       
    }
    
    public TestNbEvolutionsAllAgentLevels(boolean enableLog, AbstractChaoticSystem chaoticSystem) throws Exception{
        super(enableLog, chaoticSystem);
        this.initAnalyser(chaoticSystem);
    }
    
    private void initAnalyser(AbstractChaoticSystem chaoticSystem) {
        this.type = AnalyserType.BYTESPERBYTES;
        this.ammountOfByte = chaoticSystem.getAgentsCount();
        this.bytesSaw = new HashSet[this.ammountOfByte];
        for (int i = 0; i < this.bytesSaw.length; i++) {
            this.bytesSaw[i] = new HashSet();
        } 
    }
    
    @Override
    protected void scan(AbstractChaoticSystem chaoticSystem) {
        try {
            byte[] array = chaoticSystem.getKey();
            for (int i = 0; i < ammountOfByte; i++) {
                this.bytesSaw[i].add(array[i]);
            }
        } catch (ChaoticSystemException ex) {
            complete();
        }
    }
    
    @Override
    protected void verify(){
        if(everyBytesSaw()){
            complete();
        }
    }
    
    private boolean everyBytesSaw(){
        for (HashSet<Byte> element : this.bytesSaw) {
            if (element.size() != 256) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void log(){
        super.log();
        if(this.getEvolutionCount() % 1000 == 0) {
            if(logEnabled){
                for(int i = 0; i < this.bytesSaw.length; i++) {
                    System.out.print(this.bytesSaw[i].size() + ",");
                }
                System.out.print("\n");
                System.out.println("Bytes saw: " + Arrays.toString(this.bytesSaw));
            }
        }
    }
    
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}

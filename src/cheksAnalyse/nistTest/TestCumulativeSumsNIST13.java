package cheksAnalyse.nistTest;

import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;

/**
 *
 * @author Michael Roussel rousselm4@gmail.com
 */
public class TestCumulativeSumsNIST13 extends AbstractNistTest{

    public static final int BITS_NEEDED = 0; //TODO Set this value
    public static final String TABLE_NAME = "cumulative_sums_NIST_13";
    
    public TestCumulativeSumsNIST13(AbstractChaoticSystem chaoticSystem) throws Exception {
        super(chaoticSystem, BITS_NEEDED);
        this.type = AnalyserType.NIST_13;
    }
    
    @Override
    public void executeTest(boolean[] bits) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /* 
         * The variable bits is the input bits of the lenght of BITS_NEEDED generated by the chaotic system.
         * This method should change pValue attribute of the result.
         * this.pValue = result;
         */
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
    
}
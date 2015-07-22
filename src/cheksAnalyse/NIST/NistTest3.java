package cheksAnalyse.NIST;

import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import mainAnalyser.Saver;
import static org.apache.commons.math3.special.Erf.erfc;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 * 
 * NIST Test 2.3: Runs test
 */
public class NistTest3 extends AbstractNistTest{

    public NistTest3(AbstractChaoticSystem chaoticSystem) throws Exception {
        super(chaoticSystem);
        this.bitsNeeded = 100000;
        NistTest3.TABLE_NAME = "Runs_NIST-3";
    }
    
    public NistTest3(AbstractChaoticSystem chaoticSystem, int bitsNeeded) throws Exception {
        super(chaoticSystem);
        this.bitsNeeded = bitsNeeded;
    }
    
    @Override
    public void executeTest(boolean[] bits) {
        double p = this.calculateP(bits);
        if(this.shouldContinue(p)) {
            int[] Si = this.calculateSi(bits);
            double vObs = this.calculateVobs(Si);
            double pValue = this.calculatePValue(vObs, p);
            
            this.passed = pValue > 0.01;
        }
    }
    
    public double calculateP(boolean[] bits) {
        double ones = 0;
                
        for(int i = 0; i < bits.length; i++) {
            if(bits[i] == true) {
                ones++;
            }
        }
        
        return ones / (double) this.bitsNeeded;
        
    }
    
    public double calculateT() {
        return 2 / Math.sqrt(this.bitsNeeded);
    }
    
    public boolean shouldContinue(double p) {        
        return Math.abs(p - 0.5) < this.calculateT();
    }
    
    public int[] calculateSi(boolean[] bits) {
        int[] Si = new int[bits.length - 1];
        
        for(int i = 0; i < bits.length - 1; i++) {
            if(bits[i] != bits[i + 1]) {
                Si[i] = 1;
            } else {
                Si[i] = 0;
            }
        }
        
        return Si;
    }
    
    public int calculateVobs(int[] Si) {
        int vObs = 0;
        
        for(int i = 0; i < Si.length; i++) {
            vObs += Si[i];
        }
        
        return vObs + 1;
    }
    
    public double calculatePValue(double vObs, double p) {
        double abs = Math.abs(vObs - 2 * this.bitsNeeded * p * (1 - p));
        double div = 2 * Math.sqrt(2 * this.bitsNeeded) * p * (1 - p);
        
        return erfc(abs / div);
    }

    @Override
    protected void scan(AbstractChaoticSystem system) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void verify() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveResult(Saver saver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

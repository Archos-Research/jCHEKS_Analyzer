package mainAnalyser;

import cheksAnalyse.AbstractCheksAnalyser;
import cheksAnalyse.AbstractCheksAnalyser.AnalyserType;
import com.archosResearch.jCHEKS.concept.chaoticSystem.AbstractChaoticSystem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Thomas Lepage thomas.lepage@hotmail.ca
 */
public class PRNGAnalyser extends AbstractMainAnalyser{

    public PRNGAnalyser(HashSet<AnalyserType> types) {
        super(types);
    }

    @Override
    protected void analyse() throws Exception {      
        List<String> systemsName = this.getSystemsFileName("PRNGSystem");
        Collections.sort(systemsName);
        for(Iterator<String> system = systemsName.iterator(); system.hasNext();) {
            if(shouldContinueAnalyse()) {
                String fileName = system.next();
                Date startTime = new Date();
                
                this.analyseSystem(fileName);                
                
                DateFormat dateFormat = new SimpleDateFormat("mm:ss");
                Date date = new Date((new Date()).getTime() - startTime.getTime());
                System.out.println("Analyse of " + fileName + " is done. It took: " + dateFormat.format(date));
                system.remove();                
            } else {
                return;
            }
        }
    }
    
    @Override
    protected void analyseSystem(String systemName) throws Exception {
        AbstractChaoticSystem currentChaoticSystem = new PRNGChaoticSystem("PRNGSystem/" + systemName);
        this.analysers = AbstractCheksAnalyser.createAnalyser(types, currentChaoticSystem);
        
        while(!this.analysers.isEmpty()) {                
            for(Iterator<AbstractCheksAnalyser> iterator = this.analysers.iterator(); iterator.hasNext();) {
                AbstractCheksAnalyser analyser = iterator.next();
                if(this.analyseWithSystem(analyser, currentChaoticSystem)) {
                    iterator.remove();
                }
            }                
            currentChaoticSystem.evolveSystem();
        }
        this.analysers.clear();
    }    
}

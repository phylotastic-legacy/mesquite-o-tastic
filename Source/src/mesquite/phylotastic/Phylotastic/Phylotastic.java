/*
 * 
 * Created on Jun 6, 2012
 * Last updated on Jun 6, 2012
 * 
 */
package mesquite.phylotastic.Phylotastic;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mesquite.Mesquite;
import mesquite.lib.Debugg;
import mesquite.lib.EmployeeNeed;
import mesquite.lib.MesquiteModule;
import mesquite.lib.Taxa;
import mesquite.lib.Tree;
import mesquite.lib.Listable;
import mesquite.lib.ListableVector;
import mesquite.lib.MesquiteTree;
import mesquite.lib.TreeVector;
import mesquite.lib.duties.ExternalTreeSearcher;

public class Phylotastic extends ExternalTreeSearcher {
    
    Taxa requestedTaxa;
    String requestStr;
    
    final static String prefix = "http://phylotastic-wg.nescent.org/script/phylotastic.cgi?species=";
    final static String suffix = "&tree=mammals&format=newick";
    final static String testString = 
        "http://phylotastic-wg.nescent.org/script/phylotastic.cgi?species=Erinaceus_europaeus,Nandinia_binotata,Manis_crassicaudata,Manis_javanica,Manis_pentadactyla&tree=mammals&format=newick";
    
//    final static String testTreeReturn =
//        "(((Nandinia_binotata,((Manis_pentadactyla,Manis_crassicaudata),Manis_javanica))),Erinaceus_europaeus);";
    
    public void getEmployeeNeeds(){  //This gets called on startup to harvest information; override this and inside, call registerEmployeeNeed
    }


    public boolean startJob(String arguments, Object condition, boolean hiredByName) {
        loadPreferences();
        ListableVector foo = this.getProject().getTaxas();
        Listable[] contents = foo.getElementArray();
        if (contents[0] instanceof Taxa){
            requestedTaxa = (Taxa)contents[0];
        }
        else {
            Debugg.println("Element of Taxas not a taxa block");
        }
        requestStr = buildRequest(requestedTaxa);
        Debugg.println("requestStr is " + requestStr);
        return true;
    }

    
    private String buildRequest(Taxa taxa){
        StringBuilder taxaString = new StringBuilder(200);
        for (int i = 0; i<taxa.getNumTaxa();i++){
            String rawName = taxa.getTaxonName(i);
            String newName = rawName.replace(' ','_');
            taxaString.append(newName);
            if (i<taxa.getNumTaxa()-1)
                taxaString.append(',');
        }
        return prefix+taxaString.toString()+suffix;
    }

    public Class getDutyClass() {
        return Phylotastic.class;
    }
    
    public boolean isSubstantive(){
        return true;
    }
    
    public boolean showCitation(){
        return true;
    }

    public String getName() {
        return "Phylotastic";
    }

    public String getExplanation(){
        return "Get tree using Phylotastic web services";
    }


    public void initialize(Taxa taxa) {
        Debugg.println("Entering initialize: " + taxa);
    }


    public void fillTreeBlock(TreeVector treeList) {
        Debugg.println("Entering fillTreeBlock");
        String requestReturn;
        try {
            URL u = new URL(requestStr);
            InputStream us = u.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(us));
            StringBuilder treeStrBuilder = new StringBuilder(200);
            String raw = br.readLine();
            while(raw != null){
                treeStrBuilder.append(raw);
                raw = br.readLine();
            }
            us.close();
            requestReturn = treeStrBuilder.toString();
            
        } catch (Exception e) {
            Debugg.println("URL problem: " + requestStr);
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        MesquiteTree t = new MesquiteTree(requestedTaxa);
        t.setPermitTaxaBlockEnlargement(false);  //false for now - phylotastic should prune everything not requested
        t.setTreeVector(treeList);
        t.readTree(requestReturn);
        t.setName("Phylotastic tree");
        treeList.addElement(t, false);
    }
    
}

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
import mesquite.lib.MesquiteMessage;
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
    final static String FORMATSPECIFIER = "&format=";
    final static String TREESPECIFIER = "&tree=";
    final static String suffix = "&tree=mammals&format=newick";
    final static String testString = 
        "http://phylotastic-wg.nescent.org/script/phylotastic.cgi?species=Erinaceus_europaeus,Nandinia_binotata,Manis_crassicaudata,Manis_javanica,Manis_pentadactyla&tree=mammals&format=newick";
    final static String testFormat = "newick";
    final static String testTree = "mammals";
    
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
        String requestedFormat = testFormat;
        String requestedTree = testTree;
        requestStr = buildRequest(requestedTaxa,requestedFormat,requestedTree);
        Debugg.println("requestStr is " + requestStr);
        return true;
    }

    
    private String buildRequest(Taxa taxa, String formatId, String treeId){
        StringBuilder requestString = new StringBuilder(2000);
        requestString.append(prefix);
        for (int i = 0; i<taxa.getNumTaxa();i++){
            String rawName = taxa.getTaxonName(i);
            String newName = rawName.replace(' ','_');
            requestString.append(newName);
            if (i<taxa.getNumTaxa()-1)
                requestString.append(',');
        }
        requestString.append(TREESPECIFIER);
        requestString.append(treeId);
        requestString.append(FORMATSPECIFIER);
        requestString.append(formatId);
        return requestString.toString();
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

    //This method should be used to set the taxa block to build the query, but it is never called
    public void initialize(Taxa taxa) {
        Debugg.println("Entering initialize: " + taxa);
    }


    public void fillTreeBlock(TreeVector treeList) {
        String requestReturn;
        try {
            URL u = new URL(requestStr);
            final InputStream us = u.openStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(us));
            final StringBuilder treeStrBuilder = new StringBuilder(200);
            String raw = br.readLine();
            while(raw != null){
                treeStrBuilder.append(raw);
                raw = br.readLine();
            }
            us.close();
            requestReturn = treeStrBuilder.toString();
            
        } catch (Exception e) {
            MesquiteMessage.warnProgrammer("URL for Phylotastic has a syntax problem; String is : " + requestStr);
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

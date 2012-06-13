/*
 * 
 * Created on Jun 6, 2012
 * Last updated on Jun 6, 2012
 * 
 */
package mesquite.phylotastic.aPhylotasticIntro;


import mesquite.lib.duties.*;


/* ======================================================================== */
/* named with an "a" so it loads first among the PDAP modules*/
public class aPhylotasticIntro extends PackageIntro {
    /*.................................................................................................................*/
    public boolean startJob(String arguments, Object condition, boolean hiredByName) {
        return true;
     }

    
    /*.................................................................................................................*/
    public String getName() {
        return "Phylotastic Package Introduction";
    }

    public Class getDutyClass(){
        return aPhylotasticIntro.class;
     }

    /*.................................................................................................................*/
    public String getExplanation() {
        return "Serves as an introduction to the Phylotastic Bridge";
     }
   
    /*.................................................................................................................*/
    /** Returns the name of the package of modules (e.g., "PDAP Package")*/
    public String getPackageName(){
        return "Phylotastic Package";
    }
    
    /*.................................................................................................................*/
    /** Returns a citation for the package of modules */
    public String getPackageCitation(){
        return "Phylotastic Hackathon 2012. Phylotastic Package.";
    }
    
    /** Returns full(er) names of the authors of the package */
    public String getPackageAuthors() {
        return "Phylotastic Hackathon 2012" ;
    }
    
    /** Returns a version string for the package */
    public String getPackageVersion() {
        return "0.1";
    }
    
     /*.................................................................................................................*/
    /** Returns version for a package of modules as an integer*/
    public int getPackageVersionInt(){
        return 10;
    }

    /*.................................................................................................................*/
    /** returns the URL of the notices file for this module so that it can phone home and check for messages */
//    public String  getHomePhoneNumber(){ 
//        return "http://mesquiteproject.org/pdap_mesquite/notices.xml";
//        //return "file:///Users/peter/Projects/workspace/PDAP/Resources/notices/notices.xml";  // Testing from source
//    }
    /*.................................................................................................................*/
    /** Returns whether there is a splash banner*/
    public boolean hasSplash(){
        return true; 
    }

}

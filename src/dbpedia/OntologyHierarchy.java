package dbpedia;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class OntologyHierarchy implements Serializable{
	
	private static final long serialVersionUID = 5224909995589382023L;
	public ClassNode root;
	public LinkedHashMap<String, ClassNode> nameNodeLinkedMap; //a list mapping each className to its node in the hierarchy
	
	public OntologyHierarchy(){
		nameNodeLinkedMap = new LinkedHashMap<String, ClassNode>();
	}
}

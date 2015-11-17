package dbpedia;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassNode implements Serializable {

	private static final long serialVersionUID = -8525988239294931127L;
	public String classID;
	public String className;
	public ClassNode parent;
	public LinkedHashMap<String, ClassNode> children;// className->node

	public HashMap<String, Integer> propertyAllHistogram;// for model training,
															// property
															// histogram of
															// entities mapped
															// to this node and
															// its descendants
	public int entityAllCount;// for model training, store # of training
								// entities mapped to this class and its
								// descendants (cannot do prediction from top to
								// bottom in the hierarchy if only storing
								// entityAllCount in the lowest level class)

	public HashMap<String, Integer> mappedEntityHistogram;// for new tweets,
															// store (entity,
															// cnt) pairs for
															// the lowest level
															// class node
	public int mappedEntityAllCount;// for new tweets, store # of testing
									// entities mapped to the class or its
									// descendants

	public List<Map.Entry<String, Double>> topkEntities;// selected top-k
														// entities for each
														// node

	public ClassNode() {
		classID = new String("");
		className = null;
		parent = null;
		children = new LinkedHashMap<String, ClassNode>();
		propertyAllHistogram = new HashMap<String, Integer>();
		entityAllCount = 0;
		mappedEntityHistogram = new HashMap<String, Integer>();
		mappedEntityAllCount = 0;
		topkEntities = null;
	}

	/**
	 * compute comm & dist between two class nodes
	 * 
	 * @param cc
	 * @return comm & dist
	 */
	public int[] getClassRelation(ClassNode cc) {
		int[] relation = { 0, 0 }; // comm & dist
		int i = 0;
		int minlen = this.classID.length();
		if (minlen > cc.classID.length())
			minlen = cc.classID.length();
		for (i = 0; i < minlen; i++) {
			if (this.classID.charAt(i) != cc.classID.charAt(i))
				break;
		}
		relation[0] = i;
		relation[1] = (this.classID.length() - i) + (cc.classID.length() - i);

		return relation;
	}
}

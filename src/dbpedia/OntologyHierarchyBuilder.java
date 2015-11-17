package dbpedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class OntologyHierarchyBuilder {

	static OntologyHierarchy hierarchy = null;

	public static void main(String[] args) {
		// build ontology hierarchy, then serialize to disk
		System.out.println("building ontology hierarchy");
		hierarchy = buildOntologyHierarchy();
		System.out.println("hierarchy.nameNodeLinkedMap.size="
				+ hierarchy.nameNodeLinkedMap.size());
		System.out.println("done!");
	}

	public static OntologyHierarchy buildOntologyHierarchy() {
		OntologyHierarchy hierarchy = new OntologyHierarchy();

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"ontology.txt"));
			String line;
			HashMap<String, HashSet<String>> parentChildrenMap = new HashMap<String, HashSet<String>>();// parent->children
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty())
					continue;
				String[] classes = line.split("\\+");
				String par = classes[1];// parent class
				String chi = classes[0];// child class
				HashSet<String> set;
				if ((set = parentChildrenMap.get(par)) == null) {
					set = new HashSet<String>();
					set.add(chi);
					parentChildrenMap.put(par, set);
				} else {
					set.add(chi);
				}
			}// while
			br.close();

			// print parentChildrenMap
			// int maxChildrenNo = 0;
			// System.out.println(parentChildrenMap.size());//111
			// for(Entry<String, HashSet<String>> en :
			// parentChildrenMap.entrySet()){
			// System.out.println(en.getKey()+" "+en.getValue());
			// if(maxChildrenNo<en.getValue().size())
			// maxChildrenNo = en.getValue().size();
			// }
			// System.out.println(maxChildrenNo);//45

			// build ontology hierarchy
			// 1.build root node first
			ClassNode root = new ClassNode();
			root.classID = "";
			root.className = "owl#Thing";
			root.parent = null;
			hierarchy.root = root;
			hierarchy.nameNodeLinkedMap.put("owl#Thing", root);
			// 2.build the rest nodes
			PrintWriter pw=new PrintWriter("classid.txt");
			buildChildrenNode(hierarchy, root, parentChildrenMap,pw);
			// System.out.println();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hierarchy;
	}

	public static void buildChildrenNode(OntologyHierarchy hierarchy,
			ClassNode parentNode,
			HashMap<String, HashSet<String>> parentChildrenMap, PrintWriter pw) {
		HashSet<String> childSet = parentChildrenMap.get(parentNode.className);
		parentChildrenMap.remove(parentNode.className);// no need to remove

		// if no children
		if (childSet == null || childSet.isEmpty()) {
			parentNode.children = null;
			return;
		}

		// if has children
		Iterator<String> iter = childSet.iterator();
		while (iter.hasNext()) {
			// 1.build child node first
			String childName = iter.next();
			ClassNode childNode = new ClassNode();
			childNode.parent = parentNode;
			childNode.classID = parentNode.classID.toString();
			childNode.classID = childNode.classID + " "
					+ Integer.toString(parentNode.children.size() + 1);
			childNode.className = childName;
			System.out.println(childNode.className + " "
					+ childNode.classID.toString());
			pw.println(childNode.className
					+ childNode.classID.toString());
			hierarchy.nameNodeLinkedMap.put(childName, childNode);
			// 1.5.connect child to parent
			parentNode.children.put(childName, childNode);
			// 2.build the children of current child node
			buildChildrenNode(hierarchy, childNode, parentChildrenMap,pw );

		}
	}
}

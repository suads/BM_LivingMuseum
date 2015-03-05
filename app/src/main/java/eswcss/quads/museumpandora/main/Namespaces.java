package eswcss.quads.museumpandora.main;

import java.util.HashMap;

public class Namespaces {

	public static HashMap<String,String> prefixValues = new HashMap<String,String>();
	
	static {
		prefixValues.put("ecrm", "http://erlangen-crm.org/current/");
		prefixValues.put("thes", "http://collection.britishmuseum.org/id/thesauri/");
		prefixValues.put("object", "http://collection.britishmuseum.org/id/object/");
		prefixValues.put("bmo", "http://collection.britishmuseum.org/id/ontology/");
		prefixValues.put("bmid", "http://collection.britishmuseum.org/id/");
		prefixValues.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		prefixValues.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		prefixValues.put("owl", "http://www.w3.org/2002/07/owl#");
		prefixValues.put("skos", "http://www.w3.org/2004/02/skos/core#");
		prefixValues.put("foaf", "http://xmlns.com/foaf/0.1/");
	}
	
	public static String getNamespaceValue(String prefix){
		return prefixValues.get(prefix);
	}
	
	public static String getNamespace(String prefix){
		return "<"+getNamespaceValue(prefix)+">";
	}

}

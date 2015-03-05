package eswcss.quads.museumpandora.main;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class DAP  {

	private static String SPARQL_ENDPOINT = "http://collection.britishmuseum.org/sparql";//"http://sparql.researchspace.org";
	
	public static Model model = ModelFactory.createDefaultModel();
	
//	static {
//		RDFDataMgr.read(model,RDFDataMgr.open("locations.owl"),Lang.RDFXML);
//	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	public static ResultSet submit(String query){
		ResultSet results = null;
		QueryExecution exec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, query);
		results = exec.execSelect();
		return results;
	}

//	public static ResultSet submit_local(String query){
//		ResultSet results = null;
//		QueryExecution exec = QueryExecutionFactory.create(query,model);
//		results = exec.execSelect();
//		return results;
//	}



}

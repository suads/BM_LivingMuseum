package eswcss.quads.museumpandora.main;

public class QueryBank {
	
	private static int limit = 10;

	public static String getObjectByBBCSeries = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
			"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
			"PREFIX bmo: <http://collection.britishmuseum.org/id/ontology/>" +
			"PREFIX crm: <http://erlangen-crm.org/current/>" +
			"PREFIX bbcpont: <http://purl.org/ontology/po/>" +
			"PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
			"PREFIX dct: <http://purl.org/dc/terms/>" +
			"PREFIX purl: <http://purl.org/ontology/po/>" +
			"SELECT DISTINCT ?obj ?ep ?title ?image ?desc ?shortSynopsis ?mediumSynopsis ?longSynopsis ?location ?collection ?culture ?foundat ?collectionId {" +
			"?ep crm:P70_documents ?obj ." +
			"?ep bbcpont:microsite ?series ." +
			"?obj bmo:PX_has_main_representation ?image ." +
			"?ep dc:title ?title ." +
			"?obj bmo:PX_physical_description ?desc ." +
			"OPTIONAL { ?ep purl:short_synopsis ?shortSynopsis . }" +
			"OPTIONAL { ?ep purl:medium_synopsis ?mediumSynopsis . }" +
			"OPTIONAL { ?ep purl:long_synopsis ?longSynopsis . }" +
			"?page foaf:primaryTopic ?ep ." +
			"?page dct:created ?date ." +
//			"OPTIONAL {?obj crm:P45_consists_of ?mat ." +
//			"?mat skos:prefLabel ?material .}" +
			"OPTIONAL{?obj crm:P46i_forms_part_of ?collec ." +
			"?collec rdfs:label ?collection .}" +
			"OPTIONAL{?obj crm:P108i_was_produced_by ?cu ." +
			"?cu crm:P9_consists_of ?cult ." +
			"?cult crm:P10_falls_within ?cultur ." +
			"?cultur skos:prefLabel ?culture .}" +
			"OPTIONAL{?obj crm:P12i_was_present_at ?fou ." +
			"?fou crm:P7_took_place_at ?found ." +
			"?found skos:prefLabel ?foundat .}" +
			"OPTIONAL{?obj crm:P55_has_current_location ?loc ." +
			"?loc rdfs:label ?location .}" +
			"FILTER(STR(?series)=\"http://www.bbc.co.uk/ahistoryoftheworld\") ." +
			"OPTIONAL{?obj crm:P1_is_identified_by ?id. " +
			"?id rdfs:label ?collectionId." +
			"?id crm:P2_has_type ?idType." +
			"FILTER(STR(?idType) = 'http://collection.britishmuseum.org/id/thesauri/identifier/codexid')}" +
			"}" +
			"ORDER BY ?date LIMIT 10" ;

	public static String getObjectByLocation = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
			"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
			"PREFIX bmo: <http://collection.britishmuseum.org/id/ontology/>" +
			"PREFIX crm: <http://erlangen-crm.org/current/>" +
			"PREFIX bbcpont: <http://purl.org/ontology/po/>" +
			"PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
			"PREFIX dct: <http://purl.org/dc/terms/>" +
			"PREFIX purl: <http://purl.org/ontology/po/>" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +			
			"SELECT DISTINCT ?obj ?ep ?title ?image ?desc ?shortSynopsis ?mediumSynopsis ?longSynopsis ?location ?collection ?culture ?foundat ?objectType { " +
			//" BIND(<START_URI> as ?obj) ." +
			"?obj crm:P55_has_current_location ?loc ." +
			"?loc rdfs:label ?location ." +
			"FILTER ( regex(?location, \"^ROOM_ID.*\", \"i\") ) " +

			" OPTIONAL { ?obj crm:P102_has_title ?titl . ?titl rdfs:label ?title . FILTER(lang(?title)='en') } " +

			" OPTIONAL { ?obj bmo:PX_has_main_representation ?image . }" +
			" OPTIONAL { ?obj bmo:PX_physical_description ?desc . } " +
			" OPTIONAL { ?ep purl:short_synopsis ?shortSynopsis . }" +
			" OPTIONAL { ?ep purl:medium_synopsis ?mediumSynopsis . }" +
			" OPTIONAL { ?ep purl:long_synopsis ?longSynopsis . }" +

//			"OPTIONAL {?obj crm:P45_consists_of ?mat ." +
//			"?mat skos:prefLabel ?material .}" +
			"OPTIONAL{?obj crm:P46i_forms_part_of ?collec ." +
			"?collec rdfs:label ?collection .}" +
			"OPTIONAL{?obj crm:P108i_was_produced_by ?cu ." +
			"?cu crm:P9_consists_of ?cult ." +
			"?cult crm:P10_falls_within ?cultur ." +
			"?cultur skos:prefLabel ?culture .}" +
			"OPTIONAL{?obj crm:P12i_was_present_at ?fou ." +
			"?fou crm:P7_took_place_at ?found ." +
			"?found skos:prefLabel ?foundat .}" +
			"OPTIONAL { ?obj bmo:PX_object_type ?type . " +
			" ?type skos:prefLabel ?objectType . }" +
			"} "
			+ " LIMIT " + limit ;


	//"FILTER ( regex(?location, \".*ROOM_ID.*\", \"i\") ) " +
	public static String getStartObject = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
			"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
			"PREFIX bmo: <http://collection.britishmuseum.org/id/ontology/>" +
			"PREFIX crm: <http://erlangen-crm.org/current/>" +
			"PREFIX bbcpont: <http://purl.org/ontology/po/>" +
			"PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
			"PREFIX dct: <http://purl.org/dc/terms/>" +
			"PREFIX purl: <http://purl.org/ontology/po/>" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			"SELECT DISTINCT ?obj ?ep ?title ?image ?desc ?shortSynopsis ?mediumSynopsis ?longSynopsis ?location ?collection ?culture ?foundat ?objectType { " +
			
			" OPTIONAL { ?obj crm:P102_has_title ?titl . ?titl rdfs:label ?title . FILTER(lang(?title)='en') } " +

			" FILTER( ?obj = <START_URI> ) . " +
			" OPTIONAL { ?obj bmo:PX_has_main_representation ?image . } " +
			
			"OPTIONAL { ?obj bmo:PX_physical_description ?desc . }" +
			"OPTIONAL { ?ep purl:short_synopsis ?shortSynopsis . }" +
			"OPTIONAL { ?ep purl:medium_synopsis ?mediumSynopsis . }" +
			"OPTIONAL { ?ep purl:long_synopsis ?longSynopsis . }" +
//			"OPTIONAL {?obj crm:P45_consists_of ?mat ." +
//			"?mat skos:prefLabel ?material .}" +
			"OPTIONAL{?obj crm:P46i_forms_part_of ?collec ." +
			"?collec rdfs:label ?collection .}" +
			"OPTIONAL{?obj crm:P108i_was_produced_by ?cu ." +
			"?cu crm:P9_consists_of ?cult ." +
			"?cult crm:P10_falls_within ?cultur ." +
			"?cultur skos:prefLabel ?culture .}" +
			"OPTIONAL{?obj crm:P12i_was_present_at ?fou ." +
			"?fou crm:P7_took_place_at ?found ." +
			"?found skos:prefLabel ?foundat .}" +
			"?obj crm:P55_has_current_location ?loc ." +
			"?loc rdfs:label ?location . " +
			"OPTIONAL { ?obj bmo:PX_object_type ?type . " +
			" ?type skos:prefLabel ?objectType . }" +
			"} " +
			" LIMIT 1";// + limit ;

	public static String getNeighboringRooms = "PREFIX quads: <http://www.owl-ontologies.com/Ontology1409856138.owl> "
			+ "SELECT DISTINCT ?neighbour_label " +
"{ " +
"   ?connection quads:connects ?curr_room . " + 
"   ?curr_room quads:hasLabel ?room_label .  " +
"   FILTER (?room_label = \"ROOM_ID\") " +
"   ?connection quads:connects ?neighbour_room . " +
"   FILTER(?neighbour_room != ?curr_room) . " +
"   ?neighbour_room quads:hasLabel ?neighbour_label . " + 
"}";
	
}

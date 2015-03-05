package eswcss.quads.museumpandora.main;

import android.util.Log;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

//import com.hp.hpl.jena.query.ResultSetFormatter;

public class Engine {

	public static String startObjectURI = "http://collection.britishmuseum.org/id/object/EOC3130";//"http://collection.britishmuseum.org/id/object/YCA2067";

	private static Engine instance = new Engine();
	private Engine(){ artefacts = new LinkedList<Artefact>(); }
	public static Engine getInstance(){ return instance;}

	private LinkedList<Artefact> artefacts;

	public static void main(String[] args) {
		//ResultSetFormatter.out(System.out, results);
	
		//execute following code on room change
		User.getInstance().addToHistory(User.getInstance().getCurrentArtefact());
		System.out.println( User.getInstance().getCurrentArtefact());
		
		instance.setArtefacts(new LinkedList<Artefact>());
		String user_location = User.getInstance().getCurrentArtefact().getLocation();
        Log.i("BM.E.main","User Location: "+user_location);
		instance.generateArtefacts("The British Museum: Gallery G"+user_location);
        Log.i("BM.E.main", "Number of Artefacts: " +instance.getArtefacts().size());

        Log.i("BM.E.main", "Recommendations: ");
		for(String room_nb: instance.getNeighboringRooms(user_location)){
			Log.i("BM.E.main", "Room Nb: "+room_nb);
			instance.generateArtefacts("The British Museum: Gallery G"+room_nb);
		}
		
		Collections.sort(instance.artefacts);
		
		//System.err.println("\nRecommendations...\n\n");
        Log.i("BM.E.main","Recommendations...");
		int i=0;
		for(Artefact artefact: instance.artefacts){

			System.out.println(artefact);
			i++;
//			if(i%3==0)
//				break;
		}
	}

	public HashSet<String> getNeighboringRooms(String room_nb){
		HashSet<String> neighbors = Neighborings.relations.get(room_nb);
		HashSet<String> neighbors2 = new HashSet<String>();
		
		for(String n1: neighbors){
			for(String n2: Neighborings.relations.get(n1))
				neighbors2.add(n2);
		}
		neighbors.addAll(neighbors2);
		return neighbors;
//		LinkedList<String> locations = new LinkedList<String>();
//		
//		ResultSet results = DAP.submit_local(QueryBank.getNeighboringRooms.replace("ROOM_ID", room_label));
//		while(results.hasNext()){
//			QuerySolution solution = results.next();
//			String location = notNull(solution.get("?neighbour_label")) ? solution.get("?neighbour_label").toString().trim() : "";
//			if(location.length()!=0)
//				locations.addLast(location);
//		}
//		return locations;
	}

    public void generateArtefacts(String loc){
        String query = QueryBank2.getObjectByLocation.replace("ROOM_ID", loc);
        Log.i("BM.E.generateArtefacts",query);
        ResultSet results = DAP.submit(query);

        HashSet<String> memory = new HashSet<String>();
        HashSet<String> memory2 = new HashSet<String>();
        int resultsNumber = 0;

        while(results.hasNext()){
            QuerySolution solution = results.next();
            String description = notNull(solution.get("?desc")) ? solution.get("?desc").toString().trim() : "";
            String objectURI = notNull(solution.get("?obj")) ? solution.get("?obj").toString().trim() : "";
            Log.e("URI", objectURI);
            String documentURI = notNull(solution.get("?ep")) ? solution.get("?ep").toString().trim() : "";
            String title = notNull(solution.get("?title")) ? solution.get("?title").toString().trim() : "";
            Log.e("Title", title);
            String imageURL = notNull(solution.get("?image")) ? solution.get("?image").toString().trim() : "";
            String shortSynopsis = notNull(solution.get("?shortSynopsis")) ? solution.get("?shortSynopsis").toString().trim() : "";
            String mediumSynopsis = notNull(solution.get("?mediumSynopsis")) ? solution.get("?mediumSynopsis").toString().trim() : "";
            String longSynopsis = notNull(solution.get("?longSynopsis")) ? solution.get("?longSynopsis").toString().trim() : "";
            String location = notNull(solution.get("?location")) ? solution.get("?location").toString().trim() : "";
            //String material = notNull(solution.get("?material")) ? solution.get("?material").toString().trim() : "";
            String collection = notNull(solution.get("?collection")) ? solution.get("?collection").toString().trim() : "";
            String culture = notNull(solution.get("?culture")) ? solution.get("?culture").toString().trim() : "";
            String foundat = notNull(solution.get("?foundat")) ? solution.get("?foundat").toString().trim() : "";
            String objectType = notNull(solution.get("?objectType")) ? solution.get("?objectType").toString().trim() : "";

            if(memory.contains(objectURI))
                continue;
            else {
                memory.add(objectURI);
                resultsNumber++;
            }

            if(memory2.contains(documentURI))
                continue;
            else {
                memory2.add(documentURI);
            }

            Artefact art = new Artefact(objectURI, documentURI, location,
                    collection, culture, foundat,
                    title, description, imageURL,
                    shortSynopsis, mediumSynopsis, longSynopsis, objectType);

            artefacts.addLast(art);

            //System.out.println(art);

        }

        System.err.println("Number of Artefacts in: "+loc+" is: "+resultsNumber);

    }

	public Artefact getStartArtefact(){
		String query = QueryBank2.getStartObject.replace("START_URI",startObjectURI);
        Log.i("BM.E.getStartArtefact",query);
		ResultSet results = DAP.submit(query);

		while(results.hasNext()){
			QuerySolution solution = results.next();

			String description = notNull(solution.get("?desc")) ? solution.get("?desc").toString() : "";
			String objectURI = notNull(solution.get("?obj")) ? solution.get("?obj").toString() : "";
			String documentURI = notNull(solution.get("?ep")) ? solution.get("?ep").toString() : "";
			String title = notNull(solution.get("?title")) ? solution.get("?title").toString() : "";
			String imageURL = notNull(solution.get("?image")) ? solution.get("?image").toString() : "";
			String shortSynopsis = notNull(solution.get("?shortSynopsis")) ? solution.get("?shortSynopsis").toString() : "";
			String mediumSynopsis = notNull(solution.get("?mediumSynopsis")) ? solution.get("?mediumSynopsis").toString() : "";
			String longSynopsis = notNull(solution.get("?longSynopsis")) ? solution.get("?longSynopsis").toString() : "";
			String location = notNull(solution.get("?location")) ? solution.get("?location").toString() : "";
			//String material = notNull(solution.get("?material")) ? solution.get("?material").toString() : "";
			String collection = notNull(solution.get("?collection")) ? solution.get("?collection").toString() : "";
			String culture = notNull(solution.get("?culture")) ? solution.get("?culture").toString() : "";
			String foundat = notNull(solution.get("?foundat")) ? solution.get("?foundat").toString() : "";
			String objectType = notNull(solution.get("?objectType")) ? solution.get("?objectType").toString() : "";

			Artefact art = new Artefact(objectURI, documentURI, location,
					collection, culture, foundat,
					title, description, imageURL,
					shortSynopsis, mediumSynopsis, longSynopsis, objectType);
			return art;

		}

		return null;
	}

//	public static Comparator<Artefact> ArtefactComparator = new Comparator<Artefact>() {
//
//		public int compare(Artefact art1, Artefact art2) {
//			//descending order
//			return art2.compareTo(art1);
//		}
//
//	};
	
	public LinkedList<Artefact> getArtefacts() {
		return artefacts;
	}
	public void setArtefacts(LinkedList<Artefact> artefacts) {
		this.artefacts = artefacts;
	}

	public boolean notNull(Object o){
		return o != null;
	}

}

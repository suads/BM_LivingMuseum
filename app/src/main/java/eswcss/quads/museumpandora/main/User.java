package eswcss.quads.museumpandora.main;

import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class User {

    public static int HISTORY_BAG_SIZE = 11;

	private static User instance = new User();
	public static User getInstance(){return instance;}
	
	private Artefact currentArtefact;
	private LinkedList<Artefact> history;
	private HashMap<String,Integer> lexicon;
	
	
	private User(){
		lexicon = new HashMap<String,Integer>();
		currentArtefact = Engine.getInstance().getStartArtefact();
		history = new LinkedList<Artefact>();
	}
	
	public String getProfile(){
        Log.i("LEXICON SIZE: ", ""+lexicon.size());
		String profile = "";
		double max = 0;
		for(int val: lexicon.values()){
			if(val > max) max = val;
		}

		for(Entry<String,Integer> entry: lexicon.entrySet()){
            if(max!=0)
			    profile += entry.getKey()+": "+  ( (double) entry.getValue() ) / max +"\n";
            else
                profile += entry.getKey()+": 0\n";

		}
		return profile;
	}

	public void visited(Artefact art){
		currentArtefact = art;
		addToHistory(art);
        Engine.getInstance().getArtefacts().remove(art);
	}
	
	public double similarityWith(Artefact art){
		double scalar = 0;
        double magnitude_B = 0;
        double magnitude_A = 2;

        int[] coefs = new int[4];

		String objectType = art.getObjectType();
		String title = art.getTitle();
		String collection = art.getCollection();
		String foundat = art.getFoundat();
		
		if(lexicon.containsKey(objectType)) {
            coefs[0] = lexicon.get(objectType);
            magnitude_B += Math.pow(coefs[0],2);
            scalar += coefs[0];
        }

		if(lexicon.containsKey(collection)) {
            coefs[1] = lexicon.get(collection);
            magnitude_B += Math.pow(coefs[1],2);
            scalar += coefs[1];
        }

		if(lexicon.containsKey(foundat)) {
            coefs[2] = lexicon.get(foundat);
            magnitude_B += Math.pow(coefs[2],2);
            scalar +=  coefs[2];
        }

		if(lexicon.containsKey(title)) {
            coefs[3] = lexicon.get(title);
            magnitude_B += Math.pow(coefs[3],2);
            scalar += coefs[3];
        }

        if(magnitude_B == 0)
            return 0;

        magnitude_B = Math.sqrt(magnitude_B);

        //cosine similarity
        return  scalar / (magnitude_A * magnitude_B);

	}

    private void dislikes(Artefact art){

        dislike(art.getObjectType());
        dislike(art.getCulture());
        dislike(art.getCollection());
        dislike(art.getFoundat());
        dislike(art.getTitle());
    }

    private void likes(Artefact art){
        like(art.getObjectType());
        like(art.getCulture());
        like(art.getCollection());
        like(art.getFoundat());
        like(art.getTitle());
    }

    public void dislikes(){
		dislikes(currentArtefact);
        if(Engine.getInstance().getArtefacts().size() > 0) {
            currentArtefact = Engine.getInstance().getArtefacts().getFirst();
        } else {
            currentArtefact = Engine.getInstance().getStartArtefact();
        }
        Collections.sort(Engine.getInstance().getArtefacts());
        //Engine.getInstance().getArtefacts().clear();
	}
	
	public void likes(){
		likes(currentArtefact);
        if(Engine.getInstance().getArtefacts().size() > 0) {
            currentArtefact = Engine.getInstance().getArtefacts().getFirst();
        } else {
            currentArtefact = Engine.getInstance().getStartArtefact();
        }
        Collections.sort(Engine.getInstance().getArtefacts());
        //Engine.getInstance().getArtefacts().clear();
    }
	
	public void addToHistory(Artefact art){
		history.addLast(art);
		
		if(history.size()==HISTORY_BAG_SIZE)
			dislikes(history.removeFirst());
		
		like(art.getObjectType());
		like(art.getCulture());
		like(art.getCollection());
		like(art.getFoundat());
		like(art.getTitle());

		for(String key: lexicon.keySet())
            Log.i("BM.E.addToHistory","Lexicon Entry: "+key+"     "+lexicon.get(key));
			//System.err.println("Lexicon Entry: "+key+"     "+lexicon.get(key));
	}

    private void dislike(String feature){
        if(feature!=null && feature.length()!=0){
            Integer old_val = lexicon.get(feature);
            if(old_val==null)
                lexicon.put(feature, -1);
            else
                lexicon.put(feature, old_val-1);
        }
    }

    private void like(String feature){
        if(feature != null && feature.length()!=0){
            Integer old_val = lexicon.get(feature);
            if(old_val==null)
                lexicon.put(feature, 1);
            else
                lexicon.put(feature, old_val+1);
        }
    }

    public String getLocation(){
		return currentArtefact.getLocation();
	}
	
	public Artefact getCurrentArtefact() {
		return currentArtefact;
	}
	public void setCurrentArtefact(Artefact currentArtefact) {
		this.currentArtefact = currentArtefact;
	}
	public LinkedList<Artefact> getHistory() {
		return history;
	}
	public void setHistory(LinkedList<Artefact> history) {
		this.history = history;
	}

	
}

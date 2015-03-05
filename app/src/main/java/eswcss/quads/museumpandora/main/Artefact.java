package eswcss.quads.museumpandora.main;

public class Artefact implements Comparable<Artefact> {

	private String objectURI;
	private String documentURI;
	private String location;
	private String collection;
	private String culture;
	private String foundat;
	private String title;
	private String description;
	private String imageURL;
	private String shortSynopsis;
	private String mediumSynopsis;
	private String longSynopsis;
	private String objectType;

    public Artefact(){

    }
	
	public Artefact(String objectURI, String documentURI, String location,
			 String collection, String culture, String foundat,
			String title, String description, String imageURL,
			String shortSynopsis, String mediumSynopsis, String longSynopsis, String objectType) {
		super();
		this.objectURI = objectURI;
		this.documentURI = documentURI;
		this.location = location;
		this.collection = collection;
		this.culture = culture;
		this.foundat = foundat;
		this.title = title;
		this.description = description;
		this.imageURL = imageURL;
		this.shortSynopsis = shortSynopsis;
		this.mediumSynopsis = mediumSynopsis;
		this.longSynopsis = longSynopsis;
		this.objectType = objectType;
        this.title = title.replace("@en","");

        if(title.length()>0) this.title = this.title + " (" + foundat + ")";

		if(Mappings.locations.containsKey(location)){
			this.location = Mappings.locations.get(location);
		}

        if(imageURL.length() == 0){
            this.imageURL="http://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/240px-No_image_available.svg.png";
        }
	}
	
	public double score(){
		return User.getInstance().similarityWith(this);
	}
	
	public String getObjectType() {
		return objectType;
	}



	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	public String toString(){
		return "\n------------------\nArtefact with score: "+score()+"\n------------------\n"+
				"objectURI: "+objectURI+" \n"+
	"documentURI: "+documentURI+" \n"+
	"location: "+location+" \n"+
	"collection: "+collection+" \n"+
	"culture: "+culture+" \n"+
	"type: "+objectType+" \n"+
	"foundat: "+foundat+" \n"+
	"title: "+title+" \n"+
	"description: "+description+" \n"+
	"imageURL: "+imageURL+" \n"+
	"shortSynopsis: "+shortSynopsis+" \n"+
	"mediumSynopsis: "+mediumSynopsis+" \n"+
	"longSynopsis: "+longSynopsis+" \n";

	}
	
	public String getObjectURI() {
		return objectURI;
	}
	public void setObjectURI(String objectURI) {
		this.objectURI = objectURI;
	}
	public String getDocumentURI() {
		return documentURI;
	}
	public void setDocumentURI(String documentURI) {
		this.documentURI = documentURI;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getCulture() {
		return culture;
	}
	public void setCulture(String culture) {
		this.culture = culture;
	}
	public String getFoundat() {
		return foundat;
	}
	public void setFoundat(String foundat) {
		this.foundat = foundat;
	}
	public String getShortSynopsis() {
		return shortSynopsis;
	}
	public void setShortSynopsis(String shortSynopsis) {
		this.shortSynopsis = shortSynopsis;
	}
	public String getMediumSynopsis() {
		return mediumSynopsis;
	}
	public void setMediumSynopsis(String mediumSynopsis) {
		this.mediumSynopsis = mediumSynopsis;
	}
	public String getLongSynopsis() {
		return longSynopsis;
	}
	public void setLongSynopsis(String longSynopsis) {
		this.longSynopsis = longSynopsis;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public int compareTo(Artefact compareArtefact) {
		 
		double score1 = this.score();
		double score2 = compareArtefact.score();
 		//descending order
		return (int) Math.floor(score2 - score1);
 
	}	
}

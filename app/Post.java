package app;
import java.sql.Timestamp;

import exceptions.TextFormatException;
import interfaces.PostInterface;

public class Post implements PostInterface, Comparable<Post>{
	/*
	 * OVERVIEW: Post e' una classe mutabile. Ogni post ha un id univoco, un autore che 
	 * puo' essere modificato e un testo, modificabile che deve essere non vuoto e al
	 * massimo lungo 140 caratteri. Vi e' inoltre un timestamp che indica la data e 
	 * l'orario della creazione del post
	 *
	 * TYPICAL ELEMENT: <id, author, text, timestamp>. Con id, author e text passati dal
	 * costruttore e timestamp generato automaticamente.
	 * 
	 * FUNZIONE DI ASTRAZIONE: 
	 * <id, author, text, timestamp> dove:
	 * id (int) -> identificatore del post (univocita' garantita in SocialNetwork)
	 * author (String) -> autore del post (validazione dell'input in SocialNetwork)
	 * text (String) -> testo del post con 0<text.length<=140
	 * timestamp (Timestamp) -> data e ora di invio del post
	 * 
	 * INVARIANTE DI RAPPRESENTAZIONE:
	 * author != null && text!=null && (0<text.length<=140)
	 */
    private final int id;
    private String author;
    private String text;
    private final Timestamp timestamp;
   
    protected final static int MAX_TEXT_LENGTH = 140;

    
    public Post(int id, String author, String text) 
			throws TextFormatException, IllegalArgumentException {
		if (author==null)
			throw new IllegalArgumentException("Exception: username cannot be null!");
		if (text==null)
			throw new IllegalArgumentException("Exception: text cannot be null!");
		

		this.author = author;
		this.id = id;
		this.text = text;
		this.timestamp = new Timestamp(System.currentTimeMillis());

		if (text.length() > MAX_TEXT_LENGTH || text.isBlank())
			throw new TextFormatException("Invalid format: text field should" 
			+ " be non empty and 140 characters in length at MOST!");
		try {
			//introduciamo un ritardo per differenziare i timestamp dei post
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.err.println("Exception: thread interrupted!");
		}
	}
	/*
	* OVERVIEW: 
	* Questo metodo restituisce l'id del post
	*/
	public int getId() {
		return id;
	}
	/*
	* OVERVIEW: 
	* Questo metodo restituisce l'autore del post
	*/
	public String getAuthor() {
		return author;
	}
	/*
	* OVERVIEW: 
	* Questo metodo imposta l'autore del post
	*/
    /*REQUIRES: author != null
     *THROWS: IA Exception if author == null
     *EFFECTS: imposta l'utente del SocialNetwork che ha scritto il post
     *MODIFIES: this.author
     */
	protected void setAuthor(String author) throws IllegalArgumentException{
		if (author==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		this.author = author;
	}
	/*
	* OVERVIEW: 
	* Questo metodo restituisce il testo del post
	*/
	public String getText() {
		return text;
	}
	/*
	* OVERVIEW: 
	* Questo metodo imposta il testo del post, che non deve essere vuoto e deve avere 
	* un massimo di 140 caratteri
	*/
    /*REQUIRES: text != null && 0<text.length<=140
     *THROWS: IA Exception if text==null 
     *		and TF Exception(custom) if text.lenth>140 || text.length<=0
     *EFFECTS: imposta il testo del post (non vuoto e massimo 140 caratteri)
     *MODIFIES: this.text
     */
	protected void setText(String text) throws IllegalArgumentException, 
												TextFormatException{
		if (text==null)
			throw new IllegalArgumentException ("Exception: text field cannot be null!");
		if (text.length() > MAX_TEXT_LENGTH || text.isBlank())
			throw new TextFormatException("Invalid format: text field should" 
			+ " be non empty and 140 characters in length at MOST!");
		this.text = text;
	}
	/*
	* OVERVIEW: 
	* Questo metodo restituisce la data e l'orario in cui il post e' stato inserito
	*/
	public Timestamp getTimestamp() {
		return timestamp;
	}
	/*
	* OVERVIEW: 
	* Questo metodo stampa un post con un formato leggibile e distinguendo i seguenti
	* campi: id, autore, timestamp e testo del post
	*/
	public String toString() {
        return "post:[" +
                "id=" + id + ", " +
                "by='" + author + '\'' + ", " +
                "at='" + timestamp + '\'' + ", " +
                "text='" + text + "\']";
    }
	@Override
	public int compareTo(Post p) {
		if(p.getId()>id) return -1;
		if(p.getId()<id) return 1;

		return 0;
	}


}
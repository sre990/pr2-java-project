package app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import exceptions.ForbiddenActionException;
import exceptions.TextFormatException;
import exceptions.PermissionDeniedException;
import interfaces.ModeratedSocialInterface;

public class ModeratedSocial 
extends SocialNetwork implements ModeratedSocialInterface{
	/*
	 * OVERVIEW: ModeratedSocialNetwork e' una collezione mutabile di post e utenti, in cui sono
	 * presenti tutte le funzionalita' della classe SocialNetwork con l'aggiunta di un sistema
	 * di moderazione, segnalazione dei post e censura degli stessi. Un utente moderatore e'
	 * in grado di cancellare altri utenti e post. Inoltre, ogni utente registrato puo' segnalare
	 * un post tramite il suo id; il post segnalato, se presente nella rete sociale, verra' 
	 * aggiunto nella lista dei post segnalati. E' inoltre possibile inserire delle parole nella
	 * lista delle parole proibite e censurare i post che le contengono sostituendole con un *,
	 * oppure rimuovere l'intero post dalla lista dei post della rete sociale.
	 * 
	 * TYPICAL ELEMENT: <social, followers, posts, mods, blacklist, reported>.
	 * 
	 * FUNZIONE DI ASTRAZIONE:
	 * Come la classe social e in piu':
	 * 	Set<String> mods dove:
	 * 	String -> e' uno username del giusto formato
	 * 	
	 * 	Set<String> blacklist dove:
	 * 	String -> e' una parola del giusto formato
	 * 
	 * 	Map<Integer, String> reported dove:
	 * 	Integer -> l'id del post 
	 * 	String -> e' uno username del giusto formato
	 * 
	 * INVARIANTE DI RAPPRESENTAZIONE:
	 * Come la classe social e in piu':
	 * ForAll user in mods . user!=null && user is in social && user!=GUEST
	 * ForAll w in blacklist . w!= null && 140>=w.length>=3 &&
	 * ForAll <id,user> in reported . user is in social && exists p in
	 * posts . p.id == id && p.author==user
	 */
	private static final String CENSORSHIP = "*";
	private Set<String> mods;
	private Set<String> blacklist;
	private Map<Integer, String> reported;
	public ModeratedSocial() {
		super();
		mods = new HashSet<>();
        blacklist = new HashSet<>();
        reported = new HashMap<>();
	}
	/*
    * OVERVIEW:
	* Questa funzione permette di aggiungere dei moderatori alla lista di moderatori
	* della rete soicale. Ciascun moderatore deve essere registrato nella rete sociale
	* e non puo' essere un ospite.
	*/
	public void addModerator(String username) 
			throws IllegalArgumentException, NoSuchElementException, ForbiddenActionException,
					PermissionDeniedException{
		if (username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (!social.containsKey(username))
			throw new NoSuchElementException("Exception: the social network contains no such user!");
		if (getMods().contains(username))
			throw new ForbiddenActionException("Forbidden action: the user is already a moderator!");
		if (username.equals(GUEST))
			throw new PermissionDeniedException ("Permission denied: guests cannot be moderators!");
		getMods().add(username);
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di rimuovere un moderatore dalla lista dei moderatori della
	* rete sociale. 
	*/
	public void removeModerator(String username) 
			throws IllegalArgumentException, NoSuchElementException {
		if (username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (!social.containsKey(username))
			throw new NoSuchElementException("Exception: the social network contains no such user!");
		if (!getMods().contains(username))
			throw new NoSuchElementException("Exception: the user is not a moderator!");
		getMods().remove(username);
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce i moderatori della rete sociale.
	*/
	public Set<String> getMods() {
		return mods;
	}
	/*
	* OVERVIEW
	* Questo metodo stampa i post che hanno ricevuto una segnalazione.
	*/
	public void printReported() {
		for (Entry<Integer, String> r : reported.entrySet()) {
			System.out.println("Post #" + r.getKey() + " by "
				+ r.getValue() + " has been reported.");
		}	
	}
	/*
    * OVERVIEW:
	* Questo metodo permette a un utente registrato di segnalare un post della rete
	* social. Ciascun post puo' essere segnalato una sola volta.
	*/
	public void reportPost (int id, String reporter) 
			throws IllegalArgumentException, PermissionDeniedException, 
					NoSuchElementException, ForbiddenActionException {
		if (reporter==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (reporter==GUEST || !social.containsKey(reporter))
			throw new PermissionDeniedException ("Permission denied: guests cannot perform this action!");
		for (Post p : posts) {				
			if (p.getId() == id) {	
				if (reported.containsKey(p.getId()))
					throw new ForbiddenActionException("Exception: the post has already been reported!");
				reported.put(p.getId(), p.getAuthor());
				return;//l'id e' univoco, non ne troveremo altri
			}
		}
		throw new NoSuchElementException("Exception: the social network contains no such id!");
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di scorrere una lista di post e, se troviamo la parola
	* chiave "report:", seguita da un id, aggiungiamo il post corrispondente a quell'id alla lista dei 
	* post segnalati
	*/
	public void addToReported(List<Post> ps) 
			throws IllegalArgumentException, NullPointerException {
        if (ps == null) 
    		throw new IllegalArgumentException("Exception: input list cannot be null!");
		for(Post p : ps) {
			if (p == null) 
				throw new NullPointerException ("Exception: elements of input cannot be null!");
	        if (p.getText().toLowerCase().contains("report:")) {
				//separo la keyword "report" dall'id del post che voglio segnalare
			    String[] splitText= p.getText().split(":"); 		  
			    //mi ricavo il post originario tramite il suo id
			    //e lo aggiungendolo nella lista dei post segnalati	
	    		try {
	    			//prendo l'id del post a cui l'utente ha messo like
				    int idOriginal = Integer.parseInt(splitText[1].trim());
				    //e faccio la segnalazione
					reportPost(idOriginal, p.getAuthor());
	    		} catch (NumberFormatException e ) {
	            	System.err.println("Invalid format: the id should be a number!");
	            	e.printStackTrace();
				} catch (IllegalArgumentException 
						| NoSuchElementException 
						| ForbiddenActionException
						| PermissionDeniedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*
    * OVERVIEW:
	* Questo metodo permette rimuovere un post dalla lista dei post segnalati.
	*/
	public void removeFromReported(int id) throws NoSuchElementException {
		//se non abbiamo trovato nessun post con questo id significa che non era presente nella
		//lista dei post segnalati
		if(!reported.containsKey(id))
			throw new NoSuchElementException ("Exception: the reported list contains no such id!");
		reported.remove(id);
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di aggiungere una parola alla lista nera delle parole
	* proibite nella rete sociale. Ciascuna parola deve essere lunga minimo tre
	* caratteri e al massimo 140.
	*/
	public void addToBlackList(String word) 
			throws IllegalArgumentException, TextFormatException, ForbiddenActionException{
		if (word==null)
			throw new IllegalArgumentException ("Exception: input cannot be null!");
		boolean isRightLength = (word.length() <= Post.MAX_TEXT_LENGTH
				&& word.length() >= MIN_WORD_LENGTH);
		if (!isRightLength) 
			throw new TextFormatException("Invalid format: blacklisted words cannot be shorter than 3" +
					" or longer than 140 characters!");
		word = word.toLowerCase();
		if (getBlacklist().contains(word)) 
			throw new ForbiddenActionException ("Forbidden action: the word is already blacklisted!");
		getBlacklist().add(word);
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di rimuovere una parola dalla lista nera delle parole 
	* proibite nella rete sociale.
	*/
	public void removeFromBlackList(String word) 
			throws IllegalArgumentException, NoSuchElementException{
		if (word==null)
			throw new IllegalArgumentException ("Exception: input cannot be null!");
		word = word.toLowerCase();
		if (!getBlacklist().contains(word))
			throw new NoSuchElementException("Exception: the blacklist contains no such word!");
		getBlacklist().remove(word);
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce la lista nera delle parole proibite nella rete sociale.
	*/
	public Set<String> getBlacklist() {
		return blacklist;
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di rimuovere un utente dalla rete sociale. I guest non possono
	* essere rimossi. Ciascun utente puo' cancellare solo il proprio account, a meno che non
	* sia un moderatore, in tal caso puo' cancellare il suo account e quello degli utenti
	*/
	@Override
	public void deleteUser(String deleter, String username) 
			throws IllegalArgumentException, NoSuchElementException, 
			ForbiddenActionException, PermissionDeniedException{
		if (deleter==null || username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (!(social.containsKey(username) && social.containsKey(deleter)))
			throw new NoSuchElementException ("Exception: the social network contains no such user!");
		if (username.equals(GUEST)) 
			throw new ForbiddenActionException ("Forbidden action: guests cannot be deleted!");
		if (!(deleter.equals(username) || getMods().contains(deleter)))
			throw new PermissionDeniedException ("Permission denied: non moderators cannot perform this action");
		for (Map.Entry<String, Set<String>> user : social.entrySet()) {	
			getFollowing(user.getKey()).remove(username);
		}
		for (Map.Entry<String, Set<String>> user : followers.entrySet()) {	
			getFollowers(user.getKey()).remove(username);
		}
		if (getMods().contains(deleter) && getMods().contains(username)) mods.remove(username);
		social.remove(username);
		followers.remove(username);	

	}
	/*
    * OVERVIEW:
	* Questo metodo permette di cancellare un post dalla lista dei post della rete sociale.
	* Questa azione e' concessa ai moderatori e agli autori del post. Gli ospiti non posssono
	* cancellare i post.
	*/
	@Override
	public void deletePost(String deleter, int id) 
			throws IllegalArgumentException, PermissionDeniedException, NoSuchElementException{
		if (deleter==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (deleter==GUEST)
			throw new PermissionDeniedException ("Permission denied: guests cannot perform this action!");				
		for (Post p : posts) {				
			if (p.getId() == id) {
				//se non l'ho scritto io e non sono un moderatore non
				//posso cancellarlo
				if (!(p.getAuthor().equals(deleter) || getMods().contains(deleter))) 
					throw new PermissionDeniedException ("Permission denied: non moderators cannot perform this action!");				
				posts.remove(p);
				return;//usciamo perche' l'id e' univoco, non ne troveremo altri
			}
		}
		//se siamo arrivati alla fine della lista dei post senza trovarne uno con
		//lo stesso id, vuol dire che l'id non e' stato trovato nella lista 
		throw new NoSuchElementException ("Exception: the social network contains no such id!");
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di rimuovere automaticamente dalla lista dei post della rete sociale,
	* ogni messaggio il cui testo contenga una parola presente nella lista nera.
	*/
	public void filterPosts() {
		//uso un iteratore perche' devo rimuovere dei post mentre sto iterando sulla lista dei
		//post e avrei un'eccezione.
		Iterator<Post> iter = posts.iterator();
		while (iter.hasNext()) {
		    Post p = iter.next();
		    for (String s : blacklist) {
	        	//la ricerca non e' case-sensitive
	            if (p.getText().toLowerCase().contains(s.toLowerCase())) {
	            	iter.remove();
	                break;//almeno *una* parola, quando la trovo esco
	            }
	        }
		}
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di censurare i post contenti una parola presente nella lista nera,
	* sostituendola con un asterisco. 
	*/
	public void censorPosts() {
		for (Post p : posts) {
		    for (String s : blacklist) {
	        	//la ricerca non e' case-sensitive
	            if (p.getText().toLowerCase().contains(s.toLowerCase())) {          
            		String oldText = p.getText().toLowerCase();
            		String censoredText = oldText.replaceAll(s.toLowerCase(), CENSORSHIP);
            		//System.out.println(censoredText);//test
					try {
						p.setText(censoredText);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (TextFormatException e) {
						e.printStackTrace();
					}			
	            }
	        }
		}
	}
}
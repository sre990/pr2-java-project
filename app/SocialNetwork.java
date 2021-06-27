package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import exceptions.*;
import interfaces.SocialNetworkInterface;
/*
 * OVERVIEW: SocialNetwork e' una collezione mutabile di post e utenti, in cui ogni
 * utente registrato puo' mettere like al post di un altro (e quindi seguirlo), 
 * taggarlo, seguirlo o smettere di seguirlo. Esiste anche la possibilita' di 
 * postare sul SocialNetwork da guest, pur con funzionalita' limitate: un ospite
 * non puo' seguire altri utenti, essere seguito o mettere like.
 * 
 * TYPICAL ELEMENT: <social, followers, posts>.
 * 
 * FUNZIONE DI ASTRAZIONE:
 * Map<String, Set<String>> social dove:
 * 	String -> e' uno username del giusto formato
 * 	Set<String> -> la lista degli utenti registrati seguiti dallo username
 * 	di cui sopra.
 * 
 * Map<String, Set<String>> followers dove:
 * 	String -> username del giusto formato
 * 	Set<String> -> la lista degli utenti registrati che seguono lo username
 * 	di cui sopra.
 * 
 * Set<Post> posts dove:
 * 	Post -> e'un post come definito nella classe Post
 * 
 * INVARIANTE DI RAPPRESENTAZIONE:
 * 	social != null && posts != null && followers !=null
 *    //parte dell'invariante relativa alla map SOCIAL
 *	&& {ForAll <user, following> in social . user != null && following != null 
 * && [~Exist u in following . u== null && u==user && u==GUEST]
 * && user!=GUEST && 24=>user.length=>3 && 
 *	ForAll c in user . c=='0..9' || c=='a..z' || c=='A..Z' || c=='.' || c=='_'}
 *    //parte dell'invariante relativa alla map FOLLOWERS
 *	&& {ForAll <user, followedBy> in followers . user != null && 
 * followedBy != null && [~Exist u in followedBy . u == null && 
 *                       u==user && u==GUEST]
 * && user!=GUEST && 24=>user.length=>3 && 
 *	ForAll c in user . c=='0..9' || c=='a..z' || c=='A..Z' || c=='.' || c=='_'}
 *    //parte dell'invariante relativa al set POSTS
 *	&& ForAll p in posts . p != null
 */
public class SocialNetwork implements SocialNetworkInterface {
	protected Map<String, Set<String>> social;
	protected Map<String, Set<String>> followers;
	protected Set<Post> posts;
	
    protected static final int MIN_WORD_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 24;

    public static final String GUEST = "GUEST";
    private static final String PREFIX = "@";
    private static final String REGEX = 
    		"\\B"+PREFIX+
    		"(?!(?:[a-z0-9.]*_){2})(?!(?:[a-z0-9_]*\\.){2})[._a-z0-9]{"+
    		MIN_WORD_LENGTH+","+MAX_NAME_LENGTH+"}\\b";
    public SocialNetwork() {
		social = new HashMap<>();
		followers = new HashMap<>();
        posts = new TreeSet<>();
	}

    /*
    * OVERVIEW:
    * Questo metodo restituisce la rete sociale derivata dalla lista di post,
    * dove ogni elemento della map ha un campo chiave (l'utente), e un campo
    * valore (gli autori che sta seguendo). Assumiamo che mettendo follow
    * a un utente o like all'id di un suo post lo si cominci a seguire. 
    */
	public Map<String, Set<String>> guessFollowers(List<Post> ps) 
			throws IllegalArgumentException, NullPointerException, PermissionDeniedException, 
					NoSuchElementException {
        if (ps == null) 
    		throw new IllegalArgumentException("Exception: input list cannot be null!");    
		//faccio un distinguo tra i post veri e propri, i follow e i like
		HashMap<Integer,Post> postsMap = new HashMap<>();
		HashMap<Integer,Post> followsMap = new HashMap<>();
        HashMap<Integer,Post> likesMap = new HashMap<>();
		//divido tutti i post in base al tipo (like, follow, post normali) e
        //da qui comincio a costruire la rete sociale
		for (Post p : ps) {
        	if (p == null) 
        		throw new NullPointerException ("Exception: elements of input cannot be null!");
			//se l'autore del post non e' gia' presente nella rete sociale e' 
			//un guest. 
			if (!social.containsKey(p.getAuthor())) p.setAuthor(GUEST);
			//i follow sono dei post il cui testo inizia con "follow:",
			//seguito dal nome dell'utente che l'autore vuole seguire.
			if (p.getText().toLowerCase().contains("follow:")) 
				//ai guest non e' permesso seguire altri utenti
				if (p.getAuthor().equals(GUEST))
					throw new PermissionDeniedException ("Permission denied: guest users cannot perform this action!");
				else 
					followsMap.put(p.getId(), p);
			//i like non sono altro che dei post il cui testo inizia con "like:",
			//seguito dall'id del post a cui si vuole mettere like.
			else if (p.getText().toLowerCase().contains("like:")) 
				//ai guest non e' permesso mettere like ad altri utenti
				if (p.getAuthor().equals(GUEST))
					throw new PermissionDeniedException ("Permission denied: guest users cannot perform this action!");					
				else likesMap.put(p.getId(), p);
			//tutti -anche gli utenti non registrati- possono postare
			else postsMap.put(p.getId(), p);
		}
		//per ogni azione di following prendo l'autore che l'utente vuole seguire
		//e lo aggiungo alla lista degli autori che sta seguendo.
		for (Post f : followsMap.values()) {
			//separo la keyword "follow" dal nome dell'autore che voglio seguire
			String[] splitText = f.getText().split(":");
			//prendo l'autore che voglio seguire
			String followedAuthor = splitText[1].trim();
			//controllo che l'autore esista e che l'utente non lo stia gia'
			//seguendo
			if (!social.containsKey(followedAuthor))
				throw new NoSuchElementException("Exception: the social network contains no such user!");
			boolean isFollowing = 
					getFollowing(f.getAuthor()).contains(followedAuthor);
			//se lo sta gia' seguendo passo avanti
			if(isFollowing) continue;
			//altrimenti inserisco l'utente alla lista dei follower dell'autore 
			//a cui ha messo follow
			try {
				startFollowing(f.getAuthor(), followedAuthor);
			} catch (IllegalArgumentException | NoSuchElementException | PermissionDeniedException
					| ForbiddenActionException e) {
				e.printStackTrace();
			}
		}
		//per ogni like prendo il post corrispondente (tramite l'id)
    	//aggiungo chi ha messo like nel set dei follower dell'autore del post
        for (Post l : likesMap.values()) {    
        	//separo la keyword "like" dall'id del post a cui voglio mettere like
            String[] splitText = l.getText().split(":");
            Post likedPost = null;
            //prendo l'id del post a cui l'utente ha messo like
            try {
            	int idLiked = Integer.parseInt(splitText[1].trim());
                //controllo che il post esista (non null) e mi ricavo il post originario 
                //tramite il suo id
                likedPost = postsMap.get(idLiked);
            } catch(NumberFormatException e) {
            	System.err.println("Invalid format: the id should be a number!");
            	e.printStackTrace();
            }
            if (likedPost==null)
            	throw new NoSuchElementException("Exception: the social network contains no such ID!");
			boolean isFollowing = 
					getFollowing(l.getAuthor()).contains(likedPost.getAuthor());
			boolean isAutoLike = 
					l.getAuthor().equals(likedPost.getAuthor());
			//se sta gia' seguendo l'autore o ha messo like a un suo stesso post
			//passo avanti. Non si tratta di errori.
			if(isFollowing || isAutoLike) continue;            
			//dato che ho assunto che mettendo like a un autore si diventa suo
            //follower, aggiungo l'autore a cui l'utente ha messo like alla lista
            //degli autori da lui seguiti
            try {
				startFollowing(l.getAuthor(), likedPost.getAuthor());
			} catch (IllegalArgumentException | NoSuchElementException | PermissionDeniedException
					| ForbiddenActionException e) {
				e.printStackTrace();
			}
        }
        return social;
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce gli utenti più influenti delle rete sociale,
    * ovvero quelli che hanno un numero maggiore di “follower”. Affinche' un
	* autore possa comparire nella lista, deve avere almeno un follower.
	*/
	public List<String> influencers() {
		//uso una struttura dati per memorizzare coppie chiave/valore, contenenti
		//autore e relativo numero di follower. Da qui ordino per numero di follower
		//e costruisco una lista ordinata.
		List<String> influencers = new ArrayList<>();
		Map<String, Integer> followedBy = new HashMap<>();
		int followersSize = 0;
		//la mia nuova hashmap e' costituita da tutte le coppie utente/numero di 
		//follower ricavate dalla rete sociale.
		for (Map.Entry<String, Set<String>> user : social.entrySet()) {
			followersSize = getFollowers(user.getKey()).size();
			//se l'utente ha zero follower lo salto
			if(followersSize==0) continue;
			followedBy.put(user.getKey(), followersSize);
		}
		//per ordinare l'hashmap in ordine decrescente di valori, chiamo entrySet()
		//che mi da tutti gli elementi della map e converto in una lista
        List<Map.Entry<String, Integer>> sortedByFollowers = 
        		new ArrayList<>(followedBy.entrySet());
        //poi ordino la lista per ordine decrescente di follower
        sortedByFollowers
        	.sort(Map.Entry
        			.comparingByValue(Comparator
        					.reverseOrder()));
        //infine aggiungo i nomi degli influencer alla mia lista di influencer.
        for (Entry<String, Integer> i : sortedByFollowers) {
            influencers.add(i.getKey());
        }
		return influencers;
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce tutti gli utenti menzionati (inclusi) nella
	* rete sociale. Per ricavarli occorre restituire il campo String della
	* struttura Map<String, Set<String>> che implementa la rete sociale.
	* Assumiamo che, in parole povere, restituisca gli utenti registrati.
	*/
	public Set<String> getMentionedUsers() {
		Set<String> mentionedUsers = new HashSet<String>();
		for (Map.Entry<String, Set<String>> user : social.entrySet()) {
			if (user.getKey().equals(GUEST)) continue;
			mentionedUsers.add(user.getKey());
		}		
		return mentionedUsers;
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce gli utenti menzionati (inclusi) nella lista dei
	* post. Dunque si comporta come un tag all'interno di un post. Assumiamo che 
	* le menzioni siano del formato "@nome_utente".
	* Assumiamo che un utente possa taggare un altro che non sta seguendo, purche'
	* sia iscritto.
	*/
	public Set<String> getMentionedUsers(List<Post> ps) 
			throws IllegalArgumentException, NullPointerException, NoSuchElementException {
        if (ps == null) 
    		throw new IllegalArgumentException("Exception: input list cannot be empty!");
		Set<String> taggedAuthors = new HashSet<>();
		Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher;
		String taggedUser;
        for (Post p : ps) {
        	if (p == null) 
        		throw new NullPointerException ("Exception: elements of input cannot be null");
            matcher = pattern.matcher(p.getText());
            while (matcher.find()) {
            	taggedUser = matcher.group().replace(PREFIX, "");
            	//se l'utente mezionato e' registrato nella rete sociale, allora
            	//lo aggiungo tra gli utenti menzionati
            	if (!social.containsKey(taggedUser) || taggedUser.equals(GUEST)) 
            		throw new NoSuchElementException("Exception: the social network contains no such user!");
            	taggedAuthors.add(taggedUser);
            }
        }
        return taggedAuthors;
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce la lista dei post scritti dall’utente nella 
	* rete sociale il cui nome e' dato dal parametro username
	*/
	public List<Post> writtenBy(String username) 
			throws IllegalArgumentException, NoSuchElementException {
		if (username == null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
        if (!(social.containsKey(username)))
        	throw new NoSuchElementException("Exception: the social network contains no such user!");
        List<Post> list = new ArrayList<>();
        for (Post p : posts) {
            if (p.getAuthor().equals(username)) list.add(p);
        }
        return list;
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce la lista dei post effettuati dall’utente il cui
	* nome e' dato dal parametro username presenti nella lista ps
	*/
	public List<Post> writtenBy(List<Post> ps, String username) 
			throws IllegalArgumentException, NullPointerException, NoSuchElementException {
        if (username == null) 
    		throw new IllegalArgumentException("Exception: username cannot be null!");
		if (ps == null) 
    		throw new IllegalArgumentException("Exception: input list cannot be null!");	
        if (!(social.containsKey(username)))
        	throw new NoSuchElementException("Exception: the social network contains no such user!");
        List<Post> list = new ArrayList<>();
        for (Post p : ps) {
        	if (p == null)
        		throw new NullPointerException ("Exception: elements of input cannot be null");
            if (p.getAuthor().equals(username)) list.add(p);
        }
        return list;
	}
    /*
    * OVERVIEW:
    * Questo metodo restituisce la lista dei post presenti nella rete sociale 
    * che includono almeno una delle parole presenti nella lista delle parole 
	* argomento del metodo
	*/
	public List<Post> containing(List<String> words) 
			throws IllegalArgumentException, NullPointerException {
		if (words == null) 
    		throw new IllegalArgumentException("Exception: input list cannot be empty!");
		for(String s : words) 
        	if (s == null) 
        		throw new NullPointerException ("Exception: elements of input cannot be null!");
		List<Post> list = new ArrayList<>();
		for (Post p : posts) {
	    	for (String s : words) {
	        	//la ricerca non e' case-sensitive
	            if (p.getText().toLowerCase().contains(s.toLowerCase())) {
	            	list.add(p);
	                break;//almeno *una* parola, quando la trovo esco
	            }
	        }
	    }
	    return list;
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce un post e permette di aggiungerlo alla lista dei post
	* della rete sociale.
	*/
	public Post createPost(int id, String username, String text) 
			throws IllegalArgumentException, DuplicateException {
		if (username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (text== null)
			throw new IllegalArgumentException ("Exception: text cannot be null!");
		if (!social.containsKey(username))
            username = GUEST;
		for (Post p : posts) {
			if (p.getId() == id) 
				throw new DuplicateException ("Duplicate: this ID is already taken!");
		}
        Post p = null;
		try {
			p = new Post(id, username, text);
		} catch (IllegalArgumentException | TextFormatException e) {
			e.printStackTrace();
		}
		posts.add(p);

		return p;
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di rimuovere un post dalla lista dei post della
	* rete sociale. Cio' e' permesso soltanto agli autori del post (chi lo ha 
	* scritto) e ai moderatori (cfr. classe ModeratedSocial).
	* I guest non possono eliminare i post.
	*/
	public void deletePost(String deleter, int id) 
			throws IllegalArgumentException, PermissionDeniedException, NoSuchElementException {
		if (deleter==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (deleter==GUEST)
			throw new PermissionDeniedException ("Permission denied: guests cannot perform this action!");	
		for (Post p : posts) {				
			if (p.getId() == id) {
				//se non l'ho scritto io non posso cancellarlo
				if (!p.getAuthor().equals(deleter)) 
					throw new PermissionDeniedException ("Permission denied: users can only delete their own posts!");				
				posts.remove(p);
				return;//usciamo perche' l'id e' univoco, non ne troveremo altri
			}
		}
		//se siamo arrivati alla fine della lista dei post senza trovarne uno con
		//lo stesso id, vuol dire che l'id non e' stato trovato nella lista 
		throw new NoSuchElementException ("Exception: the social network contains no such ID!");
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di registrare un utente nella rete sociale, a
	* condizione che lo username scelto sia valido e che non sia gia' stato preso
	*/
	public void createUser(String username) 
			throws IllegalArgumentException,TextFormatException, DuplicateException {
		if (username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		//Controllo che lo username scelto sia composto da lettere, numeri, dot 
		//e underscore e che sia della lunghezza corretta
		boolean isRightLength = (username.length() >= MIN_WORD_LENGTH
				&& username.length() <= MAX_NAME_LENGTH);
		Pattern pattern = 
				Pattern.compile("[A-Za-z0-9_.]+", Pattern.CASE_INSENSITIVE);
		if (!(pattern.matcher(username).matches() && isRightLength)) {
        	throw new TextFormatException("Invalid format:"+
        	" usernames should be 3-24 characters long and contain only letters"
            + ", numbers, dots and underscores.");
        }
		//Se dopo i controlli sullo username non e' gia' preso lo aggiungiamo
		//alla rete sociale. All'inizio un nuovo utente non segue nessuno.
		if (social.containsKey(username)) 
			throw new DuplicateException ("Duplicate: this username is already taken!");
		social.put(username, new HashSet<>());
		followers.put(username, new HashSet<>());
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di rimuovere un utente dalla rete sociale (bannarlo). 
	* E' concesso solo ai moderatori (cfr. classe ModeratedSocial) e gli altri utenti
	* possono farlo solo con se stessi.
	*/
	public void deleteUser(String deleter, String username) 
			throws IllegalArgumentException, NoSuchElementException, 
					PermissionDeniedException, ForbiddenActionException{
		if (deleter==null || username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (username.equals(GUEST)||deleter.equals(GUEST)) 
			throw new ForbiddenActionException ("Forbidden action: guests cannot delete accounts or be deleted!");
		if (!(social.containsKey(username) && social.containsKey(deleter)))
			throw new NoSuchElementException ("Exception: the social network contains no such user!");
		if (!deleter.equals(username))
			throw new PermissionDeniedException ("Permission denied: users can only delete their own accounts!");

		for (Map.Entry<String, Set<String>> user : social.entrySet()) {	
			getFollowing(user.getKey()).remove(username);
		}
		for (Map.Entry<String, Set<String>> user : followers.entrySet()) {	
			getFollowers(user.getKey()).remove(username);
		}
		social.remove(username);
		followers.remove(username);
	}
	/*
	* Questo metodo restituisce tutti i follower di un autore
	*/
	public Set<String> getFollowers(String username) 
			throws IllegalArgumentException, NoSuchElementException{
		if (username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (!social.containsKey(username))
			throw new NoSuchElementException("Exception: the social network contains no such user!");
		Set <String> followersSet = followers.get(username);

		return followersSet;
	}
	/*
    * OVERVIEW:
	* Questo metodo restituisce tutti gli autori seguiti dall'utente
	*/
	public Set<String> getFollowing(String username) 
			throws IllegalArgumentException, NoSuchElementException {
		if (username==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (!social.containsKey(username))
			throw new NoSuchElementException("Exception: the social network contains no such user!");
		Set<String> followingSet = social.get(username);
		
		return followingSet;
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di aggiungere un autore alla lista degli autori
	* seguiti da un utente
	*/
	public void startFollowing(String follower, String followed) 
			throws IllegalArgumentException,NoSuchElementException, 
			PermissionDeniedException, ForbiddenActionException{	
		//controllo che entrambi gli utenti esistano nella rete sociale
		if (follower==null || followed==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		if (!(social.containsKey(follower) && social.containsKey(followed))) 
			throw new NoSuchElementException("Exception: the social network contains no such user!");
		//controllo che gli utenti non siano dei guest
		if (follower.equals(GUEST) || followed.equals(GUEST))
			throw new PermissionDeniedException("Permission denied: guests cannot follow or be followed!");
		//controllo che l'utente non voglia seguire se stesso.
		if (followed.equals(follower)) 
			throw new ForbiddenActionException("Forbidden action: users cannot follow themselves!");
		boolean isFollowing = getFollowing(follower).contains(followed);
		if(isFollowing)
			throw new ForbiddenActionException("Forbidden action: you are already following the author!");
		//aggiungo l'utente alla lista dei follower dell'autore a cui ha
		//messo follow.
		social.get(follower).add(followed);
		followers.get(followed).add(follower);
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di rimuovere un autore alla lista degli autori
	* seguiti da un utente
	*/
	public void stopFollowing(String follower, String followed) 
			throws IllegalArgumentException, NoSuchElementException, ForbiddenActionException{
		if (follower==null || followed==null)
			throw new IllegalArgumentException ("Exception: username cannot be null!");
		//controllo che entrambi gli utenti esistano nella rete sociale
		if (!(social.containsKey(follower) && social.containsKey(followed))) 
			throw new NoSuchElementException("Exception: the social network contains no such user!");
		//controllo che l'utente stia seguendo l'autore che vuole smettere di 
		//seguire
		boolean isFollowing = getFollowing(follower).contains(followed);
		if (!isFollowing) 
			throw new ForbiddenActionException ("Forbidden action: users can only unfollow authors that they are" + 
					" already following!");
		getFollowing(follower).remove(followed);
		getFollowers(followed).remove(follower);
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di stampare tutti gli autori della rete sociale
	* e relativo numero di follower
	*/
	public void printAuthorsAndFollowers() {
		for (Map.Entry<String, Set<String>> user : social.entrySet()) {
			System.out.println("L'autore \"" + user.getKey() +"\"" +
				" e' seguito da " + getFollowers(user.getKey()).size() + 
				" utente/i.");

		}
		System.out.println("----------");	
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di stampare tutti gli utenti della rete sociale
	* e relativo numero di autori che stanno seguendo
	*/
	public void printUsersAndFollowing() {
		for (Map.Entry<String, Set<String>> user : social.entrySet()) {
			System.out.println("L'utente \"" + user.getKey() +"\"" +
				" segue " + getFollowing(user.getKey()).size() + 
				" autore/i.");

		}
		System.out.println("----------");	
	}
	/*
    * OVERVIEW:
	* Questo metodo permette di stampare tutti i post della rete sociale.
	*/
	public void printPosts() {
		for (Post p : posts) {
			System.out.println(p);		
		}
		System.out.println("----------");
	}

}
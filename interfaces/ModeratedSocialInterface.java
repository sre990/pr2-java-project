package interfaces;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import app.Post;
import exceptions.*;

public interface ModeratedSocialInterface {
    /*REQUIRES: username != null && exists user in social . user==username &&
     * 			[~exists user in mods . user==username] && username!=GUEST
     *THROWS: IAE if username==null and NSEE if ~exists user in social . user==username
     *			and FAE if [exists user in mods . user==username] and PDE if username==GUEST
     *EFFECTS: aggiunge un utente alla lista dei moderatori della rete sociale
     *MODIFIES: mods
     */
	void addModerator(String username)
			throws IllegalArgumentException, NoSuchElementException, ForbiddenActionException,
					PermissionDeniedException;
	
    /*REQUIRES: username != null && exists user in social . user==username &&
     * 			exists user in mods . user==username
     *THROWS: IAE if username==null and NSEE if ~exists user in social . user==username
     *			and ~exists user in mods . user==username
     *EFFECTS: rimuove un moderatore alla lista dei moderatori della rete sociale
     *MODIFIES: mods
     */
	void removeModerator(String username) 
			throws IllegalArgumentException, NoSuchElementException;
	
    /*REQUIRES: true
     *THROWS:
     *EFFECTS: restituisce la lista dei moderatori della rete sociale
     *MODIFIES:
     */
	Set<String> getMods();
	
    /*REQUIRES: true
     *THROWS:
     *EFFECTS: stampa tutti i post che hanno ricevuto una segnalazione
     *MODIFIES:
     */
	void printReported();
	
    /*REQUIRES: reporter!=null && [reporter!=GUEST && exists user 
     * 			in social . user==reporter] && [exists p in posts . id==p.id]
     * 			&& [exists p in posts . id==p.id && ~exists rId in reported . rId==p.id]
     *THROWS: IAE if reported==null and PDE if [reporter!=GUEST && exists user 
     * 			in social . user==reporter] and NSEE if [exists p in posts . id==p.id]
     *			and FAE if [exists p in posts . id==p.id && exists rId in reported . rId==p.id]
     *EFFECTS: aggiunge tutti i post che hanno ricevuto una segnalazione alla lista
     *			dei post segnalati
     *MODIFIES: reported
     */
	void reportPost (int id, String reporter) 
			throws IllegalArgumentException, PermissionDeniedException, 
			NoSuchElementException, ForbiddenActionException;
	
    /*REQUIRES: reporter!=null && ~exists p in ps . p==null
     *THROWS: IAE if reported==null and NPE if exists p in ps . p==null
     *EFFECTS: aggiunge tutti i post della lista data in input che hanno ricevuto una 
     *			segnalazione alla lista dei post segnalati
     *MODIFIES: reported
     */
	void addToReported(List<Post> ps) 
			throws IllegalArgumentException, NullPointerException;
	
    /*REQUIRES: exists rId in reported . rId==id
     *THROWS: NSEE if ~exists rId in reported . rId==id
     *EFFECTS: rimuove un post alla lista dei post che hanno ricevuto una segnalazione
     *MODIFIES: reported
     */
	public void removeFromReported(int id) throws NoSuchElementException;
    
	/*REQUIRES: word!=null && 3<=username<=140 && ~exists w in blacklist . w==word
     *THROWS: IAE if word==null and TFE if username>140 || username<3 and
     *			FAE if exists w in blacklist . w==word
     *EFFECTS: aggiunge una parola alla lista nera delle parole proibite nella rete sociale
     *MODIFIES: blacklist
     */
	void addToBlackList(String word) 
			throws IllegalArgumentException, TextFormatException, ForbiddenActionException;
    
	/*REQUIRES: word!=null && exists w in blacklist . w==word
     *THROWS: IAE if word==null and NSEE if ~exists w in blacklist . w==word
     *EFFECTS: rimuove una parola alla lista nera delle parole proibite nella rete sociale
     *MODIFIES: blacklist
     */
	void removeFromBlackList(String word) 
			throws IllegalArgumentException, NoSuchElementException;
    
	/*REQUIRES: true
     *THROWS:
     *EFFECTS: restituisce la lista nera delle parole proibite nella rete sociale
     *MODIFIES:
     */
	Set<String> getBlacklist();
	
    /*REQUIRES: deleter!=null && username!=null && 
     * 			[exist user1,user2 in social . user1==deleter && user2==username]
     * 			&& username!=GUEST && 
     * 			[deleter==username || exists user in mods . user==deleter]
     *THROWS: IAE if deleter==null and username==null and NSEE if 
     *			[~exist user1,user2 in social . user1==deleter && user2==username]
     *			and FAE if username==GUEST and 
     *			PDE if [deleter!=username && ~exists user in mods . user==deleter]
     *EFFECTS: rimuove un utente dalla rete sociale il cui nome e' dato dal parametro
     *			username
     *MODIFIES: social, followers, mods (if exist deleter, username in mods) 
     */
	void deleteUser(String deleter, String username) 
			throws IllegalArgumentException, NoSuchElementException, 
			ForbiddenActionException, PermissionDeniedException;
	
    /*REQUIRES: deleter!=null && deleter!=GUEST && 
     * 			exists p in posts . p.id==id &&
     *			[exist p in posts . p.id==id && p.author==deleter 
     *			|| exists user in mods . user==deleter]
     *THROWS: IAE if deleter==null and PDE if deleter==GUEST or 
     *		 [exist p in posts . p.id==id && p.author!=deleter 
     *			|| ~exists user in mods . user==deleter] and
     *		NSEE if ~exists p in posts . p.id==id
     *EFFECTS: rimuove un post dalla lista dei post della rete sociale
     *MODIFIES: posts
     */
	void deletePost(String deleter, int id) 
			throws IllegalArgumentException, PermissionDeniedException, NoSuchElementException;
    
	/*REQUIRES: true
     *THROWS:
     *EFFECTS: rimuove autmaticamente dalla lista dei post della rete sociale,
	 * 			ogni messaggio il cui testo contenga una parola presente nella lista nera.
     *MODIFIES: posts
     */
	void filterPosts();	
	
    /*REQUIRES: true
     *THROWS:
     *EFFECTS: censura un post contente una parola presente nella lista nera, sostituendola 
     *			con un asterisco.
     *MODIFIES: posts
     */
	void censorPosts();
}
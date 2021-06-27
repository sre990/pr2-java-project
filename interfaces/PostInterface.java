package interfaces;

import java.sql.Timestamp;

public interface PostInterface {
    
    /*REQUIRES: true 
     *THROWS:
     *EFFECTS: restituisce l'identificatore univoco del post
     *MODIFIES:
     */
    int getId();
    
    /*REQUIRES: true
     *THROWS:
     *EFFECTS: restituisce l'utente del SocialNetwork che ha scritto il post
     *MODIFIES:
     */
    String getAuthor();
    
    /*REQUIRES: true
     *THROWS: 
     *EFFECTS: restituisce il testo del post
     *MODIFIES: 
     */
    String getText(); 
    
    /*REQUIRES: true
     *THROWS:
     *EFFECTS: restituisce la data e l'ora in cui e' stato scritto il post
     *MODIFIES: 
     */
    Timestamp getTimestamp();
    
    /*REQUIRES: true
     *THROWS:
     *EFFECTS: restituisce il post in formato leggibile
     *MODIFIES:
     */
    String toString();
	
}
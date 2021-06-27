import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import app.ModeratedSocial;
import app.Post;
import app.SocialNetwork;
import exceptions.*;

public class TestBattery {
	public static void main(String[] args) {
		final long startTime = System.currentTimeMillis();
		SocialNetwork microBlog = new SocialNetwork();
		ModeratedSocial moderatedMicroBlog = new ModeratedSocial();
		List <Post> ps = new ArrayList<>();

        System.out.println("START OF TESTING\n");
        System.out.print("WARNING: this class simulates an execution of ");
		System.out.println("MicroBlog, where all exceptions are handled and the worst possible outcome is expected.");
		System.out.print("Please, see the Main class for an ideal execution and a showcase of all of MicroBlog's features.\n");

        System.out.println("\nTesting the Post class:");
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Test con campo null");
		try {
			new Post(1, null, "ciao");
		} catch (IllegalArgumentException | TextFormatException e) {
			e.printStackTrace();
		}
		try {
			new Post(2, "Claudio", null);
		} catch (IllegalArgumentException | TextFormatException e) {
			e.printStackTrace();
		}
		System.out.println("Test con username di lunghezza < 1");
		try {
			new Post(3, "Claudio", "");
		} catch (IllegalArgumentException | TextFormatException e) {
			e.printStackTrace();
		}
		System.out.println("Test con username di lunghezza > 140");
		try {
			new Post(4, "Claudio", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		} catch (IllegalArgumentException | TextFormatException e) {
			e.printStackTrace();
		}

		
//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the SocialNetwork class:");
        System.out.println("Testing the guessFollowers(ps) method:");
        System.out.println("Test con campo null");
        try {
			microBlog.guessFollowers(null);
        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException | PermissionDeniedException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test con elemento della lista in input null");
        ps.add(null);
        try {
			microBlog.guessFollowers(ps);
        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException | PermissionDeniedException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test di permission denied per la follow");
    	try {
    		ps = new ArrayList<>();
        	ps.add(microBlog.createPost(1, "GUEST", "follow:Luca"));
			microBlog.guessFollowers(ps);
        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException 
        		| PermissionDeniedException | DuplicateException e ) {
        	e.printStackTrace();
        }
    	System.out.println("Test di permission denied per il like");
    	try {
    		ps = new ArrayList<>();
        	ps.add(microBlog.createPost(2, "GUEST", "like:324"));
			microBlog.guessFollowers(ps);
        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException 
        		| PermissionDeniedException | DuplicateException e ) {
        	e.printStackTrace();
        }	
    	System.out.println("Test follow a persona non iscritta");
    	try {
        	microBlog.createUser("Marco");
    		ps = new ArrayList<>();
            ps.add(microBlog.createPost(3, "Marco", "follow:sdgfsdggdg"));
			microBlog.guessFollowers(ps);
    	} catch (IllegalArgumentException | NullPointerException | NoSuchElementException 
        		| PermissionDeniedException | DuplicateException | TextFormatException e ) {
        	e.printStackTrace();
    	}
    	System.out.println("Test per like a post inesistente");
    	try {	
    		ps = new ArrayList<>();
            ps.add(microBlog.createPost(4, "Marco", "like:2342342"));
			microBlog.guessFollowers(ps);
    	} catch (IllegalArgumentException | NullPointerException | NoSuchElementException 
        		| PermissionDeniedException | DuplicateException e ) {
        	e.printStackTrace();
    	}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the getMentionedUsers(ps) method:");
        System.out.println("Test con campo null");
		try {
        	microBlog.getMentionedUsers(null);
		} catch (IllegalArgumentException | NullPointerException | NoSuchElementException e) {
			e.printStackTrace();
		}
        System.out.println("Test con elemento della lista in input null");
		try {
        	ps.add(null);
        	microBlog.getMentionedUsers(ps);
		} catch (IllegalArgumentException | NullPointerException | NoSuchElementException e) {
			e.printStackTrace();
		}
		System.out.println("Test per menzione a utente non registrato");
		try {
			ps = new ArrayList<>();
			ps.add(microBlog.createPost(5, "Marco", "bene, tu @dfasdfdfa?"));
			microBlog.getMentionedUsers(ps);
		} catch (IllegalArgumentException | NullPointerException | NoSuchElementException | DuplicateException e) {
			e.printStackTrace();
		}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the writtenBy(username) method:");
        System.out.println("Test con campo null");   
		try {
			microBlog.writtenBy(null);
		} catch (IllegalArgumentException | NoSuchElementException e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente non registrato");   
		try {
        	microBlog.writtenBy("Gianmarco");
		} catch (IllegalArgumentException | NoSuchElementException e) {
			e.printStackTrace();
		}     	
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the writtenBy(ps, username) method:");
        System.out.println("Test con campo null");   
		try {
        	microBlog.writtenBy(null, "Luca");
		} catch (IllegalArgumentException | NullPointerException | NoSuchElementException e) {
			e.printStackTrace();
		} 
		try {
        	ps.add(microBlog.createPost(23, "Marco", "follow:Luca"));
        	microBlog.writtenBy(ps, null);
		} catch (IllegalArgumentException | NullPointerException | NoSuchElementException | DuplicateException e) {
			e.printStackTrace();
		} 
        System.out.println("Test con elemento della lista in input null");   
        try {
	       	microBlog.createUser("Mario");
	       	ps.add(null);
	       	microBlog.writtenBy(ps, "Mario");
		} catch (IllegalArgumentException | NullPointerException | NoSuchElementException 
				| DuplicateException | TextFormatException e) {
			e.printStackTrace();
		} 
		//ritardo di 10ms per rendere l'output piu' leggibile 
        System.out.println("Test con utente non registrato");   
		try {
        	ps.add(microBlog.createPost(343, "Michele", "follow:Luca"));
        	microBlog.writtenBy(ps, "Francesco");
		} catch (IllegalArgumentException | NullPointerException | NoSuchElementException | DuplicateException e) {
			e.printStackTrace();
		} 

		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the containing(words) method:");
        System.out.println("Test con campo null");
        try {     
        	microBlog.containing(null);
		} catch (IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
		} 
        System.out.println("Test con elemento della lista in input null");
        try {     
        	List<String> ws = new ArrayList<>();
        	ws.add(null);
        	microBlog.containing(ws);
		} catch (IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
		}        
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}        
		System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the createPost() method:");
        System.out.println("Test con campo null");
		try {     
			microBlog.createPost(45, null, "testo");
		} catch(IllegalArgumentException | DuplicateException e) {
			e.printStackTrace();
		}
		try {     
			microBlog.createPost(34, "GUEST", null);
		} catch(IllegalArgumentException | DuplicateException e) {
			e.printStackTrace();
		}
        System.out.println("Test con ID gia' utilizzato");
		try {     
			microBlog.createPost(0, "Marco", "testo");
			microBlog.createPost(0, "GUEST", "testo");
		} catch(IllegalArgumentException | DuplicateException e) {
			e.printStackTrace();
		}   
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the deletePost() method(SocialNetwork version):");
        System.out.println("Test con campo null");
		try {     
			microBlog.deletePost(null, 0);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException e) {
			e.printStackTrace();
		} 
        System.out.println("Test di permission denied per un guest");
		try {     
			microBlog.deletePost("GUEST", 0);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException e) {
			e.printStackTrace();
		} 
        System.out.println("Test di permission denied per un utente che cerca di cancellare il post di un altro");
		try {    
        	microBlog.createUser("Nicola");
			microBlog.createUser("Giorgio");
			microBlog.createPost(88, "Giorgio", "ciao");		
			microBlog.deletePost("Nicola", 88);
		} catch(IllegalArgumentException | NoSuchElementException | 
				PermissionDeniedException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		} 
        System.out.println("Test nel caso in cui l'utente provi a cancellare un post inesistente");
		try {    
			microBlog.deletePost("Nicola", 6365435);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException e) {
			e.printStackTrace();
		}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the createUser() method:");
        System.out.println("Test con campo null");
		try {    
			microBlog.createUser(null);
		} catch(IllegalArgumentException | NoSuchElementException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
        System.out.println("Test con caratteri proibiti");
		try {    
			microBlog.createUser("?????");
		} catch(IllegalArgumentException | NoSuchElementException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
        System.out.println("Test con lunghezza username < 1");
		try {    
			microBlog.createUser("");
		} catch(IllegalArgumentException | NoSuchElementException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
        System.out.println("Test con lunghezza username > 140");
		try {    
			microBlog.createUser("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		} catch(IllegalArgumentException | NoSuchElementException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}        
		System.out.println("Test con utente gia' iscritto");
		try {    
			microBlog.createUser("Paolo");
			microBlog.createUser("Paolo");
		} catch(IllegalArgumentException | NoSuchElementException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}  
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the deleteUser() method(SocialNetwork version):");
        System.out.println("Test con campo null");
		try {    
			microBlog.deleteUser(null, "Giacomo");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
		try {    
			microBlog.deleteUser("Giacomo", null);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente non registrato");
		try {    
			microBlog.createUser("Giacomo");
			microBlog.deleteUser("Giacomo", "Tiziano");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
		try {    
			microBlog.createUser("Tiziano");
			microBlog.deleteUser("Elena", "Tiziano");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente che prova a eliminare l'account di un altro");
		try {    
			microBlog.deleteUser("Giacomo", "Tiziano");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException | ForbiddenActionException e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente che prova a eliminare l'account GUEST e viceversa");
		try {    
			microBlog.deleteUser("Giacomo", "GUEST");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
		try {    
			microBlog.deleteUser("GUEST", "Giacomo");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}  
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the getFollowers(username) method:");
        System.out.println("Test con campo null");
        try {
        	microBlog.getFollowers(null);
        } catch(IllegalArgumentException | NoSuchElementException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test con utente non registrato nella rete sociale");
        try {
        	microBlog.getFollowers("Ettore");
        } catch(IllegalArgumentException | NoSuchElementException e ) {
        	e.printStackTrace();
        }
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the getFollowing(username) method:");
        System.out.println("Test con campo null");
        try {
        	microBlog.getFollowing(null);
        } catch(IllegalArgumentException | NoSuchElementException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test con utente non registrato nella rete sociale");
        try {
        	microBlog.getFollowing("Raffaele");
        } catch(IllegalArgumentException | NoSuchElementException e ) {
        	e.printStackTrace();
        }
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
        System.out.println("-----------------------------------------------------------");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the ModeratedSocial class:");
        System.out.println("Testing the addModerator(username) method:");
        System.out.println("Test con campo null");
		try {    
			moderatedMicroBlog.addModerator(null);
		} catch(IllegalArgumentException | NoSuchElementException | 
				ForbiddenActionException | PermissionDeniedException e) {
			e.printStackTrace();
		}
		System.out.println("Test con utente non presente nella rete sociale");
		try {    
			moderatedMicroBlog.addModerator("Marco");
		} catch(IllegalArgumentException | NoSuchElementException | 
				ForbiddenActionException | PermissionDeniedException e) {
			e.printStackTrace();
		}
		System.out.println("Test con utente che viene reso moderatore due volte");
		try {    
			moderatedMicroBlog.createUser("Marco");
			moderatedMicroBlog.addModerator("Marco");
			moderatedMicroBlog.addModerator("Marco");
		} catch(IllegalArgumentException | NoSuchElementException | 
				ForbiddenActionException | PermissionDeniedException | 
				TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
		System.out.println("Test con un guest che viene reso moderatore");
		try {
			moderatedMicroBlog.createUser("GUEST");
		} catch (IllegalArgumentException | TextFormatException | DuplicateException e1) {
			e1.printStackTrace();
		}
		try {    
			moderatedMicroBlog.addModerator("GUEST");
		} catch(IllegalArgumentException | NoSuchElementException | 
				ForbiddenActionException | PermissionDeniedException e) {
			e.printStackTrace();
		}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the removeModerator(username) method:");
        System.out.println("Test con campo null");
		try {    
			moderatedMicroBlog.removeModerator(null);
		} catch(IllegalArgumentException | NoSuchElementException  e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente che non e' un moderatore");
		try {    
			moderatedMicroBlog.removeModerator("GUEST");
		} catch(IllegalArgumentException | NoSuchElementException  e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente che non e' iscritto alla rete sociale");
		try {    
			moderatedMicroBlog.removeModerator("Massimo");
		} catch(IllegalArgumentException | NoSuchElementException  e) {
			e.printStackTrace();
		}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}  
        System.out.println("-----------------------------------------------------------");
		System.out.println("Testing the reportPost(id, reporter) method:");
        System.out.println("Test con campo null");
		try {    
			moderatedMicroBlog.createUser("Enrico");
			moderatedMicroBlog.createPost(0, "Enrico", "buongiorno");
			moderatedMicroBlog.reportPost(0, null);
		} catch(IllegalArgumentException | NoSuchElementException | 
				TextFormatException | DuplicateException | PermissionDeniedException | 
				ForbiddenActionException  e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente non registrato che prova a segnalare un post");
		try {    
			moderatedMicroBlog.reportPost(0, "GUEST");
		} catch(IllegalArgumentException | NoSuchElementException | 
				PermissionDeniedException | ForbiddenActionException  e) {
			e.printStackTrace();
		}
		try {    
			moderatedMicroBlog.reportPost(0, "Luigi");
		} catch(IllegalArgumentException | NoSuchElementException | 
				PermissionDeniedException | ForbiddenActionException  e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente che prova a segnalare due volte un post");
		try {    
			moderatedMicroBlog.reportPost(0, "Enrico");
			moderatedMicroBlog.reportPost(0, "Enrico");
		} catch(IllegalArgumentException | NoSuchElementException | 
				PermissionDeniedException | ForbiddenActionException  e) {
			e.printStackTrace();
		}
        System.out.println("Test con utente che prova a segnalare un post che non esiste");
		try {    
			moderatedMicroBlog.reportPost(65, "Enrico");
		} catch(IllegalArgumentException | NoSuchElementException | 
				PermissionDeniedException | ForbiddenActionException  e) {
			e.printStackTrace();
		}
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}  
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the addToReported(ps) method:");
        System.out.println("Test con campo null");
        try {
        	moderatedMicroBlog.addToReported(null);
        } catch(IllegalArgumentException e) {
        	e.printStackTrace();
        }
        System.out.println("Test con elemento della lista in input null");
        try {
        	ps.add(null);
        	moderatedMicroBlog.addToReported(ps);
        } catch(IllegalArgumentException | NullPointerException e) {
        	e.printStackTrace();
        }
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the removeFromReported(id) method:");
        System.out.println("Test con post non presente nella lista dei post segnalati");
        try {
        	moderatedMicroBlog.removeFromReported(88);
        } catch(NoSuchElementException e) {
        	e.printStackTrace();
        }
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the addToBlackList(word) method:");
        System.out.println("Test con campo null");
        try {
        	moderatedMicroBlog.addToBlackList(null);
        } catch (IllegalArgumentException | TextFormatException | ForbiddenActionException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test con stringa in input < 3");
        try {
        	moderatedMicroBlog.addToBlackList("aa");
        } catch (IllegalArgumentException | TextFormatException | ForbiddenActionException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test con stringa in input > 140");
        try {
        	moderatedMicroBlog.addToBlackList("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        } catch (IllegalArgumentException | TextFormatException | ForbiddenActionException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test quando si prova a inserire due volte una parola nella blacklist");
        try {
        	moderatedMicroBlog.addToBlackList("aaaa");
        	moderatedMicroBlog.addToBlackList("aaaa");
        } catch (IllegalArgumentException | TextFormatException | ForbiddenActionException e ) {
        	e.printStackTrace();
        }
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the removeFromBlackList(word) method:");
        System.out.println("Test con campo null");
        try {
        	moderatedMicroBlog.removeFromBlackList(null);
        } catch (IllegalArgumentException | NoSuchElementException e ) {
        	e.printStackTrace();
        }
        System.out.println("Test con parola non presente nella lista delle parole proibite");
        try {
        	moderatedMicroBlog.removeFromBlackList("ciao");
        } catch (IllegalArgumentException | NoSuchElementException e ) {
        	e.printStackTrace();
        }
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the deleteUser() method(ModeratedSocial version):");
        System.out.println("Test con campo null");
        try {    
        	moderatedMicroBlog.createUser("Giacomo");
        	moderatedMicroBlog.deleteUser(null, "Giacomo");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
		try {    
			moderatedMicroBlog.deleteUser("Giacomo", null);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
		System.out.println("Test con utente non registrato");
		try {    
			moderatedMicroBlog.deleteUser("Giacomo", "Tiziano");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
		try {    
			moderatedMicroBlog.createUser("Tiziano");
			moderatedMicroBlog.deleteUser("Elena", "Tiziano");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException | TextFormatException | DuplicateException e) {
			e.printStackTrace();
		}
		System.out.println("Test quando si prova ad eliminare l'account GUEST");
		try {    
			moderatedMicroBlog.deleteUser("Giacomo", "GUEST");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
		System.out.println("Test un non-moderatore prova a eliminare l'account di un altro");
		try {    
			moderatedMicroBlog.deleteUser("Giacomo", "Tiziano");
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException |
				ForbiddenActionException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
        System.out.println("-----------------------------------------------------------");
        System.out.println("Testing the deletePost() method(ModeratedSocial version):");
        System.out.println("Test con campo null");
        try {     
        	moderatedMicroBlog.deletePost(null, 0);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException e) {
			e.printStackTrace();
		} 
        System.out.println("Test di permission denied per un guest");
		try {     
			moderatedMicroBlog.deletePost("GUEST", 0);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException e) {
			e.printStackTrace();
		} 
        System.out.println("Test di permission denied per un non-moderatore che prova a cancellare il post di un altro");
		try {     
			moderatedMicroBlog.deletePost("Giacomo", 0);
		} catch(IllegalArgumentException | NoSuchElementException | PermissionDeniedException e) {
			e.printStackTrace();
		} 
        System.out.println("Test con post non presente nella lista dei post della rete sociale");
		try {  
			moderatedMicroBlog.addModerator("Giacomo");
			moderatedMicroBlog.deletePost("Giacomo", 1000);
		} catch(IllegalArgumentException | NoSuchElementException
				| PermissionDeniedException | ForbiddenActionException e) {
			e.printStackTrace();
		} 
		//ritardo di 10ms per rendere l'output piu' leggibile 	
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
        System.out.println("-----------------------------------------------------------");
        System.out.println("-----------------------------------------------------------");

		final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
	}

}
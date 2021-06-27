import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import app.ModeratedSocial;
import app.Post;
import app.SocialNetwork;
import exceptions.*;

public class Main {
	public static void main(String[] args) 
			throws IllegalArgumentException, TextFormatException, 
			DuplicateException, NoSuchElementException, InterruptedException, 
			NullPointerException, PermissionDeniedException, ForbiddenActionException {
		final long startTime = System.currentTimeMillis();
		
        System.out.println("START OF TESTING\n");
        
        System.out.print("WARNING: this class simulates an ideal execution of ");
		System.out.println("MicroBlog, no exception is raised and no errors are expected.");
		System.out.print("Please, see the TestBattery class for errors and ");
		System.out.println("exception handling.\n\n");
		
		ModeratedSocial social = new ModeratedSocial();
		social.createUser(SocialNetwork.GUEST);
		//lista di post 
		List <Post> ps = new ArrayList<>();
		//lista di parole da ricercare
		List<String> words = new ArrayList<String>();

        social.createUser("Stefano");
        social.createUser("Luca");

        ps.add(social.createPost(1, "Stefano", "ehilà, ciao @Luca, come stai?"));
        ps.add(social.createPost(2, "Luca", "bene, tu @Stefano?"));
		ps.add(social.createPost(3, "Giacomo", "allora?! Rispondi!"));
		ps.add(social.createPost(4, "Luca", "follow:Stefano"));
		ps.add(social.createPost(5, "Stefano", "LiKe:2"));
//						Testing of requested methods						//
        System.out.println("Microblog posts up until now:");
		for (Post p : ps) System.out.println((p==null) ? "" : p.toString());
		System.out.println("----------");
        System.out.println("Testing of guessFollowers(ps).");
		System.out.println(social.guessFollowers(ps));
        social.createUser("Gino");
        social.createUser("Beppe");
        social.createUser("Enrico");
        social.createUser("Massimo");
        social.createUser("Alberto");
        social.createUser("Franco");
        social.createUser("Mario");
        social.createUser("Giorgio");
        social.createUser("Emiliano");
        social.startFollowing("Massimo", "Giorgio");	
		social.startFollowing("Massimo", "Enrico");
		social.startFollowing("Giorgio", "Enrico");
		social.startFollowing("Emiliano", "Enrico");
		social.startFollowing("Enrico", "Beppe");
		social.startFollowing("Beppe", "Giorgio");
        social.startFollowing("Massimo", "Alberto");
		social.startFollowing("Beppe", "Alberto");
		System.out.println("----------");
		System.out.println("Testing guessFollowers(ps) again:");
		System.out.println(social.guessFollowers(ps));
		System.out.println("----------");
        System.out.println("Testing of influencers().");
		System.out.println(social.influencers());
		System.out.println("----------");
        System.out.println("Testing of getMentionedUsers().");
		System.out.println(social.getMentionedUsers());
		System.out.println("----------");
        System.out.println("Testing of getMentionedUsers(ps).");
		System.out.println(social.getMentionedUsers(ps));
        social.createPost(6, "Alberto", "questo e' il sesto post di MicroBlog");
        social.createPost(7, "Enrico", "seguitemi tutti su MicroBlog");
        social.createPost(8, "Alberto", "ecco un altro post su Microblog!");
        social.createPost(9, "Alberto", "e un altro ancora!");
		System.out.println("----------");
		social.createPost(10, "...", "Ciao a tutti!");
        System.out.println("Testing of writtenBy(username).");
        System.out.println(social.writtenBy("GUEST"));
        ps.add(social.createPost(33, "Luca", "allora?! Rispondi!"));
		System.out.println("----------");
        System.out.println("Testing of writtenBy(ps, username).");
        System.out.println(social.writtenBy(ps, "Luca"));
        words.add("microblog");
        words.add("altro");
		System.out.println("----------");
        System.out.println("Testing of containing(words).");		
        System.out.println(social.containing(words));
        
//						Testing of additional methods						//
		System.out.println("\n\n(start of additional methods testing)\n");
		System.out.println("Testing of printPosts():");
        social.printPosts();
		social.deletePost("Alberto", 8);
		System.out.println("After a post is deleted by its author:");
		social.printPosts();
		social.addModerator("Alberto");
		social.deletePost("Alberto", 33);
		System.out.println("After a mod deletes a post:");
		social.printPosts();
		System.out.println("Testing of printAuthorsAndFollowers().");
		social.printAuthorsAndFollowers();
		System.out.println("Testing of printUsersAndFollowing().");
		social.printUsersAndFollowing();
		System.out.println("Testing of getFollowing(username).");
		System.out.println(social.getFollowing("Massimo"));
		System.out.println("Testing of getFollowers(username).");
		System.out.println(social.getFollowers("Enrico"));
		System.out.println("Before a defollow:");
		System.out.println(social.getFollowers("Giorgio"));
		social.stopFollowing("Massimo", "Giorgio");
		System.out.println("After a defollow:");
		System.out.println(social.getFollowers("Giorgio"));
		social.stopFollowing("Beppe", "Giorgio");
		System.out.println("After another defollow:");
		System.out.println(social.getFollowers("Giorgio"));
		System.out.println("Before an user deletes their own account:");
		System.out.println(social.getFollowing("Massimo"));
		System.out.println(social.getMentionedUsers());
		social.deleteUser("Enrico", "Enrico");
		System.out.println("After an user deletes their own account:");
		System.out.println(social.getFollowing("Massimo"));
		System.out.println(social.getMentionedUsers());
		System.out.println("Before an user is banned by a mod:");
		System.out.println(social.getFollowing("Luca"));
		System.out.println(social.getMentionedUsers());
		social.deleteUser("Alberto", "Stefano");
		System.out.println("After an user is banned by a mod:");
		System.out.println(social.getFollowing("Luca"));
		System.out.println(social.getMentionedUsers());
		System.out.println("----------");
		System.out.println("After having censored some posts:");
		social.addToBlackList("ciao");
		social.censorPosts();
		social.printPosts();
		System.out.println("After having filtered out some posts:");
		social.addToBlackList("microblog");
		social.filterPosts();
		social.printPosts();
		System.out.println("After having added and then deleted a word from the blacklist:");
		social.addToBlackList("altro");
		System.out.println(social.getBlacklist());
		social.removeFromBlackList("altro");
		System.out.println(social.getBlacklist());
		System.out.println("----------");
		social.addModerator("Luca");
		social.addModerator("Gino");
		System.out.println("After a mod deletes a post:");
		social.deletePost("Gino", 10);
		social.printPosts();
		System.out.println("After having removed a mod:");
		System.out.println(social.getMods());
		social.removeModerator("Gino");
		System.out.println(social.getMods());
		System.out.println("----------");
		System.out.println("After having reported two posts:");
		ps.add(social.createPost(22, "Luca", "report: 9"));
		social.addToReported(ps);
		social.reportPost(3, "Gino");
		social.printReported();
		System.out.println("After having removed a post from the reported list:");
		social.removeFromReported(9);
		social.printReported();
		System.out.println("----------");



		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime));
	}
}
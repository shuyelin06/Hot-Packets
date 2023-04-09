package core;

import java.util.ArrayList;
import java.util.Arrays;

public class DemoQuestions {
	

	public static final ArrayList<String> bitcamp = new ArrayList<String>(
		Arrays.asList
		(
		"bitcamp is a fun event",
		"bitcamp is a place for exploration",
		"bitcamp is from April 7-9",
		"bitcamp has a 5 tracks",
		"bitcamp has a colorwar and a design den",
		"bitcamp has 3 color teams",
		"bitcamp offered chickfila",
		"bitcap has workshops",
		"bitcamp has icecream",
		"bitcamp is fun",
		"bitcamp volunteers are awesome",
		"bitcamp is a fun experience",
		"bitcamp is exciting",
		"bitcamp is at Xfinity Center",
		"bitcamp is awesome",
		"WE LOVE GITHUB"
		));
	public static final ArrayList<String> gitHub = new ArrayList<String>(
		Arrays.asList
		(
		"GitHub is awesome",
		"you can git alot done with GitHub",
		"We used GitHub for our project",
		"GitHub is a hosting service",
		"GitHub allows distributed version control",
		"GitHub is revolutionary",
		"GitHub is good for collaboration",
		"GitHub has over 100M developers",
		"GitHub makes sharing convenient",
		"GitHub has a nice branching format",
		"GitHub was founded in 2008",
		"GitHub HQ is at San Francisco",
		"GitHub is easy to use",
		"GitHub is easy to learn",
		"WE LOVE GITHUB"
		));
	public static final ArrayList<String> CapitalOne = new ArrayList<String>(
		Arrays.asList
		(
		"Capital One was founded in 1994",
		"Capital One HQ is at McLean, VA",
		"Capital One helps find the right credit cards",
		"Capital One specializes in credit cards",
		"Capital One is an American Bank",
		"Capital One is among the 10 U.S. banks by assets",
		"Customers can access their account online",
		"A lot of people use Capital One",
		"Capital One brings ingenuity, simplicity, and humanity to banking",
		"Capital One customer service is awesome",
		"Capital One is nice",
		"Capital One's website is clean",
		"Capital One is awesome",
		"Capital One for everyone",
		"WE LOVE CAPITAL ONE"
		));
	public static final ArrayList<String> CipherTech = new ArrayList<String>(
		Arrays.asList
		(
		"Cipher Tech has a nice logo",
		"Cipher Tech was founded in 2006",
		"Cipher Tech workers are nice",
		"Cipher Tech is great",
		"Cipher Tech is located in Virginia",
		"Cipher Tech is a defense and intelligence agency",
		"Cipher Tech was founded by Northeastern students",
		"One of Cipher Tech mission is digital forensics",
		"Cipher Tech has a nice website",
		"Cipher Tech has the best scientists",
		"Another Cipher Tech mission is maleware reverse engineering",
		"Cipher Tech is awesome",
		"Cipher Tech is nice",
		"Cipher Tech provides computer programming services",
		"WE LOVE CIPHER TECHNOLOGY SOLUTIONS"
		));
	
	public static final ArrayList<String> Money = new ArrayList<String>(
			Arrays.asList("$$$"));
			

	
	public final static ArrayList<String> array = Money;
	
	public static String randomString() { 
		if ( array != null ) {
			return array.get((int) (Math.random() * array.size()));
		}
		return null;
	}

}

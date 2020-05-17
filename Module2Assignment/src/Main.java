/**
 * 
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;
import java.util.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
 
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

/**
 * @author NickS
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		
		URL oracle = new URL("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
		BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
		
		RegexMatcher regexmatcher = new RegexMatcher();
		Pattern firstLineOfPoem = Pattern.compile(regexmatcher.PoemBegin);
		Pattern lastLineOfPoem = Pattern.compile(regexmatcher.PoemEnd);
		Pattern BRTagsRegex = Pattern.compile(regexmatcher.BRTags);
		Pattern ParagraphTagRegex = Pattern.compile(regexmatcher.ParagraphTags);
		Pattern ItalicsRegex = Pattern.compile(regexmatcher.Italics);
		Pattern ItalicsAfterRegex = Pattern.compile(regexmatcher.ItalicsAfter);
		Pattern EndingDashRegex = Pattern.compile(regexmatcher.MDashAfter);
		String inputLine;
		
		Map<String, Integer> counterMap = new HashMap<>();
		
		boolean doWordAnalysis = false;
		//int poemBody = 0;
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> processedWords = new ArrayList<String>();
		//String[] line;
		//String[] subString;
		
		while ((inputLine = in.readLine()) != null) 
		{
			//System.out.println(inputLine);
			Matcher poemStart = firstLineOfPoem.matcher(inputLine);
			Matcher poemEnd = lastLineOfPoem.matcher(inputLine);
			if(poemStart.find()) 
			{
				// Set bool flag
				//System.out.println("###########################");
				doWordAnalysis = true;
				//System.out.println(inputLine);
				// Split line on spaces and add to a string array
				String[] wordsInLine = inputLine.split(" ");
				for (String i : wordsInLine) 
				{
					String withoutspace = i.replaceAll("\\s", "");
					if (i.contentEquals("")) 
					{
						// Don't add
					}
					else 
					{
						words.add(i);
					}
				}
			}
			else if(doWordAnalysis && poemEnd.find()) 
			{
				// Set bool flag
				doWordAnalysis = false;
				String[] wordsInLine = inputLine.split(" ");
				for (String i : wordsInLine) 
				{
					String withoutspace = i.replaceAll("\\s", "");
					if (i.contentEquals("")) 
					{
						// Don't add
					}
					else 
					{
						words.add(i);
					}
				}				
			}
			else if (doWordAnalysis && !poemEnd.find()) 
			{
				String[] wordsInLine = inputLine.split(" ");
				for (String i : wordsInLine) 
				{
					String withoutspace = i.replaceAll("\\s", "");
					if (i.contentEquals("")) 
					{
						// Don't add
					}
					else 
					{
						words.add(i);
					}
				}
			}
		}
		
		ArrayList<String> deletedWords = new ArrayList<String>();
		
		for (String i : words) 
		{
			if (i.contains("lifted")) 
			{
				int bobDole = 1;
			}
			if (!i.contains("margin-left:") /*&& !i.contains("-")*/ && !i.contains("%") && !i.contains("<SPAN") && !i.contains("CLASS")) 
			{
				// Desired words
				Matcher ParagraphTag = ParagraphTagRegex.matcher(i);
				Matcher BRTag = BRTagsRegex.matcher(i);
				Matcher ItalicsTags = ItalicsRegex.matcher(i);
				Matcher ItalicsAfterTags = ItalicsAfterRegex.matcher(i);
				Matcher EndingDash = EndingDashRegex.matcher(i);

				if (BRTag.find()) 
				{
					String str = i.replaceAll(regexmatcher.BRTags, "$1");
					Matcher EndingDashSubString = EndingDashRegex.matcher(str);
					if(EndingDashSubString.find()) 
					{
						String substr = str.replaceAll(regexmatcher.MDashAfter, "$1");
						processedWords.add(substr);
					}
					else 
					{
						processedWords.add(str);
					}
				}
				else if(ParagraphTag.find()) 
				{
					
					// Don't add paragraph tags
					deletedWords.add(i);
				}
				else if(ItalicsTags.find()) 
				{
					String str = i.replaceAll(regexmatcher.Italics, "$1");
					processedWords.add(str);
				}
				else if(ItalicsAfterTags.find()) 
				{
					String str = i.replaceAll(regexmatcher.ItalicsAfter, "$1$2");
					processedWords.add(str);
				}
				else if(EndingDash.find()) 
				{
					String str = i.replaceAll(regexmatcher.MDashAfter, "$1");
					processedWords.add(str);
				}
				else 
				{
					processedWords.add(i);
				}

			}
			else 
			{
				// Don't add words with undesired characters listed above
				deletedWords.add(i);
			}
		}

		ArrayList<String>postProcessedWords = new ArrayList<String>();
		for (String word : processedWords) 
		{
			if(word.contains("lifted")) 
			{
				int bobDole = 1;
			}
			String newWords[] = word.split("&mdash;");
			if (newWords.length > 0) 
			{
				for (String i : newWords) 
				{
					String str = i.replaceAll("(.*?)</SPAN>", "$1");
					postProcessedWords.add(str);
				}
			}
			else
			{
				postProcessedWords.add(word);
			}
		}
		
		for(String word : postProcessedWords)
		{
			if (word.contains("Doubtless"))
			{
				int bobDole = 1;
			}
			String punctuationSuffix = word.replaceAll("^(.*?)\\p{Punct}+$", "$1");
			String punctuationPrefix = punctuationSuffix.replaceAll("^\\p{Punct}+(.*?)$", "$1");
			
			//If a key doesn't exist in a hashmap, `get(T)` returns null
			if(counterMap.get(punctuationPrefix) == null) 
			{
			    //We know this key doesn't exist, so let's create a new entry with 1 as the count
			    counterMap.put(punctuationPrefix, 1);
			} 
			else 
			{
			    //We know this key exists, so let's get the old count, increment it, and then update
			    //the value
			    int count = counterMap.get(punctuationPrefix);
			    counterMap.put(punctuationPrefix, count + 1);
			}
			
		}
		
//		HashMap<String, Integer> hm = new HashMap<String, Integer>(); 
//		  
//        // enter data into hashmap 
//        hm.put("Math", 98); 
//        hm.put("Data Structure", 85); 
//        hm.put("Database", 91); 
//        hm.put("Java", 95); 
//        hm.put("Operating System", 79); 
//        hm.put("Networking", 80); 
//        Map<String, Integer> hm1 = HashMapSorter.sortByValue(hm); 
//  
//        // print the sorted hashmap 
//        for (Map.Entry<String, Integer> en : hm1.entrySet()) 
//        { 
//            //System.out.println("Key = " + en.getKey() +  ", Value = " + en.getValue()); 
//        } 
//		
//		counterMap.entrySet().forEach(entry->
//		{
//		    //System.out.println(entry.getKey() + " " + entry.getValue());  
//		 });
//		
//		//System.out.println("*************************");
		
//		for (String word : deletedWords) 
//		{
//			//System.out.println(word);
//		}
		
		in.close();
		
		// a Map with string keys and integer values
	    Map<String, Integer> budget = new HashMap<>();
	    budget.put("clothes", 120);
	    budget.put("grocery", 150);
	    budget.put("transportation", 100);
	    budget.put("utility", 130);
	    budget.put("rent", 1150);
	    budget.put("miscellneous", 90);
	 
	    // let's sort this map by values first
//	    Map<String, Integer> sorted = counterMap
//	        .entrySet()
//	        .stream()
//	        .sorted(comparingByValue())
//	        .collect(
//	            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
//	                LinkedHashMap::new));
	 
	    //System.out.println("map after sorting by values: " + sorted);
	    
	    // above code can be cleaned a bit by using method reference
//	    Map<String, Integer> sorted = counterMap
//	        .entrySet()
//	        .stream()
//	        .sorted(comparingByValue())
//	        .collect(
//	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
//	                LinkedHashMap::new));
	 
	    // Sort the map in decreasing order of value
	    Map<String, Integer>sorted = counterMap
	        .entrySet()
	        .stream()
	        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	        .collect(
	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                LinkedHashMap::new));
	 	    
	    // Print out the key and values from the hash map
		sorted.entrySet().forEach(entry->
		{
		    System.out.println(entry.getKey() + " " + entry.getValue());  
		 });
	}

}

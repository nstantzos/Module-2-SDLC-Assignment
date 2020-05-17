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
//        Pattern p = Pattern.compile("\\p{Punct}");
//
//        Matcher mm = p.matcher("One day! when, I; was <walking. \"'I found your pants? just kidding...");
//        int count = 0;
//        while (mm.find()) 
//        {
//            count++;
//            System.out.println("\nMatch number: " + count);
//            System.out.println("start() : " + mm.start());
//            System.out.println("end()   : " + mm.end());
//            System.out.println("group() : " + mm.group());
//        }
//		
//		
//		
//		// String to be scanned to find the pattern.
//	      String line = "This order was placed for QT3000! OK?";
//	      String patternone = "(.*?)(\\d)(.*)";
//
//	      // Create a Pattern object
//	      Pattern r = Pattern.compile(patternone);
//
//	      // Now create matcher object.
//	      Matcher m = r.matcher(line);
//	      if (m.find( )) 
//	      {
//	         System.out.println("Found value: " + m.group(0));
//	         System.out.println("Found value: " + m.group(1));
//	         System.out.println("Found value: " + m.group(2));
//	         System.out.println("Found value: " + m.group(3));
//	      }
//	      else 
//	      {
//	         System.out.println("NO MATCH");
//	      }
		
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
		
		//String poemBegin = "Edgar Allan Poe";
		//String poemEnd = "End of the Project Gutenberg EBook of The Raven, by Edgar Allan Poe";
		
		Map<String, Integer> counterMap = new HashMap<>();
		 
		//assertEquals(3, counterMap.get("China").intValue());
		//assertEquals(2, counterMap.get("India").intValue());
		
		
		boolean doWordAnalysis = false;
		int poemBody = 0;
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> processedWords = new ArrayList<String>();
		String[] line;
		String[] subString;
		
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
						//System.out.println(i);
					}
				}
			}
			else if(doWordAnalysis && poemEnd.find()) 
			{
				// Set bool flag
				doWordAnalysis = false;
				//line = inputLine.split(" ");
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
						//System.out.println(i);
					}
				}
//				System.out.println(inputLine);
//				for (String word : line) 
//				{
//					if (word.contains("mdash")) 
//					{
//						subString = word.split("&mdash;");
//						for (String i : subString) 
//						{
//							words.add(i);
//						}
//					}
//					else 
//					{
//						words.add(word);
//					}
//				}
				
				// Exit loop
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
						//System.out.println(i);
					}
				}
				
//				line = inputLine.split(" &&&mdash;");
//				for (String word : line) 
//				{
//					//words.add(word);
//					//System.out.println(word);
//				}
//				
//				for(String word : words) 
//				{
//					//System.out.println(word);
//					if(word.contains("<")) 
//					{
//						// Don't count as a word
//					}
//					else 
//					{
//						
//					}
//					
//				}
//				System.out.println(inputLine);
//				counterMap.compute(inputLine, (k, v) -> v == null ? 1 : v + 1); 
			}
			
			//System.out.println(inputLine);
			
//			if (inputLine.equals(poemBegin)) 
//			{
//				// Set bool flag
//				doWordAnalysis = true;
//				int bobDole = 1;
//			}
//			if (doWordAnalysis) 
//			{
//				// do math
//				if (inputLine.equals(poemEnd)) 
//				{
//					System.out.println("End of poem found");
//				}
//			}
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
				
				//processedWords.add(i);
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
					//String str = i.replaceAll(regexmatcher.ParagraphTags, "$1");
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
			//counterMap.compute(punctuationPrefix, (k, v) -> v == null ? 1 : v + 1); 
			//System.out.println(punctuationPrefix);
			
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
		
		HashMap<String, Integer> hm = new HashMap<String, Integer>(); 
		  
        // enter data into hashmap 
        hm.put("Math", 98); 
        hm.put("Data Structure", 85); 
        hm.put("Database", 91); 
        hm.put("Java", 95); 
        hm.put("Operating System", 79); 
        hm.put("Networking", 80); 
        Map<String, Integer> hm1 = HashMapSorter.sortByValue(hm); 
  
        // print the sorted hashmap 
        for (Map.Entry<String, Integer> en : hm1.entrySet()) 
        { 
            //System.out.println("Key = " + en.getKey() +  ", Value = " + en.getValue()); 
        } 
		
		counterMap.entrySet().forEach(entry->
		{
		    //System.out.println(entry.getKey() + " " + entry.getValue());  
		 });
		
		//System.out.println("*************************");
		
		for (String word : deletedWords) 
		{
			//System.out.println(word);
		}
		
		in.close();
		// TODO Auto-generated method stub
		//System.out.println("Hello World");
		//System.out.println("Goodbye world");
		
		// a Map with string keys and integer values
	    Map<String, Integer> budget = new HashMap<>();
	    budget.put("clothes", 120);
	    budget.put("grocery", 150);
	    budget.put("transportation", 100);
	    budget.put("utility", 130);
	    budget.put("rent", 1150);
	    budget.put("miscellneous", 90);
	 
	    //System.out.println("map before sorting: " + budget);
	 
	    // let's sort this map by values first
	    Map<String, Integer> sorted = counterMap
	        .entrySet()
	        .stream()
	        .sorted(comparingByValue())
	        .collect(
	            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
	                LinkedHashMap::new));
	 
	    //System.out.println("map after sorting by values: " + sorted);
	    
	    //System.out.println("************************");
	    // above code can be cleaned a bit by using method reference
	    sorted = counterMap
	        .entrySet()
	        .stream()
	        .sorted(comparingByValue())
	        .collect(
	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                LinkedHashMap::new));
	 
	    // now let's sort the map in decreasing order of value
	    sorted = counterMap
	        .entrySet()
	        .stream()
	        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	        .collect(
	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                LinkedHashMap::new));
	 
	    //System.out.println("map after sorting by values in descending order: " + sorted);
	    
	    
		sorted.entrySet().forEach(entry->
		{
		    System.out.println(entry.getKey() + " " + entry.getValue());  
		 });
		
		
//		for (String word :counterMap.keySet()) 
//		{
//			for (int frequency : counterMap.values()) 
//			{
//				System.out.println(word + " " + frequency);
//			}
//
//		}
	

	}

}

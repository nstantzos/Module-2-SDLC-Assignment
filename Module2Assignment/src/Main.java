/**
 * Program created by Nick Stantzos, finished on May 17 2020. The purpose of this program is to read in text from a website
 * that contains the poem "The Raven" by Edgar Allan Poe and count the number of occurrences of each word, displaying the 
 * results in order of most to least frequent. The text parsing portion of this assignment was fairly tedious, as I needed
 * to filter out all extraneous HTML tags/text, formatting/font characters, and punctuation. If I had known of an HTML
 * object import package that Java has, then it would have made this much easier, as most of my regex captures are aimed at
 * eliminating special HTML text.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

import java.util.Collections;
import java.util.LinkedHashMap;
 
import static java.util.stream.Collectors.*;

/**
 * @author NickS
 *
 */
public class Main {

	public static void main(String[] args) throws Exception
	{
		// Call method to read in URL object and parse text into a dictionary/hash map
		Map<String, Integer> counterMap = ParseText();
		
		// Call method to sort the dictionary into descending frequency of word usage
	    Map<String, Integer> sorted = SortDictionary(counterMap);
	 	
	    // Print the sorted dictionary contents to the console window
	    PrintDictionaryToConsole(sorted);
	}

	// Method that sorts the dictionary
	private static Map<String, Integer> SortDictionary(Map<String, Integer> counterMap) {
		// Sort the map in decreasing order of value
	    Map<String, Integer>sorted = counterMap
	        .entrySet()
	        .stream()
	        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	        .collect(
	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                LinkedHashMap::new));
		return sorted;
	}

	// Method that prints the sorted dictionary to the console window
	private static void PrintDictionaryToConsole(Map<String, Integer> sorted) {
		// Format the console text into columns
	    System.out.printf("%-10s %-10s\n", "Word", "Number of Occurrences");
	    System.out.printf("%-10s %-10s\n", "----", "---------------------");
	    
	    // Print out the key and values from the hash map
		sorted.entrySet().forEach(entry->
		{
			System.out.printf("%-10s %-10s\n", entry.getKey(),entry.getValue());
		 });
	}

	
	// Method that reads URL object and parses text into a hash map. Lots of extraneous characters/text must be removed
	private static Map<String, Integer> ParseText() throws MalformedURLException, IOException {
		// Load URL object using specified website.
		URL oracle = new URL("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
		
		// Open stream to read text from URL object
		BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
		
		// Create new RegexMatcher object, compile stored patterns
		RegexMatcher regexmatcher = new RegexMatcher();
		Pattern firstLineOfPoem = Pattern.compile(regexmatcher.PoemBegin);
		Pattern lastLineOfPoem = Pattern.compile(regexmatcher.PoemEnd);
		Pattern BRTagsRegex = Pattern.compile(regexmatcher.BRTags);
		Pattern ParagraphTagRegex = Pattern.compile(regexmatcher.ParagraphTags);
		Pattern ItalicsRegex = Pattern.compile(regexmatcher.Italics);
		Pattern ItalicsAfterRegex = Pattern.compile(regexmatcher.ItalicsAfter);
		Pattern EndingDashRegex = Pattern.compile(regexmatcher.MDashAfter);
		
		String inputLine;
		
		// Create hash map object to store keys and values
		Map<String, Integer> counterMap = new HashMap<>();
		
		// Flag for parsing body of poem. Used to distinguish between poem body and other website text
		boolean doWordAnalysis = false;
		
		// Create array strings for text processing
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> processedWords = new ArrayList<String>();
		
		while ((inputLine = in.readLine()) != null) 
		{
			// Regex matcher objects. If found, they indicate the start and end of the poem
			Matcher poemStart = firstLineOfPoem.matcher(inputLine);
			Matcher poemEnd = lastLineOfPoem.matcher(inputLine);
			
			// If the beginning of the poem is found (defined in RegexMatcher.java class)
			if(poemStart.find()) 
			{
				// Set bool flag
				doWordAnalysis = true;
				
				// Split line on spaces and add to a string array
				String[] wordsInLine = inputLine.split(" ");
				for (String i : wordsInLine) 
				{
					// Remove excess whitespace strings
					String withoutspace = i.replaceAll("\\s", "");
					if (i.contentEquals("")) 
					{
						// Don't add split strings with no characters
					}
					else 
					{
						// Add everything else
						words.add(i);
					}
				}
			}
			// If the end of the poem is found
			else if(doWordAnalysis && poemEnd.find()) 
			{
				// Set bool flag
				doWordAnalysis = false;
				
				// Remove excess whitespace strings
				String[] wordsInLine = inputLine.split(" ");
				for (String i : wordsInLine) 
				{
					String withoutspace = i.replaceAll("\\s", "");
					if (i.contentEquals("")) 
					{
						// Don't add split strings with no characters
					}
					else 
					{
						// Add everything else
						words.add(i);
					}
				}				
			}
			// If the text processing flag is true
			else if (doWordAnalysis && !poemEnd.find()) 
			{
				String[] wordsInLine = inputLine.split(" ");
				for (String i : wordsInLine) 
				{
					// Remove excess whitespace strings
					String withoutspace = i.replaceAll("\\s", "");
					if (i.contentEquals("")) 
					{
						// Don't add split strings with no characters
					}
					else 
					{
						// Add everything else
						words.add(i);
					}
				}
			}
		}
		
		// Close the file stream
		in.close();
		
		// List of deleted words, used for error checking
		ArrayList<String> deletedWords = new ArrayList<String>();
		
		// Loop through 
		for (String i : words) 
		{
			// This weeds out text phrases/characters that are undesirable. Certain lines contain font/formatting
			// html code, and this if-statement is intended to weed those out. This would not be required if I knew
			// of a html-parser package in Java that would do this for me.
			if (!i.contains("margin-left:") && !i.contains("%") && !i.contains("<SPAN") && !i.contains("CLASS")) 
			{
				// Regex patterns for paragraph tags, line break tags, italic tags, and specially hyphenated text
				Matcher ParagraphTag = ParagraphTagRegex.matcher(i);
				Matcher BRTag = BRTagsRegex.matcher(i);
				Matcher ItalicsTags = ItalicsRegex.matcher(i);
				Matcher ItalicsAfterTags = ItalicsAfterRegex.matcher(i);
				Matcher EndingDash = EndingDashRegex.matcher(i);

				// If a line contains a line break tag
				if (BRTag.find()) 
				{
					// Find the line break text and remove it
					String str = i.replaceAll(regexmatcher.BRTags, "$1");
					
					// Also check for a specially hyphenated text block after the line break is removed
					Matcher EndingDashSubString = EndingDashRegex.matcher(str);
					if(EndingDashSubString.find()) 
					{
						// Remove the specially hyphenated text
						String substr = str.replaceAll(regexmatcher.MDashAfter, "$1");
						
						// Add cleaned up string to processed word list
						processedWords.add(substr);
					}
					else 
					{
						// If no specially hyphenated text is found after the line break tag is removed, add the string
						// to the processed word list
						processedWords.add(str);
					}
				}
				// If a paragraph tag is found
				else if(ParagraphTag.find()) 
				{
					// Don't add paragraph tags
					deletedWords.add(i);
				}
				// If an italic tag is found at the beginning and end of the string
				else if(ItalicsTags.find()) 
				{
					// Remove the italic tag
					String str = i.replaceAll(regexmatcher.Italics, "$1");
					processedWords.add(str);
				}
				// If an italic tag is found in the middle of the string, after some text
				else if(ItalicsAfterTags.find()) 
				{
					// Remove the italic tags
					String str = i.replaceAll(regexmatcher.ItalicsAfter, "$1$2");
					processedWords.add(str);
				}
				// If a special hyphen is found at the end of the text
				else if(EndingDash.find()) 
				{
					// Remove specially hyphenated text
					String str = i.replaceAll(regexmatcher.MDashAfter, "$1");
					// Note that this does not find special hyphens in the middle of the string (dealt with later)
					processedWords.add(str);
				}
				// For all other clean strings
				else 
				{
					// All other strings are added
					processedWords.add(i);
				}

			}
			// If any of the specified patterns are found, add the string to a deleted words list
			else 
			{
				// Don't add words with undesired characters listed above
				deletedWords.add(i);
			}
		}
		
		// Array list for removing punctuation
		ArrayList<String>postProcessedWords = new ArrayList<String>();
		
		// Loop through list of post processed words
		for (String word : processedWords) 
		{
			// Split each string upon the special hyphens to separate the strings into multiple entries
			String newWords[] = word.split("&mdash;");
			
			// If the new string array containing split text has elements
			if (newWords.length > 0) 
			{
				// Loop through each of those elements
				for (String i : newWords) 
				{
					// Check for (and remove) <SPAN> tags (I think these are used for CSS markups? not sure)
					String str = i.replaceAll("(.*?)</SPAN>", "$1");
					postProcessedWords.add(str);
				}
			}
			else
			{
				postProcessedWords.add(word);
			}
		}
		
		// Loop through the post processed words to remove punctuation and add keys and increment values to the hash map
		for(String word : postProcessedWords)
		{
			// Use regex to replace (remove) punctuation at the beginning and end of the string
			String punctuationSuffix = word.replaceAll("^(.*?)\\p{Punct}+$", "$1");
			String punctuationPrefix = punctuationSuffix.replaceAll("^\\p{Punct}+(.*?)$", "$1");
			
			// If a key doesn't exist in a hashmap, null is returned
			if(counterMap.get(punctuationPrefix) == null) 
			{
			    // At this point, we know that this key doesn't exist, so create a new entry with 1 as the count
			    counterMap.put(punctuationPrefix, 1);
			} 
			else 
			{
			    //We know this key already exists. Get the existing value and increment it, then update
			    //the value
			    int count = counterMap.get(punctuationPrefix);
			    counterMap.put(punctuationPrefix, count + 1);
			}
			
		}
		return counterMap;
	}

}

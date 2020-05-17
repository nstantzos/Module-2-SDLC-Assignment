
public class RegexMatcher 
{
	String PoemBegin;
	String PoemEnd;
	String BRTags;
	String ParagraphTags;
	String SPAN;
	String Italics;
	String ItalicsAfter;
	String MDashAfter;
	
	public RegexMatcher() 
	{
		// Starting line of poem text
		this.PoemBegin = "Once upon a midnight dreary";
		// Ending line of poem text
		this.PoemEnd = "lifted.*?nevermore!";
		// Line break tag. Group 1 returns the poem text
		this.BRTags = "(.*?)<BR>";
		// Paragraph tag
		this.ParagraphTags = "^<[/P]";
		// SPAN tag. Group 1 returns the poem text
		this.SPAN = "^(.*?)</SPAN>";
		// Italic tag. Group 1 returns the poem text
		this.Italics = "<I>(.*?)</I>";
		// Italic tag. Groups 1 and 2 both return poem text
		this.ItalicsAfter = "(.*?)<I>(.*?)</I>";
		// Special hyphen text. Group 1 returns the poem text
		this.MDashAfter = "(.*?)&mdash;$";
	}

}

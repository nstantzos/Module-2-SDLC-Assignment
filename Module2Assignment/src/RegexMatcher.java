
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
		this.PoemBegin = "Once upon a midnight dreary";
		this.PoemEnd = "lifted.*?nevermore!";
		this.BRTags = "(.*?)<BR>";
		this.ParagraphTags = "^<[/P]";
		this.SPAN = "^(.*?)</SPAN>";
		this.Italics = "<I>(.*?)</I>";
		this.ItalicsAfter = "(.*?)<I>(.*?)</I>";
		this.MDashAfter = "(.*?)&mdash;$";
	}
//	private String PoemBegin = "Title";
//
//	public String getPoemBegin() {
//		return PoemBegin;
//	}
//
//	public void setPoemBegin(String poemBegin) {
//		PoemBegin = poemBegin;
//	}


}

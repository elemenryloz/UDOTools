package parser;

public interface UdoParserListener {
	/**
	 * Callback routine for parser.
	 * 
	 * @param rule - Root of the grammar to use.
	 * @param text - text to be parsed.
	 */
	public abstract void callBack(UdoParserRule rule, String text); 
}
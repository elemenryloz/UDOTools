package parser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UdoParserTerminal extends UdoParserRule {

	final static int STRING = 0;
	public final static int REGEX = 1;

	private String rule;
	private int mode = STRING;
	
	public UdoParserTerminal(String r) {
		super("");
		rule = r;
	}
	
	public UdoParserTerminal(String n, String r) {
		super(n);
		rule = r;
	}
	
	public UdoParserTerminal(String r, int m) {
		super("");
		rule = r;
		mode = m;
	}
	
	public UdoParserTerminal(String n, String r, int m) {
		super(n);
		rule = r;
		mode = m;
	}
	
	public UdoParserTerminal addParserListener(UdoParserListener caller){
		this.caller.add(caller);
		return this;
	}
	
	@Override
	void show(){
		String d = (mode==REGEX ? "/" : "\"");
		System.out.println(ruleName+" : "+d+rule+d);
	}
	
	@Override
	int parseInternal() {

		String data = parser.getText().substring(parser.getPos());
		int lrc = 0;
		if( mode == REGEX ){
			// process regular expression
			Pattern pat = Pattern.compile("^"+rule);
			Matcher m = pat.matcher(data);
			if (m.find()) 
				parser.setPos(parser.getPos() + m.group(0).length());
			else lrc = 1;	
		} else {
			// process string
			if ( data.startsWith(rule) ) parser.setPos(parser.getPos() + rule.length());
			else lrc = 1;
		}
		return lrc;
	}
}

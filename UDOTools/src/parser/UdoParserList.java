package parser;
import java.util.Vector;

public class UdoParserList extends UdoParserRule {

	
	private Vector<ListToken> tokens = new Vector<ListToken>();
	
	private class ListToken {
		public UdoParserRule rule;
		public Repeat mode;
		public ListToken(UdoParserRule rule, Repeat mode) {
			this.rule = rule;
			this.mode = mode;
		}
	}
	
	public UdoParserList() {
		super("");
	}
	
	public UdoParserList(String n) {
		super(n);
	}
	
	public UdoParserList addToken(UdoParserRule c){
		return addToken(c, Repeat.EXACTLYONE);
	}
	
	public UdoParserList addToken(UdoParserRule rule, Repeat mode){
		ListToken c = new ListToken(rule, mode);
		tokens.add(c);
		return this;
	}

	public UdoParserList addString(String text){
		return addString(text, Repeat.EXACTLYONE);
	}

	public UdoParserList addString(String text, Repeat mode){
		ListToken c = new ListToken(new UdoParserTerminal("",text), mode);
		tokens.add(c);
		return this;
	}
	
	public UdoParserList addString(String text, String name, UdoParserListener listener){
		return addString(text, Repeat.EXACTLYONE, name, listener);
	}

	public UdoParserList addString(String text, Repeat mode, String name, UdoParserListener listener){
		ListToken c = 
				new ListToken(new UdoParserTerminal(name,text,UdoParserTerminal.STRING)
					.addParserListener(listener)
				, mode);
		tokens.add(c);
		return this;
	}
	
	public UdoParserList addRegex(String text){
		return addRegex(text, Repeat.EXACTLYONE);
	}

	public UdoParserList addRegex(String text, Repeat mode){
		ListToken c = new ListToken(new UdoParserTerminal("",text,UdoParserTerminal.REGEX), mode);
		tokens.add(c);
		return this;
	}
	
	public UdoParserList addRegex(String text, String name, UdoParserListener listener){
		return addRegex(text, Repeat.EXACTLYONE, name, listener);
	}

	public UdoParserList addRegex(String text, Repeat mode, String name, UdoParserListener listener){
		ListToken c = 
				new ListToken(new UdoParserTerminal(name,text,UdoParserTerminal.REGEX)
					.addParserListener(listener)
				, mode);
		tokens.add(c);
		return this;
	}
	
	public UdoParserList addParserListener(UdoParserListener caller){
		this.caller.add(caller);
		return this;
	}
	
	@Override
	void show(){
		System.out.print(ruleName+" :");
		for(ListToken t : tokens){
			System.out.print(" "+(t.rule instanceof UdoParserTerminal ? "\"" : "<")+t.rule.ruleName+(t.rule instanceof UdoParserTerminal ? "\"" : ">"));
			if(t.mode==Repeat.ZEROORMORE) System.out.print("*");
			else if(t.mode==Repeat.ONEORMORE) System.out.print("+");
			else if(t.mode==Repeat.ZEROORONE) System.out.print("?");
		}
		System.out.println(" ;");
	}

	@Override
	int parseInternal() {

		int lrc = 0;
		int xrc;
		for( ListToken lt: tokens){
			switch(lt.mode) {
			case EXACTLYONE:
				lrc = lt.rule.parse(parser);
				break;
			case ZEROORONE:
				lrc = 0;
				if( parser.getPos() < parser.getText().length() ){
					lt.rule.parse(parser);
				}
				break;
			case ZEROORMORE:
				xrc = lrc = 0;
				while( (parser.getPos()<parser.getText().length()) && (xrc==0) ){
					xrc = lt.rule.parse(parser);
				}
				break;
			case ONEORMORE:
				xrc = lrc = lt.rule.parse(parser);;
				while( (parser.getPos()<parser.getText().length()) && (xrc==0) ){
					xrc = lt.rule.parse(parser);
				}
				break;
			default:
				lrc = 1;
			}
			if( lrc!=0 ) break;
		}
		return lrc;
	}
}

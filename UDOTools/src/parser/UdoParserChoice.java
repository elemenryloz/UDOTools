package parser;
import java.util.Vector;

import parser.UdoParserRule.Repeat;

public class UdoParserChoice extends UdoParserRule {

	private Vector<ChoiceToken> tokens = new Vector<ChoiceToken>();
	
	private class ChoiceToken {
		public UdoParserRule rule;
		public Repeat mode;
		public ChoiceToken(UdoParserRule rule, Repeat mode) {
			this.rule = rule;
			this.mode = mode;
		}
	}
	
	public UdoParserChoice() {
		super("");
	}
	
	public UdoParserChoice(String n) {
		super(n);
	}
	
	public UdoParserChoice addToken(UdoParserRule c){
		return addToken(c, Repeat.EXACTLYONE);
	}
	
	public UdoParserChoice addToken(UdoParserRule rule, Repeat mode){
		ChoiceToken c = new ChoiceToken(rule, mode);
		tokens.add(c);
		return this;
	}
	
	public UdoParserChoice addString(String text){
		return addString(text, Repeat.EXACTLYONE);
	}

	public UdoParserChoice addString(String text, Repeat mode){
		ChoiceToken c = new ChoiceToken(new UdoParserTerminal("",text), mode);
		tokens.add(c);
		return this;
	}

	public UdoParserChoice addString(String text, String name, UdoParserListener listener){
		return addString(text, Repeat.EXACTLYONE, name, listener);
	}

	public UdoParserChoice addString(String text, Repeat mode, String name, UdoParserListener listener){
		ChoiceToken c = 
				new ChoiceToken(new UdoParserTerminal(name,text,UdoParserTerminal.STRING)
					.addParserListener(listener)
				, mode);
		tokens.add(c);
		return this;
	}
	
	public UdoParserChoice addRegex(String text){
		return addRegex(text, Repeat.EXACTLYONE);
	}

	public UdoParserChoice addRegex(String text, Repeat mode){
		ChoiceToken c = new ChoiceToken(new UdoParserTerminal("",text,UdoParserTerminal.REGEX), mode);
		tokens.add(c);
		return this;
	}
	
	public UdoParserChoice addRegex(String text, String name, UdoParserListener listener){
		return addRegex(text, Repeat.EXACTLYONE, name, listener);
	}

	public UdoParserChoice addRegex(String text, Repeat mode, String name, UdoParserListener listener){
		ChoiceToken c = 
				new ChoiceToken(new UdoParserTerminal(name,text,UdoParserTerminal.REGEX)
					.addParserListener(listener)
				, mode);
		tokens.add(c);
		return this;
	}
		
	
	public UdoParserChoice addParserListener(UdoParserListener caller){
		this.caller.add(caller);
		return this;
	}
	
	@Override
	void show(){
		System.out.print(ruleName+" :");
		boolean first=true;
		for(ChoiceToken t : tokens){
			if(!first) System.out.print(" |");
			System.out.print(" <"+t.rule.ruleName+">");
			if(t.mode==Repeat.ZEROORMORE) System.out.print("*");
			else if(t.mode==Repeat.ONEORMORE) System.out.print("+");
			else if(t.mode==Repeat.ZEROORONE) System.out.print("?");
			first=false;
		}
		System.out.println(" ;");
	}

	
	@Override
	int parseInternal() {
		int lrc = 0;
		int xrc;
		for( ChoiceToken ct: tokens){
			switch(ct.mode) {
			case EXACTLYONE:
				lrc = ct.rule.parse(parser);
				break;
			case ZEROORONE:
				lrc = 0;
				if( parser.getPos() < parser.getText().length() ){
					ct.rule.parse(parser);
				}
				break;
			case ZEROORMORE:
				xrc = lrc = 0;
				while( (parser.getPos()<parser.getText().length()) && (xrc==0) ){
					xrc = ct.rule.parse(parser);
				}
				break;
			case ONEORMORE:
				xrc = lrc = ct.rule.parse(parser);;
				while( (parser.getPos()<parser.getText().length()) && (xrc==0) ){
					xrc = ct.rule.parse(parser);
				}
				break;
			default:
				lrc = 1;
			}
			if( lrc==0 ) break;
		}
		return lrc;
	}
}

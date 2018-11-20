package parser;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JOptionPane;

import parser.UdoParserRule.Repeat;


public class UdoParser {

	private String text = "";
	private int pos=0;
	private UdoParserListener caller;
	Vector<UdoParserListener> listener = new Vector<UdoParserListener>(); 
	Stack<UdoParserEvent> stack = new Stack<UdoParserEvent>();

	public boolean debug = false;
	int debugIndent;
	
	/**
	 * <i><b>Parser</b></i>
	 * <p>
	 * Creates a Parser instance from the given grammar and caller object.
	 * 
	 * @param grammar - Grammar to be used for parsing
	 * @param caller  - Caller object to be notified when a token is successfully parsed. 
	 */
	public UdoParser() {
		debug = true;
		debugIndent = 0;
	}
	
	
	public int parse(String text, String grammar, String root, final UdoParserListener listener){
		/*
		"_note			: <_note1> | <_note2> | <_note3> ;"+
		"_note1			: /[CDFGA]#/ ;"+
		"_note2			: /[DEGAB]b/ ;"+
		"_note3			: /[ABCDEFG]/ ;"+
		"root			: <_note>;"+
		"extended		: /M?/ <_extended>;"+
		"_extended		: 2 | 4 | 69 | 6 | 7 | 9 | 11 | 13 ;"+
		"major			: <extended> | /M(aj(or)?)?/ ;"+
		"minor			: /m(in(or)?)?/ | - ;"+
		"augmented		: aug | + ;"+
		"diminished		: dim  | 0 | o | ° ;"+
		"halfdiminished	: ø | Ø ;"+
		"alteration		: #5 | #9 | #11 | b5 | b9 | b13 ;"+
		"addition		: add <_addition> ;"+
		"_addition		: 2 | 4 | 6 | 9 | 11 | 13 ;"+
		"subtraction	: /no[35]/ ;"+
		"suspension		: /sus[24]?/ ;"+
		"_quality		: <major> | <minor> <extended>? | <augmented> <extended>? | <diminished> <extended>? | <halfdiminished> <extended>? ;"+
		"_modifier		: <alteration> | <addition> | <subtraction> | <suspension> ;"+
		"slashbass		: <_note> ;"+
		"_slashbass		: \"/\" <slashbass> ;"+
	 	_chord			: <root> <_quality>? <_modifier>* <_slashbass>? ;" */
		
		
		
		final HashMap<String,UdoParserRule> rules = new HashMap<>();
		
		UdoParserListener pListener = new UdoParserListener() {
			
			String name;
			String mode;
			UdoParserChoice c;
			UdoParserList l;
			UdoParserTerminal t;
			int lx=0;
			
			@Override
			public void callBack(UdoParserRule rule, String text) {
				String n = rule.ruleName;
				switch(n){
				case "SimpleRule":
				case "ChoiceRule":
					c=null;	l=null;	t=null;
					mode=n;
					break;
				case "Name":
					name = text;
					if(mode.equals("ChoiceRule")){
						c = new UdoParserChoice(name);
						if (!name.startsWith("_")) c.addParserListener(listener);
						rules.put("<"+name+">", c);
					}
					break;
				case "List":
					lx++;
					String iname = "_list"+lx;
					l = new UdoParserList(iname);
					if (!name.startsWith("_")) l.addParserListener(listener);
					rules.put("<"+iname+">", l);
					c.addToken(l);
					break;
				case "String":
					if(mode.equals("SimpleRule")){
						t =  new UdoParserTerminal(name,text);
						if (!name.startsWith("_")) t.addParserListener(listener);
						rules.put("<"+name+">",t);
					} else {
						t =  new UdoParserTerminal(text,text);
						l.addToken(t);
					}
					break;
				case "Regex":
					text=text.substring(1,text.length()-1);
					t = new UdoParserTerminal(name,text,UdoParserTerminal.REGEX);
					if (!name.startsWith("_")) t.addParserListener(listener);
					if(mode.equals("SimpleRule")) rules.put("<"+name+">", t);
					else l.addToken(t);
					break;
				case "Reference":
					Repeat q;
					switch (text.charAt(text.length()-1)) {
					case '+':
						q=UdoParserRule.Repeat.ONEORMORE;
						text=text.substring(0,text.length()-1);
						break;
					case '*':
						q=UdoParserRule.Repeat.ZEROORMORE;
						text=text.substring(0,text.length()-1);
						break;
					case '?':
						q=UdoParserRule.Repeat.ZEROORONE;
						text=text.substring(0,text.length()-1);
						break;
					default:
						q = UdoParserRule.Repeat.EXACTLYONE;
						break;
					}
					
					UdoParserRule r = rules.get(text);
					if(r==null) JOptionPane.showMessageDialog(null, "unresolved reference >" +text+"<");
					l.addToken(r,q);
					break;
				default:
					break;
				}
			}
		};
		UdoParserListener pListener2 = new UdoParserListener() {
			
			String name;
			String mode;
			UdoParserChoice c;
			UdoParserList l;
			UdoParserTerminal t;
			int lx=0;
			
			@Override
			public void callBack(UdoParserRule rule, String text) {
				String n = rule.ruleName;
				switch(n){
				case "RULE":
					c=null;	l=null;	t=null;
					mode=n;
					break;
				case "ID":
					name = text;
					if(mode.equals("ChoiceRule")){
						c = new UdoParserChoice(name);
						if (!name.startsWith("_")) c.addParserListener(listener);
						rules.put("<"+name+">", c);
					}
					break;
				case "List":
					lx++;
					String iname = "_list"+lx;
					l = new UdoParserList(iname);
					if (!name.startsWith("_")) l.addParserListener(listener);
					rules.put("<"+iname+">", l);
					c.addToken(l);
					break;
				case "LIT":
					if(mode.equals("SimpleRule")){
						t =  new UdoParserTerminal(name,text);
						if (!name.startsWith("_")) t.addParserListener(listener);
						rules.put("<"+name+">",t);
					} else {
						t =  new UdoParserTerminal(text,text);
						l.addToken(t);
					}
					break;
				case "REGEX":
					text=text.substring(1,text.length()-1);
					t = new UdoParserTerminal(name,text,UdoParserTerminal.REGEX);
					if (!name.startsWith("_")) t.addParserListener(listener);
					if(mode.equals("SimpleRule")) rules.put("<"+name+">", t);
					else l.addToken(t);
					break;
				case "REF":
					Repeat q;
					switch (text.charAt(text.length()-1)) {
					case '+':
						q=UdoParserRule.Repeat.ONEORMORE;
						text=text.substring(0,text.length()-1);
						break;
					case '*':
						q=UdoParserRule.Repeat.ZEROORMORE;
						text=text.substring(0,text.length()-1);
						break;
					case '?':
						q=UdoParserRule.Repeat.ZEROORONE;
						text=text.substring(0,text.length()-1);
						break;
					default:
						q = UdoParserRule.Repeat.EXACTLYONE;
						break;
					}
					
					UdoParserRule r = rules.get(text);
					if(r==null) JOptionPane.showMessageDialog(null, "unresolved reference >" +text+"<");
					l.addToken(r,q);
					break;
				default:
					break;
				}
			}
		};
		
		/*
		ebnf : <rule>* ;
		rule : <id> ":" <expr> ";"  ;
		expr : <term>+ <exprx>* ;
		exprx : "|" <term>+ ;
		term : <ref> | <lit> | <regex> ;
		ref : "<" <id> ">" <quantifier>? ;
		lit : <slit> | dlit ;
		slit : "'" <char>+ "'" ;
		dlit : '"' <char>+ '"' ;
		id : /\\p{Word}+/ ;
		quant : /[*+?]/ ;
		char : /\\S/ ;
		ws : /\\s+/ ;
		*/
		
		UdoParserTerminal rWS, rIDCHAR, rQUANT, rDELIM, rEND, rDELIM2, rREGEX, rSLIT, rDLIT;
		UdoParserList rREF, rEXPRX, rEXPR, rRULE, rENBF, rID, rIDREF;
		UdoParserChoice rLIT, rTERM;
		
		rWS = new UdoParserTerminal("WS","\\s+",UdoParserTerminal.REGEX);
		rQUANT = new UdoParserTerminal("QUANT","[*+?]",UdoParserTerminal.REGEX);
		rIDCHAR = new UdoParserTerminal("IDCHAR","[_\\p{Alnum}]",UdoParserTerminal.REGEX);
		rDELIM = new UdoParserTerminal("DELIM","\\s+:\\s+",UdoParserTerminal.REGEX);
		rDELIM2 = new UdoParserTerminal("DELIM2","\\|\\s+",UdoParserTerminal.REGEX);
		rEND = new UdoParserTerminal("END",";\\s*",UdoParserTerminal.REGEX);
		rREGEX = new UdoParserTerminal("REGEX","/[^/]+/\\s+",UdoParserTerminal.REGEX);
		rSLIT = new UdoParserTerminal("SLIT","'[^']+'\\s+",UdoParserTerminal.REGEX);
		rDLIT = new UdoParserTerminal("DLIT","\"[^\"]+\"\\s+",UdoParserTerminal.REGEX);

		rID = new UdoParserList("ID").addToken(rIDCHAR,UdoParserRule.Repeat.ONEORMORE);
		rIDREF = new UdoParserList("IDREF").addString("<").addToken(rID).addString(">");
		rLIT = new UdoParserChoice("LIT").addToken(rSLIT).addToken(rDLIT);
		rREF = new UdoParserList("REF").addToken(rIDREF).addToken(rQUANT,UdoParserRule.Repeat.ZEROORONE).addToken(rWS);
		rTERM = new UdoParserChoice("TERM").addToken(rREF).addToken(rLIT).addToken(rREGEX);
		rEXPRX = new UdoParserList("EXPRX").addToken(rDELIM2).addToken(rTERM,UdoParserRule.Repeat.ONEORMORE);
		rEXPR = new UdoParserList("EXPR").addToken(rTERM,UdoParserRule.Repeat.ONEORMORE).addToken(rEXPRX,UdoParserRule.Repeat.ZEROORMORE);
		
		rRULE = new UdoParserList("RULE").addToken(rID).addToken(rDELIM).addToken(rEXPR).addToken(rEND);
		rENBF = new UdoParserList("EBNF").addToken(rRULE,UdoParserRule.Repeat.ONEORMORE);
		
		
		
		rRULE.addParserListener(pListener2);
		rID.addParserListener(pListener2);
		rREF.addParserListener(pListener2);
		rLIT.addParserListener(pListener2);
		rREGEX.addParserListener(pListener2);
		rEND.addParserListener(pListener2);
		
		
		
		// enbf : <rule>+ <ws>? ; */ 
		UdoParserList ebnf = new UdoParserList("ENBF");
		// rule : <simple_rule> | <choice_rule> ;
		UdoParserChoice rule = new UdoParserChoice("Rule");
		// simple_rule : <lhs> <terminal> <end> ;
		UdoParserList simple_rule = new UdoParserList("SimpleRule").addParserListener(pListener);
		// choice_rule : <lhs> <list> <choice_next>* <end> ;
		UdoParserList choice_rule = new UdoParserList("ChoiceRule").addParserListener(pListener);
		// lhs : <ws>? <name> <delim> ; 
		UdoParserList lhs = new UdoParserList("LHS");
		// choice_next : <choice_delim> <list>
		UdoParserList choice_next = new UdoParserList("ChoiceNext");
		// list : <tokenQ> <list_next>* ;
		UdoParserList list = new UdoParserList("List").addParserListener(pListener);
		// list_next : <ws> <token> ;
		UdoParserList list_next = new UdoParserList("ListNext");
		// token : <reference> | <terminal> ;
		UdoParserChoice token = new UdoParserChoice("Token");
		// terminal : <regex> | <string> ;
		UdoParserChoice terminal = new UdoParserChoice("Terminal");
		// reference : /<[a-zA-z_0-9]+>/ ;
		UdoParserTerminal reference = new UdoParserTerminal("Reference","<[a-zA-z_0-9]+>[*+?]?",UdoParserTerminal.REGEX).addParserListener(pListener);
		// regex : /\/[^\/]*\// ;
		UdoParserTerminal regex = new UdoParserTerminal("Regex","/[^/]*/",UdoParserTerminal.REGEX).addParserListener(pListener);
		// string : /[^\\s<>;|]+/ ;
		UdoParserTerminal string = new UdoParserTerminal("String","[^\\s<>;|]+",UdoParserTerminal.REGEX).addParserListener(pListener);
		// rule_delim : /\\s+:\\s+/ ;
		UdoParserTerminal rule_delim = new UdoParserTerminal("RuleDelim","\\s+:\\s+",UdoParserTerminal.REGEX);
		// choice_delim : /\\s+:\\s+/ ;
		UdoParserTerminal choice_delim = new UdoParserTerminal("ChoiceDelim","\\s+\\|\\s+",UdoParserTerminal.REGEX);
		// name : /[a-zA-z_0-9]+/ ;
		UdoParserTerminal name = new UdoParserTerminal("Name","[a-zA-z_0-9]+",UdoParserTerminal.REGEX).addParserListener(pListener);
		// end : /\\s+;/ ;
		UdoParserTerminal end = new UdoParserTerminal("End", "\\s+;",UdoParserTerminal.REGEX);
		// ws : /\\s+/ ;
		UdoParserTerminal ws = new UdoParserTerminal("WS","\\s+",UdoParserTerminal.REGEX);


		
		ebnf.addToken(rule,UdoParserRule.Repeat.ONEORMORE).addToken(ws,UdoParserRule.Repeat.ZEROORONE);
		rule.addToken(simple_rule).addToken(choice_rule);
		choice_rule.addToken(lhs).addToken(list).addToken(choice_next,UdoParserRule.Repeat.ZEROORMORE).addToken(end);
		simple_rule.addToken(lhs).addToken(terminal).addToken(end);
		lhs.addToken(ws,UdoParserRule.Repeat.ZEROORONE).addToken(name).addToken(rule_delim);
		choice_next.addToken(choice_delim).addToken(list);
		list.addToken(token).addToken(list_next,UdoParserRule.Repeat.ZEROORMORE);
		list_next.addToken(ws).addToken(token);
		token.addToken(reference).addToken(terminal);
		terminal.addToken(regex).addToken(string);
		
		UdoParser ip = new UdoParser();
		int lrc=ip.parse(grammar, rENBF);
		if (lrc!=0) return lrc;
		for(String r: rules.keySet()) rules.get(r).show();
		return parse(text,rules.get("<"+root+">"));
	}
	
	public int parse( String text , UdoParserRule rule ){
		
		if(debug) System.out.println("started parsing. rule: "+rule.ruleName+" text >"+text+"<");
		
		// remember text to be parsed and initialize parsing position
		this.text=text;
		pos=0;
		stack.clear();

		// start parsing with start rule
		int lrc = rule.parse( this );
		
		for(UdoParserEvent e: stack){
			for(UdoParserListener l: e.rule.caller){
				if(debug) System.out.println("notify "+e.rule.ruleName+" = "+text.substring( e.start, e.end ));
				l.callBack(e.rule, text.substring( e.start, e.end ));
			}
		}
		if(debug) System.out.println("finished parsing. rc: "+lrc+" remaining text >"+text.substring(pos)+"<");

		// return parsing result
		return lrc;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public UdoParserListener getCaller() {
		return caller;
	}

	public void setCaller(UdoParserListener caller) {
		this.caller = caller;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

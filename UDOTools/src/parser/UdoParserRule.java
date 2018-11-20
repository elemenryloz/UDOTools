package parser;
import java.util.Vector;

public abstract class UdoParserRule {
	
	
	static public enum Repeat {EXACTLYONE, ZEROORONE, ZEROORMORE, ONEORMORE};
	
	UdoParser parser;
	public String ruleName;
	Vector<UdoParserListener> caller = new Vector<UdoParserListener>(); 
	
	public UdoParserRule(String n) {
		ruleName = n;
	}

	public String toString(){
		return ruleName+"("+this.getClass()+")";
	}
	
	int parse(UdoParser parser) {

		this.parser = parser;
		
		int p = parser.getPos();
		if(parser.debug){
			for(int i=0;i<parser.debugIndent;i++) System.out.print(" ");
			System.out.println("entering "+this.ruleName+" text >"+parser.getText().substring( p )+"<");
			parser.debugIndent++;
		}
		UdoParserEvent e=new UdoParserEvent(this,p);
		parser.stack.push(e);
		
		int lrc = parseInternal();

		if( lrc==0 ) {
			e.end=parser.getPos();
		} else {
			for( ; e!=parser.stack.pop();) ;
			parser.setPos(e.start);
		}
		if(parser.debug){
			parser.debugIndent--;
			for(int i=0;i<parser.debugIndent;i++) System.out.print(" ");
			System.out.println("leaving  "+this.ruleName+" rc: "+lrc+" remaining text >"+parser.getText().substring(parser.getPos() )+"<");
		}
		return lrc;
	}
	
	abstract void show();
	abstract int parseInternal ();

}

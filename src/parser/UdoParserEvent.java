package parser;

public class UdoParserEvent {
	UdoParserRule rule;
	int start;
	int end;
	
	public UdoParserEvent(UdoParserRule rule, int start) {
		this.rule=rule;
		this.start=start;
		this.end=-1;
	}
}

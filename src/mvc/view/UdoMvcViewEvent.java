package mvc.view;

import java.awt.event.ActionEvent;
import java.util.Properties;

public class UdoMvcViewEvent {
	
	public static final int CLOSING = 1;
	public static final int ACTION = 2;
	public static final int OPTIONS = 3;
	
	public Object source;
	public int type;
	private Object data;
	
	public UdoMvcViewEvent() {
	}
	
	public UdoMvcViewEvent(Properties e) {
		source=e;
		type=OPTIONS;
	}

	public UdoMvcViewEvent(ActionEvent e) {
		source=e.getSource();
		type=ACTION;
		data=e.getActionCommand();
	}

	public String toString(){
		if(data!=null) return data.toString();
		if(source!=null) return source.toString();
		return "unknown";
	}

	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}

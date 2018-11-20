package mvc.model;

import java.util.EventObject;

public class UdoMvcModelEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	public static final int SAVE = 1;
	public static final int LOAD = 2;
	
	private int type;
	private Object data;
	
	public UdoMvcModelEvent(Object source) {
		super(source);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}

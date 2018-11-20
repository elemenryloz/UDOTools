package mvc.view.toolbar;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

import mvc.view.UdoMvcView;

public class UdoMvcViewToolBar extends JToolBar {

	UdoMvcView parent;
	
	public UdoMvcViewToolBar(UdoMvcView parent){
		super();
		this.parent=parent;
	}
	
	public UdoMvcViewToolBar(UdoMvcView parent, String name) {
		super(name);
		this.parent=parent;
	}

	public JButton addItem(Action action){
		JButton b=add(action);
		b.setActionCommand((String) action.getValue(Action.NAME));
		return b;
	}
}

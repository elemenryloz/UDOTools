package mvc.view.menu;
import javax.swing.Action;
import javax.swing.JMenuItem;

import mvc.view.UdoMvcView;

public class UdoMvcViewMenuItem extends JMenuItem {

	UdoMvcView parent;
	
	UdoMvcViewMenuItem(UdoMvcView parent, Action action) {
		super(action);
		this.parent = parent;
	}
}

package mvc.view.menu;
import javax.swing.Action;
import javax.swing.JMenuBar;

import mvc.view.UdoMvcView;

public class UdoMvcViewMenuBar extends JMenuBar {

	UdoMvcView parent;
	
	public UdoMvcViewMenuBar(UdoMvcView udoMvcView) {
		super();
		this.parent = udoMvcView;
	}
	
	public UdoMvcViewMenu addMenu(String text) {
		UdoMvcViewMenu menu = new UdoMvcViewMenu(parent);
		menu.setText(text);
		add(menu);
		return menu;
	}

	public UdoMvcViewMenuItem addMenuItem(Action action) {
		UdoMvcViewMenuItem item = new UdoMvcViewMenuItem(parent, action);
		add(item);
		return item;
	}
	
	public void setAllMenuItemsEnabled(boolean enable) {
		for(int m=0; m<getMenuCount(); m++){
			UdoMvcViewMenu menu = (UdoMvcViewMenu) getMenu(m);
			if(menu!=null) menu.setAllMenuItemsEnabled(enable);
		}
	}
}

package mvc.view.menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import mvc.view.UdoMvcView;

public class UdoMvcViewMenu extends JMenu implements ActionListener {

	UdoMvcView parent;
	
	UdoMvcViewMenu(UdoMvcView parent) {
		super();
		this.parent = parent;
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void addMenuItem(Object item) {
		try {
			Field f=item.getClass().getField("cmd");
			f.setAccessible(true);
			addMenuItem(f.get(item).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setAllMenuItemsEnabled(boolean enable) {
		for(int m=0; m<getItemCount(); m++){
			JMenuItem item = getItem(m);
			if(item!=null) item.getAction().setEnabled(enable);
		}
	}
}

package mvc.view;

import java.awt.ComponentOrientation;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JLabel;

import mvc.controller.SampleController;
import mvc.model.UdoMvcModel;
import mvc.model.UdoMvcModelEvent;
import mvc.view.action.UdoMvcViewAction;
import mvc.view.menu.UdoMvcViewMenu;
import mvc.view.menu.UdoMvcViewMenuBar;
import mvc.view.toolbar.UdoMvcViewToolBar;

public class SampleView extends UdoMvcView {
	
	private static final long serialVersionUID = 1L;
	
	JLabel label;
	
	public SampleView(){
		UdoMvcViewMenuBar menubar = addMenubar();
		UdoMvcViewToolBar toolbar = addToolBar();
		UdoMvcViewMenu menu = menubar.addMenu("File");
		
		Action action = new UdoMvcViewAction(this, "Open", "../view/images/open.gif", "Open a file...",KeyEvent.VK_O);
		menu.addMenuItem(action);
		toolbar.addItem(action);
		
		action = new UdoMvcViewAction(this, "Save", null);
		menu.addMenuItem(action);
		toolbar.addItem(action);

		action = new UdoMvcViewAction(this, "Exit", "../view/images/close.gif");
		menu.addMenuItem(action);
		toolbar.addItem(action);
		
		menu=menubar.addMenu("Edit");
		action = new UdoMvcViewAction(this, "Help", null);
		menubar.addMenuItem(action).setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label = new JLabel("init");
		add(label);
		pack();
		
	}
	
	public void SetText(String text){
		label.setText(text);
	}

	@Override
	public void handleModelEvent(UdoMvcModelEvent e) {
	}
	
	public static void main (String args[]) {
		SampleView v=new SampleView();
		SampleController cntl=new SampleController();
		cntl.addView(v);
		UdoMvcModel m=new UdoMvcModel();
		cntl.addModel(m);
		v.setListener(cntl);
		v.setVisible(true);

	}


}

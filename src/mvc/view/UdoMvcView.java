package mvc.view;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import mvc.model.UdoMvcModelEventListener;
import mvc.view.menu.UdoMvcViewMenuBar;
import mvc.view.options.UdoMvcViewOptionsDialog;
import mvc.view.status.UdoMvcViewStatusBar;
import mvc.view.toolbar.UdoMvcViewToolBar;

public abstract class UdoMvcView extends JFrame implements UdoMvcModelEventListener {

	private static final long serialVersionUID = 1L;
	
	private UdoMvcViewEventListener listener;
	private UdoMvcViewOptionsDialog options;
	private UdoMvcViewMenuBar menubar;
	private UdoMvcViewStatusBar statusbar;
	private UdoMvcViewToolBar toolbar;
	
	
	public UdoMvcView() {
		super();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		final UdoMvcView self = this;
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				UdoMvcViewEvent e=new UdoMvcViewEvent();
				e.source = self;
				e.type=UdoMvcViewEvent.CLOSING;
				e.setData(getBounds());
				listener.handleViewEvent(e);
			}
		});
	}
	
	public UdoMvcViewEventListener getListener() {
		return listener;
	}
	public void setListener(UdoMvcViewEventListener listener) {
		this.listener=listener;
	}
	
	public UdoMvcViewOptionsDialog addOptions(){
		options = new UdoMvcViewOptionsDialog(this);
		return(options);
	}
	
	public UdoMvcViewMenuBar addMenubar(){
		
		menubar = new UdoMvcViewMenuBar(this);
		this.setJMenuBar(menubar);
		return menubar;
	}

	public void setAllMenuItemsEnabled(boolean enable) {
		menubar.setAllMenuItemsEnabled(enable);
	}
	
	public UdoMvcViewStatusBar addStatusBar(){
		statusbar = new UdoMvcViewStatusBar(this);
		this.add(statusbar,BorderLayout.SOUTH);
		return statusbar;
	}
	
	public void addStatus(String status,boolean temp){
		if (temp) statusbar.setTempText(status);
		else  statusbar.setText(status);
	}
	
	public UdoMvcViewToolBar addToolBar(){
		toolbar = new UdoMvcViewToolBar(this);
		add(toolbar,BorderLayout.NORTH);
		return toolbar;
	}

	public UdoMvcViewToolBar addToolBar(String name) {
		toolbar = new UdoMvcViewToolBar(this, name);
		toolbar.setToolTipText(name);
		add(toolbar,BorderLayout.NORTH);
		return toolbar;
	}

	public UdoMvcViewToolBar getToolbar(){
		return toolbar;
	}

}

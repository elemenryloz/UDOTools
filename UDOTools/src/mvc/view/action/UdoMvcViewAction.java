package mvc.view.action;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import mvc.view.UdoMvcView;
import mvc.view.UdoMvcViewEvent;

public class UdoMvcViewAction extends AbstractAction {

	UdoMvcView parent;
	
	public UdoMvcViewAction(UdoMvcView parent, String title, String iconName) {
		super(title, iconName!=null ? new ImageIcon(new ImageIcon(parent.getClass().getResource(iconName)).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)):null);
		this.parent = parent;
		putValue(SHORT_DESCRIPTION, title);
	}
	
	public UdoMvcViewAction(UdoMvcView parent, String title, String iconName, String desc, int mnemonic) {
		super(title, iconName!=null ? new ImageIcon(new ImageIcon(parent.getClass().getResource(iconName)).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)):null);
		this.parent = parent;
		putValue(SHORT_DESCRIPTION, desc);
	    putValue(MNEMONIC_KEY, mnemonic);	
	}
	
	public UdoMvcViewAction(UdoMvcView parent, String title, String iconName, String desc, KeyStroke acc) {
		super(title, iconName!=null ? new ImageIcon(new ImageIcon(parent.getClass().getResource(iconName)).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)):null);
		this.parent = parent;
		putValue(SHORT_DESCRIPTION, desc);
	    if(acc != null) putValue(MNEMONIC_KEY, acc.getKeyCode());
	    putValue(ACCELERATOR_KEY, acc);	
	}

	public UdoMvcViewAction(UdoMvcView parent, String title, String iconName, String desc) {
		super(title, iconName!=null ? new ImageIcon(new ImageIcon(parent.getClass().getResource(iconName)).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)):null);
		this.parent = parent;
		putValue(SHORT_DESCRIPTION, desc);
	}

	public UdoMvcViewAction(UdoMvcView parent, String title, BufferedImage img, String desc, KeyStroke keyStroke) {
		super(title, img!=null ? new ImageIcon(img.getScaledInstance(24, 24, Image.SCALE_SMOOTH)) : null);
		this.parent = parent;
		putValue(SHORT_DESCRIPTION, desc);
	}

	public String getText() {
		return getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (parent.getListener()!=null) {
			parent.getListener().handleViewEvent(new UdoMvcViewEvent(e));
		}
	}

}

package mvc.view.toolbar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class UdoMvcViewToolBarItem extends JComponent implements DragGestureListener, DragSourceListener, Transferable{
	private static final long serialVersionUID = 1L;
	
	public static DataFlavor myDataFlavor;
	private DragSource dragSource;
	
	private Image image;
	private Component component;
	private boolean onToolbar;
	
	
	public UdoMvcViewToolBarItem(Image bi, Component component) {
		this.image = bi;
		this.component = component;
		Dimension d = new Dimension(bi.getWidth(null), bi.getHeight(null));
		setSize(d);
		setPreferredSize(d);
		setName("");
		if(component!=null) setToolTipText(((JComponent)component).getToolTipText());

	    dragSource = DragSource.getDefaultDragSource();
	    dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this );
		try {
			myDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public UdoMvcViewToolBarItem(JButton item) {
		ImageIcon icon = (ImageIcon)item.getIcon();
		image = icon.getImage();
		component = item;
		Dimension d = new Dimension(image.getWidth(null), image.getHeight(null));
		setSize(d);
		setPreferredSize(d);
		setName("");
		if(component!=null) setToolTipText(((JComponent)component).getToolTipText());
	    dragSource = DragSource.getDefaultDragSource();
	    dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this );
		try {
			myDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public UdoMvcViewToolBarItem clone(){
		UdoMvcViewToolBarItem i = new UdoMvcViewToolBarItem(image,component);
		i.setName(getName());
		i.setToolTipText(getToolTipText());
		return i;
	}
	
	
	public void setOnToolbar(boolean tb){
		onToolbar = tb;
	}
	
	public boolean isOnToolbar(){
		return onToolbar;
	}
	
	public void setName(String name){
		super.setName(name);
	}
		
	public Image getImage() {
		return image;
	}
	
	public Component getComponent() {
		return component;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		float dash1[] = {1.0f};
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));
		if(getName().equals("SPACE")){
			g2.setColor(Color.BLACK);
			g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
		} else if(getName().equals("SEPARATOR")){
			g2.setColor(Color.BLACK);
			g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight()-1);
		} else if(getName().equals("FLEX")){
			g2.setColor(Color.BLACK);
			g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
			g2.drawLine(0, getHeight()/2, getWidth()-1, getHeight()/2);
			g2.drawLine(0, getHeight()/2, 5, getHeight()/2+5);
			g2.drawLine(0, getHeight()/2, 5, getHeight()/2-5);
			g2.drawLine(getWidth()-1, getHeight()/2, getWidth()-6, getHeight()/2+5);
			g2.drawLine(getWidth()-1, getHeight()/2, getWidth()-6, getHeight()/2-5);
		} else {
			g.drawImage(image, getWidth()/2-image.getWidth(null)/2, getHeight()/2-image.getHeight(null)/2, null);
		}
	}
	
	// interface DragGestureListener
	@Override
	public void dragGestureRecognized(DragGestureEvent e) {
	    try {
	    	e.startDrag(null, image, new Point(0,0), this, this);
	    	if(getName().equals("") || onToolbar) setVisible(false);
	    } catch( InvalidDnDOperationException idoe ) {
	    	System.err.println( idoe );
	    }
	}
	
	// interface Transferable
	@Override
	public synchronized DataFlavor[] getTransferDataFlavors() { 
		return new DataFlavor[] { myDataFlavor }; 
	}
		
	@Override
	public boolean isDataFlavorSupported( DataFlavor flavor ) { 
		for (DataFlavor f : getTransferDataFlavors()){
			if(f.equals(myDataFlavor)) return true;
		}
		return false;
	}
		
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if(flavor.equals(myDataFlavor)) return this;
		else throw new UnsupportedFlavorException(flavor);
	}

	// interface DragSourceListener
	@Override
	public void dragDropEnd(DragSourceDropEvent e) {
    	if (getName().equals("") || onToolbar){
    		if(e.getDropSuccess()) {
    			getParent().remove(this);
        	} else {
            	setVisible(true);
        	}
    	}
	}

	@Override
	public void dragEnter(DragSourceDragEvent e) {
	      DragSourceContext context = e.getDragSourceContext();
	      int myaction = e.getDropAction();
	      if( (myaction & DnDConstants.ACTION_COPY_OR_MOVE) != 0) { 
	    	  context.setCursor(DragSource.DefaultCopyDrop);    
	      } else {
	    	  context.setCursor(DragSource.DefaultCopyNoDrop);        
	      }	
	}

	@Override
	public void dragExit(DragSourceEvent e) {
	      DragSourceContext context = e.getDragSourceContext();
	      context.setCursor(DragSource.DefaultCopyNoDrop);        
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}	
}


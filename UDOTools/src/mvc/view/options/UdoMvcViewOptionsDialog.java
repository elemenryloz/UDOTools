package mvc.view.options;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import mvc.view.filtertree.FilterTree;
import mvc.view.UdoMvcView;
import mvc.view.UdoMvcViewEvent;
import mvc.view.UdoMvcViewEventListener;

public class UdoMvcViewOptionsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	final static String OPT_BOUNDS = "options.bounds";
	

	private UdoMvcView parent;
	
	Properties prop=new Properties();
	private JSplitPane sp;
	public FilterTree ftree;
	protected UdoMvcViewEventListener listener;
	
	
	public UdoMvcViewOptionsDialog(UdoMvcView parent){
		super();
		
		this.parent = parent;
		
        this.setTitle("Options");
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                sp.setDividerLocation(Math.min(.9, ((double)sp.getLeftComponent().getPreferredSize().width+10)/((double)sp.getWidth())));
            }
        });        
        this.setModal(true);
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints c=new GridBagConstraints();
        
        ftree = new FilterTree( new UdoMvcViewOptionsDlgItem("root"));
        ftree.getTree().addTreeSelectionListener(new TL());
        ftree.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        ftree.getTree().setRootVisible(false);
        
        sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ftree, new JLabel("dlg data"));
        c.fill=GridBagConstraints.BOTH;
        c.weightx=0;
        c.weighty=1;
		c.insets=new Insets(5,5,5,5);
		c.gridx=0;
		c.gridy=0;
		c.gridheight=1;
		c.gridwidth=5;		
        this.add(sp,c);
        
        c.weighty=0;
		c.gridy++;
        this.add(new JSeparator(JSeparator.HORIZONTAL),c);

        JButton button=new JButton("Utils");
        button.setMnemonic(KeyEvent.VK_U);
		c.gridy++;
		c.gridheight=1;
		c.gridwidth=1;		
        this.add(button,c);
        c.weightx=1;
		c.gridx++;
        this.add(new JLabel("               "),c);
        button=new JButton("OK");
        button.setMnemonic(KeyEvent.VK_O);
        button.addActionListener(new OkBL());
        c.weightx=0;
		c.gridx++;
        this.add(button,c);
        button=new JButton("Cancel");
        button.setMnemonic(KeyEvent.VK_C);
        button.addActionListener(new CancelBL());
		c.gridx++;
        this.add(button,c);
        button=new JButton("Help");
        button.setMnemonic(KeyEvent.VK_H);
		c.gridx++;
        this.add(button,c);		
		pack();
        
	}
	
	public void setListener(UdoMvcViewEventListener listener) {
		this.listener=listener;
	}

	public void add(UdoMvcViewOptionsDlgItem n) {
		n.setParent(parent);
		ftree.add(n);
		sp.setDividerLocation(Math.min(.9, ((double)ftree.getPreferredSize().width+10)/((double)sp.getWidth())));
	}
	
	public void show(JFrame frame){
		setLocationRelativeTo(frame);
		super.setVisible(true);
	}
	
	public void setDefaults(Properties prop){
		
		String dim = prop.getProperty(OPT_BOUNDS);
		if (dim==null) dim="0,0,900,800";
		String d[] = dim.split(",");
		this.setBounds(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]), Integer.parseInt(d[3]));
        TreeModel model = ftree.getTree().getModel();
        UdoMvcViewOptionsDlgItem root = (UdoMvcViewOptionsDlgItem) model.getRoot();
        walkDefaults(model,root,prop);    
    }
	
	protected void walkDefaults(TreeModel model, Object o, Properties prop){
		int  cc;
		((UdoMvcViewOptionsDlgItem)o).setDefaults(prop);
		cc = model.getChildCount(o);
		for( int i=0; i < cc; i++) {
			walkDefaults(model,model.getChild(o, i),prop); 
		}
	}
	
	public void saveOptions(){
        TreeModel model = ftree.getTree().getModel();
        UdoMvcViewOptionsDlgItem root = (UdoMvcViewOptionsDlgItem) model.getRoot();
		prop.clear();
		Rectangle r=getBounds();
		prop.put(OPT_BOUNDS, r.x+","+r.y+","+r.width+","+r.height);
		walkSave(model,root);    
		UdoMvcViewEvent e = new UdoMvcViewEvent(prop);
		if(listener!=null) listener.handleViewEvent(e);
    }
	
	protected void walkSave(TreeModel model, Object o){
		int  cc;
		Properties op = ((UdoMvcViewOptionsDlgItem)o).saveOptions();
		prop.putAll(op);
		cc = model.getChildCount(o);
		for( int i=0; i < cc; i++) {
			walkSave(model,model.getChild(o, i)); 
		}
	}
	
	public void cancelOptions(){
        TreeModel model = ftree.getTree().getModel();
        UdoMvcViewOptionsDlgItem root = (UdoMvcViewOptionsDlgItem) model.getRoot();
		prop.clear();
        walkCancel(model,root);    
		UdoMvcViewEvent e = new UdoMvcViewEvent(prop);
		if(listener!=null) listener.handleViewEvent(e);
    }
	
	protected void walkCancel(TreeModel model, Object o){
		int  cc;
		Properties op = ((UdoMvcViewOptionsDlgItem)o).cancelOptions();
		prop.putAll(op);
		cc = model.getChildCount(o);
		for( int i=0; i < cc; i++) {
			walkCancel(model,model.getChild(o, i)); 
		}
	}
	
	public void selectDlg(UdoMvcViewOptionsDlgItem dlg){
        JTree tree = ftree.getTree();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        TreeNode[] nodes = model.getPathToRoot(dlg);
        TreePath tpath = new TreePath(nodes);
        tree.scrollPathToVisible(tpath);
        tree.setSelectionPath(tpath);
	}
	
    /** A listener shared by the text field and add button. */
    class TL implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
		   	UdoMvcViewOptionsDlgItem dlg=(UdoMvcViewOptionsDlgItem)ftree.getTree().getLastSelectedPathComponent();
		   	if (dlg!=null){
			   	sp.setRightComponent(dlg.getDlg());
		   	}
		}	
    }
 
    /** A listener shared by the text field and add button. */
    class OkBL implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent e) {
    		saveOptions();
        	setVisible(false);
        }
    }
 
    /** A listener shared by the text field and add button. */
    class CancelBL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	cancelOptions();
        	setVisible(false);
        }
    }
 
    
	
}

    

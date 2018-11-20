package mvc.view.filtertree;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Enumeration;
 






import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
 
/**
 * Tree widget which allows the tree to be filtered on keystroke time. Only nodes who's
 * toString matches the search field will remain in the tree or its parents.
 *
 * Copyright (c) Oliver.Watkins
 */
 
public class FilterTree extends JPanel{
 
    private String filteredText = "";
    private DefaultTreeModel originalTreeModel;
    private JScrollPane scrollpane = new JScrollPane();
    private JTree tree = new JTree();
    private DefaultMutableTreeNode originalRoot;
	private DefaultMutableTreeNode veryOriginalRoot;
 
    public FilterTree(DefaultMutableTreeNode originalRoot){
        this.originalRoot = originalRoot;
        this.veryOriginalRoot = originalRoot;
        guiLayout();
    }
 
    private void guiLayout() {
        tree.setCellRenderer(new Renderer());
 
        final PrefilledTextfield field = new PrefilledTextfield("enter filter text");
        field.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                super.keyTyped(ke);
                SwingUtilities.invokeLater(new Runnable(){
                	public void run(){
                        filterTree(field.getText());
                	}
                });
            }
        });
        
		field.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {field.transferFocus();}
		    public void focusLost(FocusEvent e) {field.removeFocusListener(this);}
		});
        
 
        originalTreeModel = new DefaultTreeModel(originalRoot);
 
        tree.setModel(originalTreeModel);
 
        this.setLayout(new BorderLayout());
 
        add(field, BorderLayout.NORTH);
        add(scrollpane = new JScrollPane(tree), BorderLayout.CENTER);
 
        originalRoot = (DefaultMutableTreeNode) originalTreeModel.getRoot();
 
    }
 
    /**
     *
     * @param text
     */
 
    private void filterTree(String text) {
        filteredText = text;
        //get a copy
        DefaultMutableTreeNode filteredRoot = copyNode(originalRoot);
 
        if (text.trim().toString().equals("")) {
 
            //reset with the original root
            originalTreeModel.setRoot(originalRoot);
 
            tree.setModel(originalTreeModel);
            tree.updateUI();
            scrollpane.getViewport().setView(tree);
 
            for (int i = 0; i < tree.getRowCount(); i++) {
                tree.expandRow(i);
            }
 
            return;
        } else {
 
            TreeNodeBuilder b = new TreeNodeBuilder(text);
            filteredRoot = b.prune((DefaultMutableTreeNode) filteredRoot.getRoot());
 
            originalTreeModel.setRoot(filteredRoot);
 
            tree.setModel(originalTreeModel);
            tree.updateUI();
            scrollpane.getViewport().setView(tree);
        }
 
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
 
    /**
     * Clone/Copy a tree node. TreeNodes in Swing don't support deep cloning.
     *
     * @param orig to be cloned
     * @return cloned copy
     */
    private DefaultMutableTreeNode copyNode(DefaultMutableTreeNode orig) {
 
        DefaultMutableTreeNode newOne = new DefaultMutableTreeNode();
        newOne.setUserObject(orig.getUserObject());
 
        Enumeration enm = orig.children();
 
        while(enm.hasMoreElements()){
 
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) enm.nextElement();
            newOne.add(copyNode(child));
        }
        return newOne;
    }
 
    /**
     * Renders bold any tree nodes who's toString() value starts with the filtered text
     * we are filtering on.
     *
     * @author Oliver.Watkins
     */
    public class Renderer extends DefaultTreeCellRenderer{
 
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasfocus) {
 
            Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasfocus);
 
            if (c instanceof JLabel){

            	Color hasColor = null;
            	try {
            		Field fld = value.getClass().getField("foregroundColor");
            		fld.setAccessible(true);
            		hasColor=(Color) fld.get(value);
            		if(hasColor!=null){
            			c.setForeground(hasColor);
            		}
            	} catch(Exception e){
            	}

            	Font hasFont=null;
            	try {
            		Field fld = value.getClass().getField("font");
            		fld.setAccessible(true);
            		hasFont=(Font) fld.get(value);
            		if(hasFont!=null){
            			c.setFont(hasFont);
            		}
            	} catch(Exception e){
            	}

                Font f = c.getFont();
            	
                if (!filteredText.equals("") && value.toString().matches(".*"+filteredText+".*")){
                    f = new Font(f.getFontName(), Font.BOLD, f.getSize());
                    c.setFont(f);
                }else{
                    f = new Font(f.getFontName(), Font.PLAIN , f.getSize());
                    c.setFont(f);
                }
                
                if(hasColor!=null && hasFont==null){
                	JPanel p = new JPanel(new BorderLayout());
                	p.setOpaque(false);
                	JPanel l = new JPanel();
                	l.setBackground(hasColor);
                	l.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
                	JPanel ll = new JPanel();
                	ll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
                	ll.setOpaque(false);
                	ll.add(l);
                	p.add(ll,BorderLayout.WEST);
                	((JLabel) c).setIcon(null);
                	c.setForeground(Color.BLACK);
                	c.setFont(tree.getFont());
                	p.add(c,BorderLayout.CENTER);
                	return p;
                } else if(hasColor!=null || hasFont!=null) {
                	JPanel p = new JPanel(new BorderLayout());
                	p.setOpaque(false);
                	JLabel l = new JLabel("aA");
                	l.setOpaque(false);
                	l.setFont(new Font("Arial", Font.BOLD+Font.ITALIC, tree.getFont().getSize()));
                	l.setForeground(Color.BLUE);
                	JPanel ll = new JPanel();
                	ll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
                	ll.setOpaque(false);
                	ll.add(l);
                	p.add(ll,BorderLayout.WEST);
                	((JLabel) c).setIcon(null);
                	p.add(c,BorderLayout.CENTER);
                	return p;
                }
            }
            return c;
        }
    }
 
    public JTree getTree() {
        return tree;
    }
 
    /**
     * Class that prunes off all leaves which do not match the search string.
     *
     * @author Oliver.Watkins
     */
 
    public class TreeNodeBuilder {
 
        private String textToMatch;
 
        public TreeNodeBuilder(String textToMatch) {
            this.textToMatch = textToMatch;
        }
 
        public DefaultMutableTreeNode prune(DefaultMutableTreeNode root) {
 
            boolean badLeaves = true;
 
            //keep looping through until tree contains only leaves that match
            while (badLeaves){
                badLeaves = removeBadLeaves(root);
            }
            return root;
        }
 
        /**
         *
         * @param root
         * @return boolean bad leaves were returned
         */
        private boolean removeBadLeaves(DefaultMutableTreeNode root) {
 
            //no bad leaves yet
            boolean badLeaves = false;
 
            //reference first leaf
            DefaultMutableTreeNode leaf = root.getFirstLeaf();
 
            //if leaf is root then its the only node
            if (leaf.isRoot())
                return false;
 
            int leafCount = root.getLeafCount(); //this get method changes if in for loop so have to define outside of it
            for (int i = 0; i < leafCount; i++) {
 
                DefaultMutableTreeNode nextLeaf = leaf.getNextLeaf();
 
                //if it does not start with the text then snip it off its parent
                if (!leaf.getUserObject().toString().matches(".*"+textToMatch+".*")) {
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) leaf.getParent();
 
                    if (parent != null)
                        parent.remove(leaf);
 
                    badLeaves = true;
                }
                leaf = nextLeaf;
            }
            return badLeaves;
        }
    }

	public void add(DefaultMutableTreeNode n) {
		veryOriginalRoot.add(n);
		filterTree("");
	}     
}
package mvc.view.options;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import mvc.view.UdoMvcView;


public class UdoMvcViewOptionsDlgItem extends DefaultMutableTreeNode {

	UdoMvcView  parent;
	
	public Color color;
	public Font font;
	
	String displayName;
	String labelText=null;
	public HashMap<String,String> options = new HashMap<String,String>();
	public HashMap<String,String> oldOptions = new HashMap<String,String>();
	
	public UdoMvcViewOptionsDlgItem(String displayName) {
		super(displayName);
		this.displayName=displayName;
	}

	public UdoMvcViewOptionsDlgItem(String displayName, String labelText) {
		super(displayName);
		this.displayName=displayName;
		this.labelText=labelText;
	}
	
	public void setParent(UdoMvcView parent){
		this.parent = parent;
	}
	
	public UdoMvcView getView(){
		return this.parent;
	}
	
	public JPanel getDlg(){
		JPanel dlg = new JPanel();
		oldOptions.clear();
		oldOptions.putAll(options);
		if(labelText!=null) dlg.add(new JLabel(labelText));
		return dlg;
	}
	
	public Properties saveOptions(){
		Properties prop = new Properties();
		for (String o: options.keySet()){
			prop.setProperty(o, options.get(o).toString());
		}
		return prop;
	}
	
	public Properties cancelOptions() {
		return saveOptions();
	}
	
	public void setDefaults(Properties prop){
	}
	
	public class ColorItem extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;
		public Color foregroundColor;
		public Font font;
		public String keycolor;
		public String keyfont;
		public ColorItem(String text, String keycolor, String keyfont) {
			super(text);
			this.keycolor=keycolor;
			if(keycolor!=null) foregroundColor=Color.decode(options.get(keycolor));
			this.keyfont=keyfont;
			if(keyfont!=null){
				String f[] = options.get(keyfont).split(",");
				font=new Font(f[0],Integer.parseInt(f[1]),Integer.parseInt(f[2]));
			}
		}
	}

	
}

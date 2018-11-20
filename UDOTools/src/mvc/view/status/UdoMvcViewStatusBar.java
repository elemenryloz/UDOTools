package mvc.view.status
;
import javax.swing.JComboBox;

import mvc.view.UdoMvcView;

public class UdoMvcViewStatusBar extends JComboBox<String> {
	
	UdoMvcView view;
	
	public UdoMvcViewStatusBar(UdoMvcView view){
		super();
		this.view=view;
	}
	
	public void setText(String text){
		addItem(text);
		setSelectedIndex(getItemCount()-1);
	}
	public void setTempText(String text){
		setEditable(true);
		setSelectedItem(text);
		setEditable(false);
	}
}

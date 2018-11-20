package mvc.view.filedialog;
import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;


public class UdoFileDialog extends JFileChooser {
		
	private boolean warnExistingFile = true;
	private boolean defaultExtention = true;
	private String validExtentions[] = null;

	public UdoFileDialog(){
		super();
	}

	public UdoFileDialog(int what){
		super();
		setFileSelectionMode(what);
	}

	public UdoFileDialog(String fileType, String...extentions ){
		super();
		setFileSelectionMode(UdoFileDialog.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
        addChoosableFileFilter(new FileNameExtensionFilter(fileType,extentions));	
        validExtentions=extentions;
	}
	
	public void setWarnExistingFile(boolean warn){
		warnExistingFile = warn;
	}

	public int showDialog(Component parent, String buttonText){
		int rc = super.showDialog(parent, buttonText);
		if (rc == UdoFileDialog.APPROVE_OPTION) {
			File file = getSelectedFile();
			
			if(validExtentions!=null && defaultExtention){
				boolean addex=true;
				for(String ex: validExtentions){
					if (file.getName().endsWith("."+ex)){
						addex=false;
						break;
					}
				}
				if (addex) {
					file=new File(file.getAbsolutePath()+"."+validExtentions[0]);
					this.setSelectedFile(file);
				}
			}
			
			if(warnExistingFile && file.exists()){
				String options[] = { UIManager.getString("OptionPane.okButtonText"), UIManager.getString("OptionPane.cancelButtonText") };
				rc = JOptionPane.showOptionDialog(this, 
						"file "+file.getName()+" already exists\nin "+file.getParent()+".\nReplace?", 
						"Warning", 
						JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						options[1]);
				if(rc==JOptionPane.OK_OPTION) rc=UdoFileDialog.APPROVE_OPTION;
				else rc=UdoFileDialog.CANCEL_OPTION;
			}
		}
		return rc;
	}
}

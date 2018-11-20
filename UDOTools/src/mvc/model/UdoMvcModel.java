package mvc.model;
import java.io.IOException;
import java.util.Properties;

import mvc.controller.UdoMvcController;
import mvc.model.options.UdoMvcModelOptions;


public class UdoMvcModel {

	private UdoMvcController controller;
	private UdoMvcModelOptions options;
	
	public void setController(UdoMvcController controller) {
		this.controller=controller;
	}

	public UdoMvcModelOptions addOptions(String file) throws IOException{
		options = new UdoMvcModelOptions(file);
		options.load();
		return(options);
	}

	public void setOptions(Properties props) {
//		options.clear();
		options.putAll(props);
	}

	public void saveOptions() throws IOException {
		if(options!=null) options.save();
	}
	
	public UdoMvcModelOptions getOptions() {
		return options;
	}
	
	public void fireEvent(UdoMvcModelEvent e){
		if(controller!=null) controller.handleModelEvent(e);
	}

}

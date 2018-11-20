package mvc.controller;
import java.util.Vector;

import mvc.model.UdoMvcModel;
import mvc.model.UdoMvcModelEvent;
import mvc.model.UdoMvcModelEventListener;
import mvc.view.UdoMvcView;
import mvc.view.UdoMvcViewEventListener;


public abstract class UdoMvcController implements UdoMvcViewEventListener, UdoMvcModelEventListener {

	Vector<UdoMvcModel> models = new Vector<UdoMvcModel>();
	Vector<UdoMvcView> views = new Vector<UdoMvcView>();
	
	public UdoMvcController(){
	}
	
	public void addView(UdoMvcView view){
		view.setListener(this);
		views.add(view);
	}
	
	public void addModel(UdoMvcModel model){
		model.setController(this);
		models.add(model);
	}

	public void handleModelEvent(UdoMvcModelEvent e){
		for(UdoMvcModelEventListener view: views){
			view.handleModelEvent(e);
		}
	}
}

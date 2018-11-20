package mvc.controller;
import mvc.view.SampleView;
import mvc.view.UdoMvcView;
import mvc.view.UdoMvcViewEvent;


public class SampleController extends UdoMvcController {

	
	public SampleController() {
	}

	public void handleViewEvent(UdoMvcViewEvent e){
		if(e.type==UdoMvcViewEvent.ACTION){
			for(UdoMvcView view: views){
				((SampleView)view).SetText((String) e.getData());
			}
		}
	}
	//
}

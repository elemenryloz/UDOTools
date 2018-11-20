package mvc.model.options;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;
 
/**
 * Holds all the properties
 * @author Udo
 *
 */
public class UdoMvcModelOptions extends Properties {

	private static final long serialVersionUID = 1L;

	String file;

	
	/**
	 * Constructor
	 * @param fileName name of the properties files to be loaded
	 * @throws FileNotFoundException 
	 */
    public UdoMvcModelOptions(String file) throws FileNotFoundException {
    	super();
		this.file = file;
    }

    /**
     *  Loads the properties file 
     * @throws IOException 
     */
    public void load() throws IOException {
    		
		// load the properties file
    	super.clear();
		super.load(new FileInputStream(file));
    }		

    /**
     * Saves the properties file
     * @throws IOException 
     */
	public void save() throws IOException {

		// save the properties file
		Properties tmp = new Properties() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public synchronized Enumeration<Object> keys() {
		        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
		    }
		};		
		tmp.putAll(this);
		tmp.store(new FileOutputStream(file), null);

	}
}
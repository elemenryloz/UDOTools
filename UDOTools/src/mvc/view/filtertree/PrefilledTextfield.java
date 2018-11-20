package mvc.view.filtertree;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
 


import javax.swing.JTextField;
 
public class PrefilledTextfield extends JTextField implements KeyListener, FocusListener{
    private String filler;
    private Color user_yellow;
   
    public PrefilledTextfield(String filler){
        user_yellow = new Color(255,255,215);
       
        this.filler = filler;
        this.reset();
        this.setBackground(user_yellow);
        this.setToolTipText("Hier bitte "+getFiller()+" eingeben.");
       
       
        addKeyListener(this);
        addFocusListener(this);
    }
    public String getText(){
    	if(super.getText().equals(filler)) return "";
        return super.getText();
    }
    
    public String getFiller(){
        return filler;
    }
   
    public void reset(){
        setText(filler);
        setForeground(Color.LIGHT_GRAY);
        setCaretPosition(0);
    }
   
    @Override
    public void keyPressed(KeyEvent arg0) {
        if(super.getText().equals(filler)){
            setText("");
            this.setForeground(Color.BLACK);
        }
       
    }
    @Override
    public void keyReleased(KeyEvent arg0) {
        if(super.getText().equals("")){
            reset();
        }
    }
    @Override
    public void keyTyped(KeyEvent arg0) {
    }
   
    @Override
    public void focusGained(FocusEvent arg0) {
        if(super.getText().equals(filler)){
            setText("");
            this.setForeground(Color.BLACK);
        }
       
    }
    @Override
    public void focusLost(FocusEvent arg0) {
        if(super.getText().equals("")){
            reset();
        }
    }
   
}
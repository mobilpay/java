package ro.mobilPay.util;
import java.io.Serializable;

public class ListItem implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -6532335112800057526L;
	public String id;
	public String key;
	public String val;
  
  public ListItem(String id, String key) {
    this.id = id;
    this.key = key;
    this.val = null;
  }
    public ListItem(String id, String key,String val) {
      this.id = id;
      this.key = key;
      this.val = val;
    }
    
    public String getId() {
        return id;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getVal() {
        return val;
    }
}
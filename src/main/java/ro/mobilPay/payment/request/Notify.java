package ro.mobilPay.payment.request;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.mobilPay.payment.Address;
import ro.mobilPay.util.MD5;

public class Notify {
	private final int ERROR_LOAD_FROM_XML_CRC_ATTR_MISSING		= 0x60000001;
	private final int ERROR_LOAD_FROM_XML_ACTION_ELEM_MISSING	= 0x60000002;
	
	public String _purchaseId		= null;
	public String _action			= null;
	public int _errorCode		= 0;
	public String _errorMessage	= null;
	public long _timestamp		= 0;
	public double _originalAmount	= 0;
	public double _processedAmount	= 0;
	public Address _customer		= null;
	public String _crc			= null;
	public String _pan_masked	= null;
	public int _payment_instrument_id = 0;
	public int _current_payment_count = 0;
	
	public Notify (){
		
	}

	public boolean loadFromXML(Element elem) throws Exception {
		
		Node tmpNode = null;
    	NodeList tmpList = null;
    	
    	tmpNode = elem.getAttributes().getNamedItem("timestamp");
    	if(tmpNode != null)
    		this._timestamp = Long.parseLong(tmpNode.getTextContent());
    	tmpNode = elem.getAttributes().getNamedItem("crc");
    	if(tmpNode != null)
    		this._crc = tmpNode.getTextContent();
    	
    	tmpList = elem.getElementsByTagName("action");
    	
		if(tmpList.getLength() != 1)
			throw new Exception("Mobilpay_Payment_Request_Notify::loadFromXml failed; mandatory crc attribute missing " + this.ERROR_LOAD_FROM_XML_CRC_ATTR_MISSING); 
		this._action = tmpList.item(0).getTextContent();
		
		tmpList = elem.getElementsByTagName("customer");
		if(tmpList.getLength() == 1)
			this._customer = new Address(tmpList.item(0));
		
		tmpList = elem.getElementsByTagName("purchase");
		if(tmpList.getLength() == 1)
			this._purchaseId = tmpList.item(0).getTextContent();
		
		tmpList = elem.getElementsByTagName("original_amount");
		if(tmpList.getLength() == 1)
			this._originalAmount = Double.parseDouble(tmpList.item(0).getTextContent());
		
		tmpList = elem.getElementsByTagName("processed_amount");
		if(tmpList.getLength() == 1)
			this._processedAmount = Double.parseDouble(tmpList.item(0).getTextContent());
		
		tmpList = elem.getElementsByTagName("pan_masked");
		if(tmpList.getLength() == 1)
			this._pan_masked = tmpList.item(0).getTextContent();
		
	 	tmpNode = elem.getAttributes().getNamedItem("current_payment_count");
    	if(tmpNode != null)
    		this._current_payment_count = Integer.parseInt(tmpNode.getTextContent());
    	
	 	tmpNode = elem.getAttributes().getNamedItem("payment_instrument_id");
    	if(tmpNode != null)
    		this._payment_instrument_id = Integer.parseInt(tmpNode.getTextContent());
   

		tmpList = elem.getElementsByTagName("error");
		if(tmpList.getLength() == 1) {
			tmpNode = tmpList.item(0);
			Node n = tmpNode.getAttributes().getNamedItem("code");
			if(n != null)
				this._errorCode = Integer.parseInt(n.getTextContent());
			
			this._errorMessage = tmpNode.getNodeValue();
		}
		
		return true;
	}
	
	public HashMap<String,String> loadFromQueryString(String queryString) throws UnsupportedEncodingException{
		HashMap<String,String> map = new HashMap<String,String>();
		StringTokenizer st = new StringTokenizer(queryString, "&"); 
		
		while(st.hasMoreTokens()) {
			String paramequalval = st.nextToken();
			StringTokenizer stt = new StringTokenizer(paramequalval,"=");
			while(stt.hasMoreTokens())
				map.put(stt.nextToken(), java.net.URLEncoder.encode(stt.nextToken(),"UTF-8"));
		}
		
		String s = map.get("mobilpay_refference_id");
		if(s != null)
			this._purchaseId = s;
		
		s = map.get("mobilpay_refference_action");
		if(s != null)
			this._action = s;

		s = map.get("mobilpay_refference_original_amount");
		if(s != null)
			this._action = s;

		s = map.get("mobilpay_refference_original_amount");
		if(s != null)
			this._originalAmount = Double.parseDouble(s);
		
		s = map.get("mobilpay_refference_processed_amount");
		if(s != null)
			this._processedAmount = Double.parseDouble(s);
		
		s = map.get("mobilpay_refference_error_code");
		if(s != null)
			this._errorCode = Integer.parseInt(s);
		
		s = map.get("mobilpay_refference_error_message");
		if(s != null)
			this._errorMessage = s;
		
		s = map.get("mobilpay_refference_timestamp");
		if(s != null)
			this._timestamp = Long.parseLong(s);
		
		
		return map;
	}
	
	public Element createXmlElement(Document _xmlDoc) throws Exception{
		Element _xmlNotifyElem = _xmlDoc.createElement("mobilpay");
		Attr _attr = _xmlDoc.createAttribute("timestamp");
		Date d = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("YmdHis");
		_attr.setNodeValue(fmt.format(d));
		_xmlNotifyElem.appendChild(_attr);
		
		_attr = _xmlDoc.createAttribute("crc");
		//MessageDigest m=MessageDigest.getInstance("MD5");
		String s = ""+System.currentTimeMillis();
		this._crc = MD5.hash(s);
		//m.update(s.getBytes(),0,s.length());
		//this._crc = new BigInteger(1,m.digest()).toString(16);
		_attr.setNodeValue(this._crc);
		_xmlNotifyElem.appendChild(_attr);
		
		Element _elem = _xmlDoc.createElement("action");
		_elem.setNodeValue(this._action);
		_xmlNotifyElem.appendChild(_elem);
		
		if(this._customer != null){
			_xmlNotifyElem.appendChild(this._customer.createXMLElement(_xmlDoc, "customer"));
		}
		
		_elem = _xmlDoc.createElement("purchase");
		_elem.setNodeValue(this._purchaseId);
		_xmlNotifyElem.appendChild(_elem);
		
		if(this._originalAmount > 0) {
			_elem = _xmlDoc.createElement("original_amount");
			_elem.setNodeValue(""+this._originalAmount);
			_xmlNotifyElem.appendChild(_elem);
			
		}
		
		if(this._processedAmount > 0) {
			_elem = _xmlDoc.createElement("processed_amount");
			_elem.setNodeValue(""+this._processedAmount);
			_xmlNotifyElem.appendChild(_elem);
			
		}
		
		_elem = _xmlDoc.createElement("error");
		_attr = _xmlDoc.createAttribute("code");
		_attr.setNodeValue(""+this._errorCode);
		_elem.appendChild(_attr);
		_elem.appendChild(_xmlDoc.createCDATASection(this._errorMessage));
		_xmlNotifyElem.appendChild(_elem);
		
		return _xmlNotifyElem;
	}
	
	public String getCrc() { return this._crc;}
}

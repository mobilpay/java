package ro.mobilPay.payment;

import java.io.UnsupportedEncodingException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Address {
		public final String TYPE_COMPANY = "company";
	    public final String TYPE_PERSON = "person";
	    
	    public final int ERROR_INVALID_PARAMETER = 0x11100001;
	    public final int ERROR_INVALID_ADDRESS_TYPE = 0x11100002;
	    public final int ERROR_INVALID_ADDRESS_TYPE_VALUE = 0x11100003;

	    public String _type = null;
	    public String _firstName = null;
	    public String _lastName = null;
	    public String _fiscalNumber = null;
	    public String _identityNumber = null;
	    public String _country = null;
	    public String _county = null;
	    public String _city = null;
	    public String _zipCode = null;
	    public String _address = null;
	    public String _email = null;
	    public String _mobilePhone = null;
	    public String _bank = null;
	    public String _iban = null;
	    
	    public Address(Node n){
	    	if(n != null) {
	    		loadFromXML(n);
	    	}
	    }
	    
	    public Address () {
	    	
	    }
	    
	    protected boolean loadFromXML(Node n)  {
	    	//$attr = $elem->attributes->getNamedItem('type');
	    	Node tmpNode = null;
	    	NodeList tmpList = null;
	    	tmpNode = n.getAttributes().getNamedItem("type");
	    	if(tmpNode != null)
	    		this._type = tmpNode.getNodeValue();
	    	else
	    		this._type = this.TYPE_PERSON;
	    	
	    	try {
	    		tmpList = ((Element)n).getElementsByTagName("first_name");
		    	if(tmpList.getLength() == 1) {
		    		//System.out.println("tmplist0:"+tmpList.item(0).getNodeValue()+":"+tmpList.item(0).getTextContent()+":");
		    		this._firstName = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	}
		    	tmpList = ((Element)n).getElementsByTagName("last_name");
		    	if(tmpList.getLength() == 1)
		    		this._lastName = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	tmpList = ((Element)n).getElementsByTagName("fiscal_number");
		    	if(tmpList.getLength() == 1)
		    		this._fiscalNumber = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	tmpList = ((Element)n).getElementsByTagName("identity_number");
		    	if(tmpList.getLength() == 1)
		    		this._identityNumber = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");

		    	tmpList = ((Element)n).getElementsByTagName("country");
		    	if(tmpList.getLength() == 1)
		    		this._country = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");

		    	tmpList = ((Element)n).getElementsByTagName("county");
		    	if(tmpList.getLength() == 1)
		    		this._county = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");

		    	tmpList = ((Element)n).getElementsByTagName("city");
		    	if(tmpList.getLength() == 1)
		    		this._city = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");

		    	tmpList = ((Element)n).getElementsByTagName("zip_code");
		    	if(tmpList.getLength() == 1)
		    		this._zipCode = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	tmpList = ((Element)n).getElementsByTagName("address");
		    	if(tmpList.getLength() == 1)
		    		this._address = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	tmpList = ((Element)n).getElementsByTagName("email");
		    	if(tmpList.getLength() == 1)
		    		this._email = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	tmpList = ((Element)n).getElementsByTagName("mobile_phone");
		    	if(tmpList.getLength() == 1)
		    		this._mobilePhone = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	tmpList = ((Element)n).getElementsByTagName("bank");
		    	if(tmpList.getLength() == 1)
		    		this._bank = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	tmpList = ((Element)n).getElementsByTagName("iban");
		    	if(tmpList.getLength() == 1)
		    		this._iban = java.net.URLDecoder.decode(tmpList.item(0).getTextContent(),"UTF-8");
		    	
		    	
		    } catch (UnsupportedEncodingException e) {
		    	System.err.println(e.getMessage());
		    	return false;
		    }
	    	
	    	
	    	return true;
	    }
	    
	    public Element createXMLElement(Document d, String nodeName) throws Exception {
	    	if(!(d instanceof org.w3c.dom.Document)) {
	    		throw new Exception("ERROR "+this.ERROR_INVALID_PARAMETER);
	    	}
	    	if(this._type == null) {
	    		throw new Exception("ERROR TYPE "+this.ERROR_INVALID_ADDRESS_TYPE);
	    	}
	    	if(this._type != this.TYPE_COMPANY && this._type != this.TYPE_PERSON)
	    		throw new Exception("ERROR TYPE VALUE "+this.ERROR_INVALID_ADDRESS_TYPE_VALUE);
	    	

	    	Element elem = d.createElement(nodeName);
	    	elem.setAttribute("type", this._type);
	    		    	
	    	if(this._firstName != null) {
	    		Element xElem = d.createElement("first_name");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._firstName,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}

	    	if(this._lastName != null) {
	    		Element xElem = d.createElement("last_name");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._lastName,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._fiscalNumber != null) {
	    		Element xElem = d.createElement("fiscal_number");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._fiscalNumber,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._identityNumber != null) {
	    		Element xElem = d.createElement("identity_number");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._identityNumber,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._country != null) {
	    		Element xElem = d.createElement("country");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._country,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._county != null) {
	    		Element xElem = d.createElement("county");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._county,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._city != null) {
	    		Element xElem = d.createElement("city");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._city,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._zipCode != null) {
	    		Element xElem = d.createElement("zip_code");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._zipCode,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._address != null) {
	    		Element xElem = d.createElement("address");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._address,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._email != null) {
	    		Element xElem = d.createElement("email");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._email,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._mobilePhone != null) {
	    		Element xElem = d.createElement("mobile_phone");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._mobilePhone,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}

	    	if(this._bank != null) {
	    		Element xElem = d.createElement("bank");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._bank,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	if(this._iban != null) {
	    		Element xElem = d.createElement("iban");
	    		xElem.appendChild(d.createCDATASection(java.net.URLEncoder.encode(this._iban,"UTF-8")));
	    		elem.appendChild(xElem);
	    	}
	    	
	    	
	    	return elem;
	    }
	    
	    public String toString() {
	    	return 
	    		"[ppiFirstName="+this._firstName+"],"
	    		+ "[ppiLastName="+this._lastName+"],"	
	    		+ "[ppiCountry="+this._country+"],"	
	    		+ "[ppiCountry="+this._county+"],"
	    		+ "[ppiCity="+this._city+"],"	
	    		+ "[ppiPostalCode="+this._zipCode+"],"
	    		+ "[ppiAddress="+this._address+"],"
	    		+ "[ppiEmail="+this._email+"],"
	    		+ "[ppiPhone="+this._mobilePhone+"],"
	    		+ "[ppiBank="+this._bank+"],"
	    		+ "[ppiIBAN="+this._iban+"],"
	    		+ "[ppiFiscalNumber="+this._fiscalNumber+"],"
	    		+ "[ppiIdentityNumber="+this._identityNumber+"],"
	    			;
	    }
}

package ro.mobilPay.payment.request;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.HashMap;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ro.mobilPay.util.ListItem;
import ro.mobilPay.util.MD5;
import ro.mobilPay.util.OpenSSL;

public abstract class Abstract {
	protected static final String PAYMENT_TYPE_SMS	= "sms";
	protected static final String PAYMENT_TYPE_CARD	= "card";
	protected static final String PAYMENT_TYPE_TRANSFER	= "transfer";
	protected static final String PAYMENT_TYPE_INTERNET	= "homePay";
	protected static final String PAYMENT_TYPE_BITCOIN	= "bitcoin";
	
	public static final int CONFIRM_ERROR_TYPE_NONE		= 0x00;
	public static final int CONFIRM_ERROR_TYPE_TEMPORARY	= 0x01;
	public static final int CONFIRM_ERROR_TYPE_PERMANENT	= 0x02;

	private static final int ERROR_LOAD_X509_CERTIFICATE	= 0x10000001;
	private static final int ERROR_ENCRYPT_DATA			= 0x10000002;
	
	protected static final int ERROR_PREPARE_MANDATORY_PROPERTIES_UNSET	= 0x11000001;
	
	private static final int ERROR_FACTORY_BY_XML_ORDER_ELEM_NOT_FOUND			= 0x20000001;
	private static final int ERROR_FACTORY_BY_XML_ORDER_TYPE_ATTR_NOT_FOUND	= 0x20000002;
	private static final int ERROR_FACTORY_BY_XML_INVALID_TYPE					= 0x20000003;
	
	private final int ERROR_LOAD_FROM_XML_ORDER_ID_ATTR_MISSING			= 0x30000001;
	private final static int ERROR_LOAD_FROM_XML_SIGNATURE_ELEM_MISSING		= 0x30000002;
	
	private final static int ERROR_CONFIRM_LOAD_PRIVATE_KEY					= 0x300000f0;
	private final static int ERROR_CONFIRM_FAILED_DECODING_DATA				= 0x300000f1;
	private final static int ERROR_CONFIRM_FAILED_DECODING_ENVELOPE_KEY		= 0x300000f2;
	private final static int ERROR_CONFIRM_FAILED_DECRYPT_DATA					= 0x300000f3;
	private final static int ERROR_CONFIRM_INVALID_POST_METHOD					= 0x300000f4;
	private final static int ERROR_CONFIRM_INVALID_POST_PARAMETERS				= 0x300000f5;
	public final static int ERROR_CONFIRM_INVALID_ACTION						= 0x300000f6;

	private static final int VERSION_QUERY_STRING	= 0x01;
	private static final int VERSION_XML			= 0x02;	
	
	public String _signature = null;
	public String _service = null;
	public String _orderId = null;
	public long _timestamp = 0;
	public String _type = PAYMENT_TYPE_SMS;
	
	public String _returnUrl = null;
	public String _confirmUrl = null;
	public String _cancelUrl = null;
	
	public HashMap<String,String> _objRequestParams = new HashMap<String,String>();
	
	private String _outEnvKey = null;
	private String _outEncData = null;

	protected Document _xmlDoc	= null;
	
	protected String _requestIdentifier	= null;
	
	protected RequestInfo _objRequestInfo	= null;
	public Notify _objReqNotify		= null;
	abstract protected void _prepare() throws Exception;
	abstract protected boolean _loadFromXml(Element _elem) throws Exception;

	public Abstract() {
		
		try {
			this._requestIdentifier = MD5.hash(""+(int)(Math.random()*1000000));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Abstract factory(String data) {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc = null;
        InputSource is = null;
		try {
			docBuilder = dbfac.newDocumentBuilder();
			is = new InputSource(new StringReader(data));
			doc = docBuilder.parse(is);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Abstract a=null;
		if(doc != null){
			//parsing data has failed
			
			try {
				a = Abstract.__factoryFromXml(doc);
				if(a != null)
					a._setRequestInfo(Abstract.VERSION_XML,data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else {
			a = Abstract.__factoryFromQueryString(data);
			a._setRequestInfo(Abstract.VERSION_QUERY_STRING, data);
		}
        
        
		return a;
	}
	
	
	protected static Abstract __factoryFromXml(Document _xmlDoc) throws Exception {
		Abstract aNew;
		if(_xmlDoc == null)
			return null;
		NodeList nList = _xmlDoc.getElementsByTagName("order");
		if(nList.getLength() != 1) {
			throw new Exception("factoryFromXml order element not found "+ Abstract.ERROR_FACTORY_BY_XML_ORDER_ELEM_NOT_FOUND);
		}
		
		Node orderElem = nList.item(0);
		Node attrs = orderElem.getAttributes().getNamedItem("type");
		if(attrs == null || attrs.getNodeValue().length() == 0) {
			throw new Exception("factoryFromXml invalid payment request type="+ attrs.getNodeValue() +" "+ Abstract.ERROR_FACTORY_BY_XML_ORDER_TYPE_ATTR_NOT_FOUND);

		}
		if(attrs.getNodeValue().compareToIgnoreCase(Abstract.PAYMENT_TYPE_SMS)==0) {
			aNew = new ro.mobilPay.payment.request.SMS();
		} else if (attrs.getNodeValue().compareToIgnoreCase(Abstract.PAYMENT_TYPE_CARD)==0) {
			aNew = new ro.mobilPay.payment.request.Card();
		} else if (attrs.getNodeValue().compareToIgnoreCase(Abstract.PAYMENT_TYPE_TRANSFER)==0) {
			aNew = new ro.mobilPay.payment.request.Transfer();
		} else {
			throw new Exception("factoryFromXml invalid payment request type=" + attrs.getNodeValue() +" "+ Abstract.ERROR_FACTORY_BY_XML_INVALID_TYPE);
		}
		aNew._loadFromXml((Element) orderElem);
		return aNew;
	}
	
	protected static Abstract __factoryFromQueryString(String _data) {
		System.err.println("__factoryFromQueryString not implemented");
		return null;
	}
	
	static public Abstract factoryFromEncrypted(String _envKey, String _encData, String _privateKey) throws Exception{
		
		//String prvkey=null;
		
		String data = OpenSSL.openssl_unseal(_encData, _envKey, _privateKey);
		System.out.println("data is :"+data);
		return Abstract.factory(data);
	}
	
	protected void _setRequestInfo(int _reqVersion, String _reqData)
	{
		if(this._objRequestInfo == null) {
			_objRequestInfo = new Abstract.RequestInfo(_reqVersion, _reqData);
		} else {
			_objRequestInfo.setRequestInfo(_reqVersion, _reqData);
		}

	}
	
	protected boolean _parseFromXml(Element _elem) throws Exception {
		Node _xmlAttr = _elem.getAttributes().getNamedItem("id");
		if(_xmlAttr == null || _xmlAttr.getNodeValue().length() == 0 )
			throw new Exception("Mobilpay_Payment_Request_Abstract::_parseFromXml failed: empty order id " +  this.ERROR_LOAD_FROM_XML_ORDER_ID_ATTR_MISSING);
		this._orderId = _xmlAttr.getNodeValue();
		NodeList elems = _elem.getElementsByTagName("signature");
		if(elems.getLength() != 1)
			throw new Exception("Mobilpay_Payment_Request_Abstract::loadFromXml failed: signature is missing" + Abstract.ERROR_LOAD_FROM_XML_SIGNATURE_ELEM_MISSING);
		else
			System.out.println("got 1 <signature> tag, assigning to class member");
		Node _xmlElem = elems.item(0);
		this._signature = _xmlElem.getNodeValue();
		if(this._signature == null || this._signature.isEmpty())
		{
			System.out.println("_signature GNV failed, trying alternative");
			this._signature = _xmlElem.getTextContent();
			System.out.println("_signature set to:"+this._signature);		
		}
		
		elems = _elem.getElementsByTagName("url");
		if(elems.getLength() == 1) {
			_xmlElem = elems.item(0);
			elems = _xmlElem.getChildNodes();
			String name, value;
			for(int i=0; i<elems.getLength(); i++) {
				name = elems.item(i).getNodeName();
				value = elems.item(i).getNodeValue();
				if(name.compareToIgnoreCase("return")==0)
					this._returnUrl = value;
				else if (name.compareToIgnoreCase("confirm")==0)
					this._confirmUrl = value;
				else if (name.compareToIgnoreCase("cancel")==0)
					this._cancelUrl = value;
			}
		}
		
		//HashMap<String,String> params = new HashMap<String,String>();
		NodeList paramElems = _elem.getElementsByTagName("params");
		if(paramElems.getLength() == 1){
			paramElems = paramElems.item(0).getChildNodes(); //should get all the <param> nodes inside <params>
			for(int i=0;i<paramElems.getLength();i++) { // for each param node
				AbstractMap.SimpleEntry<String, String> e = _parseNameValue(paramElems.item(i));//get the key (sent as <name>) and value (sent as <value>)
				if(e != null)
					_objRequestParams.put(e.getKey(), e.getValue());
			}
		}
		elems = _elem.getElementsByTagName("mobilpay");
		if(elems.getLength() == 1) {
			this._objReqNotify = new Notify();
			this._objReqNotify.loadFromXML((Element) elems.item(0));
		}
			
		return true;
	}
	
	protected AbstractMap.SimpleEntry<String, String> _parseNameValue(Node n) {
		String strName = "<empty>",strValue = "<empty>";
		NodeList nl = n.getChildNodes();
		for(int i=0;i<nl.getLength();i++) {
			String name = nl.item(i).getNodeName();
			String value = nl.item(i).getTextContent();
			//System.out.println("name is "+name+" and value is "+value);
			if(name.compareToIgnoreCase("name")==0) {
				strName = value;
			} else if (name.compareToIgnoreCase("value")==0 && value != null) {
				try {
					//System.out.println("Decoding:"+value);
					strValue = java.net.URLDecoder.decode(value,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("name is "+strName+" and value is "+strValue);
		return new AbstractMap.SimpleEntry<String,String>(strName, strValue);
		
	}

	public String getEnvKey()
	{
		return this._outEnvKey;
	}

	public String getEncData()
	{
		return this._outEncData;
	}
	
	public String getRequestIdentifier()
	{
		return this._requestIdentifier;
	}
	
	public boolean __isset(String _name)
    {
        return (this._objRequestParams !=null) && (this._objRequestParams.get(_name)!=null);
    }

    public void __set(String _name, String _value)
    {
        this._objRequestParams.put(_name, _value);
    }
    
    public String __get(String _name)
    {
        if (!__isset(_name))
        {
        	return null;
        }
        
        return this._objRequestParams.get(_name);
    }
	
	public class RequestInfo {
		int _reqVersion;
		String _reqData;
		 
		public RequestInfo(int v, String d) {
			_reqVersion = v;
			_reqData = d;	
		}
		
		public void setRequestInfo(int v, String d) {
			_reqVersion = v;
			_reqData = d;
		}
	}
	
	public ListItem encrypt(String _certificateAsString) throws Exception{
		this._prepare();
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(this._xmlDoc);
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		transformer.transform(source, result);
		writer.close();
		String srcData = writer.toString();
		//System.out.println("srcData:"+srcData);
		return OpenSSL.openssl_seal(_certificateAsString, srcData);
	}

	
}

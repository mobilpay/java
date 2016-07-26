package ro.mobilPay.payment.invoice;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Item {

	private static final int ERROR_INVALID_PARAMETER		= 0x11111001;
	private static final int ERROR_INVALID_PROPERTY			= 0x11110002;
	
	private static final int ERROR_LOAD_FROM_XML_CODE_ELEM_MISSING		= 0x40000001;
	private static final int ERROR_LOAD_FROM_XML_NAME_ELEM_MISSING		= 0x40000002;
	private static final int ERROR_LOAD_FROM_XML_QUANTITY_ELEM_MISSING	= 0x40000003;
	private static final int ERROR_LOAD_FROM_XML_QUANTITY_ELEM_EMPTY	= 0x40000004;
	private static final int ERROR_LOAD_FROM_XML_PRICE_ELEM_MISSING		= 0x40000005;
	private static final int ERROR_LOAD_FROM_XML_PRICE_ELEM_EMPTY		= 0x40000006;
	private static final int ERROR_LOAD_FROM_XML_VAT_ELEM_MISSING		= 0x40000007;
	
	public String _code			= null;
	public String _name			= null;
	public String _measurment	= null;
	public int _quantity		= 0;
	public double _price		= 0;
	public double _vat			= 0;

	
	public Item(Node item) throws Exception {
		if(item != null) 
			this.loadFromXml((Element)item);
	}

	protected boolean loadFromXml(Element item) throws Exception{
		NodeList elems = item.getElementsByTagName("code");
		if(elems.getLength() != 1){
			throw new Exception("Mobilpay_Payment_Invoice_Item::loadFromXml failed! Invalid code element."+ Item.ERROR_LOAD_FROM_XML_CODE_ELEM_MISSING);	
		}
		this._code = java.net.URLDecoder.decode(elems.item(0).getNodeValue(),"UTF-8");
		elems = item.getElementsByTagName("name");
		if(elems.getLength() != 1){
			throw new Exception("Mobilpay_Payment_Invoice_Item::loadFromXml failed! Invalid name element."+ Item.ERROR_LOAD_FROM_XML_NAME_ELEM_MISSING);	
		}
		this._name = java.net.URLDecoder.decode(elems.item(0).getNodeValue(),"UTF-8");

		elems = item.getElementsByTagName("measurment");
		if(elems.getLength() == 1){
			this._measurment = java.net.URLDecoder.decode(elems.item(0).getNodeValue(),"UTF-8");
		}
		
		elems = item.getElementsByTagName("quantity");
		if(elems.getLength() != 1){
			throw new Exception("Mobilpay_Payment_Invoice_Item::loadFromXml failed! Invalid quantity element."+ Item.ERROR_LOAD_FROM_XML_QUANTITY_ELEM_MISSING);	
		}
		this._quantity = Integer.parseInt(java.net.URLDecoder.decode(elems.item(0).getNodeValue(),"UTF-8"));
		if(this._quantity <= 0) {
			throw new Exception("Mobilpay_Payment_Invoice_Item::loadFromXml failed! Invalid quantity value=" +this._quantity+ " " +Item.ERROR_LOAD_FROM_XML_QUANTITY_ELEM_EMPTY);

		}
		
		elems = item.getElementsByTagName("price");
		if(elems.getLength() != 1){
			throw new Exception("Mobilpay_Payment_Invoice_Item::loadFromXml failed! Invalid quantity element."+ Item.ERROR_LOAD_FROM_XML_PRICE_ELEM_MISSING);	
		}
		this._price = Double.parseDouble(java.net.URLDecoder.decode(elems.item(0).getNodeValue(),"UTF-8"));
		if(this._price < 0) {
			throw new Exception("Mobilpay_Payment_Invoice_Item::loadFromXml failed! Invalid quantity value=" +this._quantity+ " " +Item.ERROR_LOAD_FROM_XML_PRICE_ELEM_EMPTY);

		}
		
		elems = item.getElementsByTagName("vat");
		if(elems.getLength() != 1){
			throw new Exception("Mobilpay_Payment_Invoice_Item::loadFromXml failed! Invalid quantity element."+ Item.ERROR_LOAD_FROM_XML_VAT_ELEM_MISSING);	
		}
		this._vat = Double.parseDouble(java.net.URLDecoder.decode(elems.item(0).getNodeValue(),"UTF-8"));
		
		
		return true;
	}
	
	public Element createXmlElement(Document _xmlDoc) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getTotalAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}

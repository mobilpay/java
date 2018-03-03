package ro.mobilPay.payment;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.mobilPay.payment.invoice.Item;

public class Invoice {
	
	private static final int ERROR_INVALID_PARAMETER			= 0x11110001;
	private static final int ERROR_INVALID_CURRENCY			= 0x11110002;
	private static final int ERROR_ITEM_INSERT_INVALID_INDEX	= 0x11110003;
	private static final int ERROR_LOAD_FROM_XML_CURRENCY_ATTR_MISSING	= 0x31110001;

	public String _currency				= null;
	public double _amount				= 0;
	public String _details				= null;
	public int _installments			= 1;
	public int _selectedInstallments	= 1;
	
	
	protected Address _billingAddress	= null;
	protected Address _shippingAddress	= null;
	
	protected ArrayList<Item> _items			= null;
	protected ArrayList<ExchangeRate> _exchangeRates	= null;
	
	public Invoice (Element _elem) throws Exception {
		this.loadFromXml(_elem);
	}
	
	public Invoice ()  {
		
	}

	protected boolean loadFromXml(Element _elem) throws Exception {
		Node attr = _elem.getAttributes().getNamedItem("currency");
		if(attr == null)
			throw new Exception("Mobilpay_Payment_Invoice::loadFromXml failed; currency attribute missing " + Invoice.ERROR_LOAD_FROM_XML_CURRENCY_ATTR_MISSING); 

		this._currency = new String (attr.getNodeValue());
		
		attr = _elem.getAttributes().getNamedItem("amount");
		if(attr != null)
			this._amount = Double.parseDouble(attr.getNodeValue());
		
		attr = _elem.getAttributes().getNamedItem("installments");
		if(attr != null)
			this._installments = Integer.parseInt(attr.getNodeValue());
		
		attr = _elem.getAttributes().getNamedItem("selected_installments");
		if(attr != null)
			this._selectedInstallments = Integer.parseInt(attr.getNodeValue());
		
		
		NodeList elems = _elem.getElementsByTagName("details");
		if(elems.getLength() == 1) {
			this._details = new String(elems.item(0).getTextContent());
		}
		
		elems = _elem.getElementsByTagName("contact_info");
		if(elems.getLength() == 1) {
			Element addrElem = (Element)elems.item(0);
			if(addrElem != null) {
				elems = addrElem.getElementsByTagName("billing");
				if(elems.getLength() == 1){
					this._billingAddress = new Address(elems.item(0));
				}
				elems = addrElem.getElementsByTagName("shipping");
				if(elems.getLength() == 1) {
					this._shippingAddress = new Address(elems.item(0));
				}
			}
		}
		
		this._items = new ArrayList<Item>();
		elems = _elem.getElementsByTagName("items");
		if(elems.getLength() == 1) {
			Element itemElems = (Element)elems.item(0);
			elems = itemElems.getElementsByTagName("item");
			if(elems.getLength() > 0) {
				double amount = 0;
				for(int i=0;i<elems.getLength();i++){
					Item item = new Item(elems.item(i));
					this._items.add(item);
					amount += item.getTotalAmount();
				}
				this._amount = amount;
			}
		}
		this._exchangeRates = new ArrayList<ExchangeRate>();
		elems = _elem.getElementsByTagName("exchange_rates");
		if(elems.getLength() == 1) {
			Element rateElems = (Element)elems.item(0);
			elems = rateElems.getElementsByTagName("rate");
			if(elems.getLength() > 0) {
				for(int i=0;i<elems.getLength();i++){
					ExchangeRate rate = new ExchangeRate(elems.item(i));
					this._exchangeRates.add(rate);
				}
			}
		}
		return true;
	}

	public Element createXMLElement(Document _xmlDoc) throws Exception {
		Element xmlInvElem = _xmlDoc.createElement("invoice");
		if(this._currency == null) {
			throw new Exception("Invalid currency" + Invoice.ERROR_INVALID_CURRENCY);
		}

		xmlInvElem.setAttribute("currency", this._currency);
		if(this._amount > 0) {
			xmlInvElem.setAttribute("amount",String.format(java.util.Locale.US,"%.02f", this._amount));

				
		}
		
		if(this._installments > 0) {
			xmlInvElem.setAttribute("installments",""+this._installments);
			
		}
		
		if(this._selectedInstallments > 0) {
			xmlInvElem.setAttribute("selected_installments",""+this._selectedInstallments);

			
		}
		Element xmlElem,xmlAddr;
		
		if(this._details != null) {
			xmlElem = _xmlDoc.createElement("details");
			xmlElem.appendChild(_xmlDoc.createCDATASection(java.net.URLEncoder.encode(this._details, "UTF-8")));
			xmlInvElem.appendChild(xmlElem);
		}
		xmlAddr = _xmlDoc.createElement("contact_info");
		if(this._billingAddress != null) {
			xmlElem = this._billingAddress.createXMLElement(_xmlDoc, "billing");
			xmlAddr.appendChild(xmlElem);
		}
		
		if(this._shippingAddress != null) {
			xmlElem = this._shippingAddress.createXMLElement(_xmlDoc, "shipping");
			xmlAddr.appendChild(xmlElem);
		}
		xmlInvElem.appendChild(xmlAddr);
		
		if(this._items != null && this._items.size() > 0) {
			Element xmlItems = _xmlDoc.createElement("items");
			Item[] items = (Item[])this._items.toArray();
			for(int i=0; i< items.length; i++) {
				Element xmlItem = ((Item)items[i]).createXmlElement(_xmlDoc);
				xmlItems.appendChild(xmlItem);
			}
			xmlInvElem.appendChild(xmlItems);
		}
		
		
		
		
		return xmlInvElem;
	}
	
	public void setBillingAddress(Address _address)
	{
		this._billingAddress = _address;
		
	}
	
	public void setShippingAddress(Address _address)
	{
		this._shippingAddress = _address;
		
	}
	
	public Address getBillingAddress()
	{
		return this._billingAddress;
		
	}
	
	public Address getShippingAddress()
	{
		return this._shippingAddress ;
		
	}
	
	public void addItem(Item i) {
		if(this._items == null)
			return;
		this._items.add(i);
	}
	
	public void addExchangeRate(ExchangeRate i){
		if(this._exchangeRates == null)
			return;
		this._exchangeRates.add(i);
		
	}

}

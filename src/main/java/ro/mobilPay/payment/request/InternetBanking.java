package ro.mobilPay.payment.request;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ro.mobilPay.payment.Invoice;

public class InternetBanking extends Abstract {
	private final int ERROR_LOAD_FROM_XML_ORDER_INVOICE_ELEM_MISSING	= 0x30000001;
	public Invoice _invoice = null;
	
	public InternetBanking() {
		this._type = Abstract.PAYMENT_TYPE_TRANSFER;
	}

	@Override
	protected void _prepare() throws Exception {
		if(this._signature == null || this._orderId == null || !(this._invoice instanceof Invoice))
			throw new Exception("One or more mandatory properties are invalid!" + ERROR_PREPARE_MANDATORY_PROPERTIES_UNSET+":"+this._signature+":"+this._orderId);

		DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		this._xmlDoc = builder.newDocument();
		Element rootElem = this._xmlDoc.createElement("order");
		//this._xmlDoc.appendChild(rootElem);
		//set payment type attribute
		
		rootElem.setAttribute("type", this._type);
		//set id attribute
		rootElem.setAttribute("id",this._orderId);
		//set timestamp attribute
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		rootElem.setAttribute("timestamp",formatter.format(new Date()));

		//set signature
		Element xmlElem =  this._xmlDoc.createElement("signature");
		xmlElem.setTextContent(this._signature);
		rootElem.appendChild(xmlElem);
		//set invoice
		xmlElem = this._invoice.createXMLElement(this._xmlDoc);
		rootElem.appendChild(xmlElem);
		
		if(this._objRequestParams != null && !this._objRequestParams.isEmpty()){
			Element xmlParams = this._xmlDoc.createElement("params");
			for (String key: this._objRequestParams.keySet()) {
				Element xmlParam = this._xmlDoc.createElement("param");
				Element xmlName = this._xmlDoc.createElement("name");
				Element xmlValue = this._xmlDoc.createElement("value");
				//xmlName.setNodeValue(key);
				xmlName.setTextContent(key);
				xmlValue.appendChild(this._xmlDoc.createCDATASection(this._objRequestParams.get(key)));
				xmlParam.appendChild(xmlName);
				xmlParam.appendChild(xmlValue);
				xmlParams.appendChild(xmlParam);
			}
			rootElem.appendChild(xmlParams);
		}
		
		if(this._returnUrl != null && !this._returnUrl.isEmpty()) {
			Element xmlURL = this._xmlDoc.createElement("url");
			xmlElem = this._xmlDoc.createElement("return");
			xmlElem.setTextContent(this._returnUrl);
			xmlURL.appendChild(xmlElem);
			if(this._confirmUrl != null && !this._confirmUrl.isEmpty()) {
				xmlElem = this._xmlDoc.createElement("confirm");
				xmlElem.setTextContent(this._confirmUrl);
				xmlURL.appendChild(xmlElem);				
			}
			rootElem.appendChild(xmlURL);
			
		}
		this._xmlDoc.appendChild(rootElem);

	}

	@Override
	protected boolean _loadFromXml(Element _elem) throws Exception {
		try {
			super._parseFromXml(_elem);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		NodeList elems = _elem.getElementsByTagName("invoice");
		if(elems.getLength() != 1) {
			throw new Exception("Mobilpay_Payment_Request_Card::loadFromXml failed; invoice element is missing "+ this.ERROR_LOAD_FROM_XML_ORDER_INVOICE_ELEM_MISSING);
		}
		this._invoice = new Invoice((Element) elems.item(0));
		return true;
		
	}

}

package ro.mobilPay.payment.request;

import org.w3c.dom.Element;


public class SMS extends Abstract {
	private static final int ERROR_LOAD_FROM_XML_SERVICE_ELEM_MISSING		= 0x31000001;
	public String _msisdn		= null;
	
	public SMS () {
		this._type = Abstract.PAYMENT_TYPE_SMS;
	}
	@Override
	protected void _prepare() throws Exception {
		// TODO Auto-generated method stub
		throw new Exception ("SMS::_prepare() not implemented");
	}

	@Override
	protected boolean _loadFromXml(Element _elem) throws Exception {
		throw new Exception ("SMS::_loadFromXml() not implemented");
	}

}

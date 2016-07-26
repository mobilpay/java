package ro.mobilPay.payment;

import org.w3c.dom.Node;

public class ExchangeRate {
	public String _from;
	public String _to;
	public double _rate;
	
	public ExchangeRate (String f,String t, double r) {
		_from = f;
		_to = t;
		_rate = r;
	}

	public ExchangeRate(Node item) {
		// TODO Auto-generated constructor stub
	}

}

package com.sesnu.orion.web.service;

import java.util.Map;

import org.json.simple.JSONObject;

import com.sesnu.orion.web.model.InvoiceFormat;

public enum InvoiceEnum {
	
	PROFORMA_INVOICE{
		public String processInvoice(InvoiceFormat format,JSONObject param,Map<String,String> data){
			String invoice = format.getFormat();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				invoice = invoice.replace(entry.getKey(), entry.getValue());
			}
			return invoice;
		}
	},
	
	COMMERCIAL_INVOICE{
		public String processInvoice(InvoiceFormat format,JSONObject param,Map<String,String> data){
			String invoice = format.getFormat();
			return invoice;
		}
	},
	
	PACKING_LIST{
		public String processInvoice(InvoiceFormat format,JSONObject param,Map<String,String> data){
			String invoice = format.getFormat();
			return invoice;
		}
	};
	
	public String processInvoice(InvoiceFormat format,JSONObject param,Map<String,String> data){
		return null;
	}

}

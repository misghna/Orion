package com.sesnu.orion.web.service;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sesnu.orion.dao.AddressBookDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.model.InvoiceFormat;
import com.sesnu.orion.web.model.OrderView;

@Service
public class InvoiceProcessor {

	@Autowired private AddressBookDAO addressBook;
	@Autowired private OrderDAO orderDao;
	
	
		public String processInvoice(InvoiceFormat format,JSONObject param){
			
			switch(param.get("invType").toString()){
			case "Proforma Invoice":
				return processInvoice(format, getPIData(param));
			case "Commericial Invoice":
				return processInvoice(format, getCIData(param));
			case "Packing List":
				return processInvoice(format, getPLData(param));
			default :
					return null;				
			}
			
		}
		
		private Map<String,String> getPLData(JSONObject param){

			return null;
		}
		
		private Map<String,String> getCIData(JSONObject param){
			
			return null;
		}
		
		private Map<String,String> getPIData(JSONObject param){
			Map<String,String> data = new HashMap<String,String>();
			AddressBook exAdd = addressBook.getByName(param.get("exporter").toString());
			data.put("EXPORTER_COMPANY_NAME",exAdd.getName());
			data.put("EXPORTER_ADDRESS", exAdd.getAddress() + " <br> " + exAdd.getCity() + "," +  exAdd.getCountry());
			data.put("EXPORTER_PHONE", "Tel. " + exAdd.getPhone());
			
			OrderView order = orderDao.getOrderByInvNo(param.get("invNo").toString());
			AddressBook imAdd = addressBook.getByName(order.getImporter());
			data.put("IMPORTER_NAME", imAdd.getName());
			data.put("IMPORTER_ADDRESS", imAdd.getAddress() + " <br> " + imAdd.getCity() + "," +  imAdd.getCountry());
			data.put("IMPORTER_PHONE", "Tel. " + imAdd.getPhone());
			
			data.put("COUNTRY_OF_ORIGIN", "Unknown");
			data.put("PORT_OF_LOADING", "Unknown");
			data.put("DESTINATION_COUNTRY", "Angola");
			data.put("PORT_OF_DISCHARGE", order.getDestinationPort());
			
			data.put("ITEM_NAME_BRAND", order.getItem() + ", " + order.getBrand());
			data.put("ITEM_PACKING", "Packing " + order.getBaseSize() + "" + order.getBaseUnit() + "X" + order.getQtyPerPack());
			data.put("ITEM_PSC_PER_CONTAINER", order.getPckPerCont() + "pck/Container");
			data.put("CONTAINERS_DETAIL", order.getContQnt() + "X" + order.getContSize() + "' Containers");
			int totalPacks = order.getQtyPerPack() * order.getPckPerCont() * order.getContQnt() ;
			data.put("UNIT_QUANTITY", totalPacks + "(packs)");
			
			data.put("UNIT_PRICE", "42.82");
			data.put("TOTAL_PRICE", "72,724.00");
			data.put("CONTAINERS_QTY", "2");
			data.put("UNIT_FREIGHT_COST", "1,200");
			data.put("TOTAL_FREIGHT_COST", "2,400");
			data.put("GRAND_TOTAL", "75,194");					
			return data;
		}

		
		
		
		
		public String processPreviewInvoice(InvoiceFormat format,String type){
			
			switch(type){
				case "Proforma Invoice":
					return processInvoice(format, getPIPreviewData());
				case "Commericial Invoice":
					return processInvoice(format, getCIPreviewData());
				case "Packing List":
					return processInvoice(format, getPLPreviewData());
				default :
						return null;				
			}
		}
		
		
		
		
		
		
		
		private Map<String,String> getPLPreviewData(){
			return null;			
		}
		
		private Map<String,String> getCIPreviewData(){
			return null;
			
		}
		
		private Map<String,String> getPIPreviewData(){
			Map<String,String> data = new HashMap<String,String>();
			data.put("EXPORTER_COMPANY_NAME", "XYZ General Trading Inc");
			data.put("EXPORTER_ADDRESS", "1657 Arsenal Avenu <br> Barcelona, Italy");
			data.put("EXPORTER_PHONE", "Tel. (393) 128-7533, Fax (393) 638-7971");
			data.put("IMPORTER_NAME", "Anseba Lda ");
			data.put("IMPORTER_ADDRESS", "Rua Martin Luther King <br> Casa #52 Macculuso <br> Municipio de Ingombato <br> Luanda, Angola ");
			data.put("IMPORTER_PHONE", "Tel. (244) 9351-74019");
			data.put("COUNTRY_OF_ORIGIN", "South Africa");
			data.put("PORT_OF_LOADING", "Durban");
			data.put("DESTINATION_COUNTRY", "Angola");
			data.put("PORT_OF_DISCHARGE", "Luanda");
			data.put("ITEM_NAME_BRAND", "Laundry Soap 'Green Bar'");
			data.put("ITEM_PACKING", "Packing 1.5Kg/pcs ");
			data.put("ITEM_PSC_PER_CONTAINER", "20pcs/Container");
			data.put("CONTAINERS_DETAIL", "4 X 20' Containers");
			data.put("UNIT_QUANTITY", "1700(Bags)");
			data.put("UNIT_PRICE", "42.82");
			data.put("TOTAL_PRICE", "72,724.00");
			data.put("CONTAINERS_QTY", "2");
			data.put("UNIT_FREIGHT_COST", "1,200");
			data.put("TOTAL_FREIGHT_COST", "2,400");
			data.put("GRAND_TOTAL", "75,194");					
			return data;
		}
		
		
		private String processInvoice(InvoiceFormat format,Map<String,String> data){
			String invoice = format.getFormat();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				invoice = invoice.replace(entry.getKey(), entry.getValue());
			}
			return invoice;
		}
}

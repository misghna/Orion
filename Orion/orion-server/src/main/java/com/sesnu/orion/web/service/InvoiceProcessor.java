package com.sesnu.orion.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sesnu.orion.dao.AddressBookDAO;
import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.ContainerDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.model.ContainerView;
import com.sesnu.orion.web.model.InvoiceFormat;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.ShippingView;

@Service
public class InvoiceProcessor {

	@Autowired private AddressBookDAO addressBook;
	@Autowired private ContainerDAO contDao;
	@Autowired private ShippingDAO shipDao;
	
		public String processInvoice(InvoiceFormat format,JSONObject param,Order order){
			
			switch(param.get("invType").toString()){
			case "Proforma Invoice":
				return processInvoice(format, getPIData(param,order));
			case "Commercial Invoice":
				return processInvoice(format, getCIData(param,order));
			case "Packing List":
				List<ContainerView> containers = contDao.listByOrderId(order.getId());
				if(containers==null || containers.size()<1){
					return null;
				}
				StringBuffer cdl = new StringBuffer();
				for (ContainerView container : containers) {
					cdl.append("<tr><td>"+container.getPackQty()+"</td><td>"+container.getContNo()+"</td><td>" + container.getGrossWeight() + "</td></tr>");
				}
				Map<String,String> data = processGeneralInfo(param,order);
				String invHtml = processInvoice(format, data);
				invHtml = invHtml.replace("CONTAINER_DETAIL_LIST", cdl.toString());
				return invHtml;
			default :
					return null;				
			}
			
		}
		
//		private Map<String,String> getPLData(JSONObject param,OrderView order){
//						
//			Map<String,String> data = processGeneralInfo(param,order);
//			
//			
//			return data;
//		}
		
		private Map<String,String> getCIData(JSONObject param,Order order){
			Map<String,String> data = processGeneralInfo(param,order);
			
			data.put("UNIT_PRICE", order.getAdjFob().toString());
			Double total = (double) Math.round(order.getAdjFob() * order.getPckPerCont()*order.getContQnt());
			data.put("TOTAL_PRICE", total.toString());
			data.put("CONTAINERS_QTY", order.getContQnt().toString());
			Double freightCost = order.getAdjFreight()*order.getPckPerCont();
			data.put("UNIT_FREIGHT_COST", freightCost.toString());
			Double totalFreight = (double) Math.round(freightCost*order.getContQnt());
			data.put("TOTAL_FREIGHT_COST", totalFreight.toString());
			Double grandTotal = (total+totalFreight);
			data.put("GRAND_TOTAL", grandTotal.toString());	
			
			return data;
		}
		
		private Map<String,String> getPIData(JSONObject param,Order order){
			Map<String,String> data = processGeneralInfo(param,order);
			
			data.put("UNIT_PRICE", order.getAdjFob().toString());
			Double total = (double) Math.round(order.getAdjFob() * order.getPckPerCont()*order.getContQnt());
			data.put("TOTAL_PRICE", total.toString());
			data.put("CONTAINERS_QTY", order.getContQnt().toString());
			Double freightCost = order.getAdjFreight()*order.getPckPerCont();
			data.put("UNIT_FREIGHT_COST", freightCost.toString());
			Double totalFreight = (double) Math.round(freightCost*order.getContQnt());
			data.put("TOTAL_FREIGHT_COST", totalFreight.toString());
			Double grandTotal = (total+totalFreight);
			data.put("GRAND_TOTAL", grandTotal.toString());	
			
			return data;
		}

		
		private Map<String,String> processGeneralInfo(JSONObject param,Order order){
			Map<String,String> data = new HashMap<String,String>();
			AddressBook exAdd = addressBook.getByName(order.getExporter());
			data.put("EXPORTER_COMPANY_NAME",exAdd.getName());
			data.put("EXPORTER_ADDRESS", exAdd.getAddress() + " <br> " + exAdd.getCity() + "," +  exAdd.getCountry());
			data.put("EXPORTER_PHONE", "Tel. " + exAdd.getPhone());
			
			
			AddressBook imAdd = addressBook.getByName(order.getImporter());
			data.put("IMPORTER_NAME", imAdd.getName());
			data.put("IMPORTER_ADDRESS", imAdd.getAddress() + " <br> " + imAdd.getCity() + "," +  imAdd.getCountry());
			data.put("IMPORTER_PHONE", "Tel. " + imAdd.getPhone());
			ShippingView ship = shipDao.getByOrderId(order.getId());
//			if(ship!=null && ship.getBl()!=null){
//				ShippingView shipping = shipDao.listByBL(ship.getBl()).get(0);
//				data.put("COUNTRY_OF_ORIGIN", shipping.getItemOrigin());
//				data.put("PORT_OF_LOADING", shipping.getLoadingPort());
//			}else{
				data.put("COUNTRY_OF_ORIGIN", "N/A");
				data.put("PORT_OF_LOADING", "N/A");
//			}
			
			data.put("DESTINATION_COUNTRY", "Angola");
			data.put("PORT_OF_DISCHARGE", order.getDestinationPort());
			
			data.put("ITEM_NAME_BRAND", order.getItem() + ", " + order.getBrand());
			data.put("ITEM_PACKING", "Packing " + order.getBaseSize() + "" + order.getBaseUnit() + "X" + order.getQtyPerPack());
			data.put("ITEM_PSC_PER_CONTAINER", order.getPckPerCont() + "pck/Container");
			data.put("CONTAINERS_DETAIL", order.getContQnt() + "X" + order.getContSize() + "' Containers");
			int totalPacks = (int) Math.ceil(order.getQtyPerPack() * order.getPckPerCont() * order.getContQnt()) ;
			data.put("UNIT_QUANTITY", totalPacks + "(packs)");			
			data.put("NO_OF_CONTAINERS", order.getContQnt().toString());
			data.put("BILL_OF_LOADING",  ship!=null && ship.getBl()!=null?ship.getBl():"");

			List<ContainerView> containers = contDao.listByOrderId(order.getId());
			if(containers!=null && containers.size()>0){
				Double grossWeight = 0d; Integer totalQty =0;
				for (ContainerView containerView : containers) {
					grossWeight +=containerView.getGrossWeight();
					totalQty += containerView.getPackQty();
				}
				data.put("TOTAL_GROSS_WEIGHT", grossWeight.toString());
				data.put("TOTAL_PACK_QUANTITY", totalQty.toString());
			}
			return data;
			
		}
		
		private Map<String,String> getPreviewData(){
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
			data.put("TOTAL_GROSS_WEIGHT", "50300");
			data.put("TOTAL_PACK_QUANTITY", "9900");
			StringBuffer cdl = new StringBuffer();
			cdl.append("<tr><td>3300</td><td>CN58957902</td><td>25100</td></tr>");
			cdl.append("<tr><td>3300</td><td>CN58957869</td><td>25100</td></tr>");
			cdl.append("<tr><td>3300</td><td>CN58957785</td><td>25100</td></tr>");
			data.put("CONTAINER_DETAIL_LIST", cdl.toString());
			
			return data;
		}
		
		
		
		public String processPreviewInvoice(InvoiceFormat format,String type){
			
			switch(type){
				case "Proforma Invoice":
					return processInvoice(format, getPreviewData());
				case "Commercial Invoice":
					return processInvoice(format, getPreviewData());
				case "Packing List":
					return processInvoice(format, getPreviewData());
				default :
						return null;				
			}
		}
					
		
		private String processInvoice(InvoiceFormat format,Map<String,String> data){
			String invoice = format.getFormat();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				invoice = invoice.replace(entry.getKey(), entry.getValue());
			}
			return invoice;
		}
}

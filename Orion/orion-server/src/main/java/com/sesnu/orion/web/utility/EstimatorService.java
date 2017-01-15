package com.sesnu.orion.web.utility;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sesnu.orion.dao.ExchangeDAO;
import com.sesnu.orion.dao.MiscSettingDAO;
import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Estimate;
import com.sesnu.orion.web.model.Exchange;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.model.PortFee;
import com.sesnu.orion.web.model.ShippingView;

@SuppressWarnings("unchecked")
@Component
public class EstimatorService {

	
	@Autowired private ShippingDAO shipDao;
	@Autowired private PortFeeDAO portFeeDAO;
	@Autowired private MiscSettingDAO miscDao;
	@Autowired private ExchangeDAO exchangeDao;
	
	public Estimate totalEstimate(OrderView order, Payment pay, Bid bid,Item item) {
		Estimate est =null;
		JSONObject pd = new JSONObject();
		double total =0;
		
		// legalization
		est = legalization(order,pay);
		total += est.getValue();
		pd.put("Legalization", est.getDetails());
		
		// license
		est = license(order.getContQnt());
		total += est.getValue();
		pd.put("License", est.getDetails());

		// customs
		est = customs(bid.getTotalBid(),bid.getCurrency(), item);
		total += est.getValue();
		pd.put("Customs", est.getDetails());
		
		// bromangol
		est = bromangol(order.getContQnt(),order.getBaseSize());
		total += est.getValue();
		pd.put("Bromangol", est.getDetails());
		
		// transport
		est = transport(order.getContQnt(),order.getBaseSize());
		total += est.getValue();
		pd.put("Transport", est.getDetails());
		
		// terminal
		est = terminal(order.getContQnt(),order.getBaseSize(),order.getTerminal());
		total += est.getValue();
		pd.put("Terminal", est.getDetails());
		
		// port
		est = port(order.getContQnt(),order.getBaseSize());
		total += est.getValue();
		pd.put("Port", est.getDetails());
		
		// certificateOfQuality
		est = certificateOfQuality(order.getContQnt(),order.getBaseSize());
		total += est.getValue();
		pd.put("Certificate Of Quality", est.getDetails());
		
		// certificateOfHealth
		est = certificateOfHealth(order.getContQnt(),order.getBaseSize());
		total += est.getValue();
		pd.put("Certificate Of Quality", est.getDetails());
				
		// phytosanitary
		est = phytosanitary();
		total += est.getValue();
		pd.put("Certificate Of Quality", est.getDetails());

		// forwardingAgent
		est = forwardingAgent(bid.getTotalBid(),bid.getCurrency());
		total += est.getValue();
		pd.put("Forwarding Agent", est.getDetails());
		
		
		return  new Estimate(total,pd);
	}
	
	
	
	public Estimate legalization(OrderView order, Payment pay){
		double total=0;JSONObject pd = new JSONObject();
		PortFee portFees = null;
		
		ShippingView ship = null;
		List<ShippingView> shipings = shipDao.listByOrderId(order.getId());
		if(shipings.size()>0){
			ship = shipings.get(0);
		}
		
		if(ship==null){
			portFees = portFeeDAO.listAll().get(0);
		}else{
			portFees = portFeeDAO.getByName(ship.getShipAgency());
		}
		
		total += portFees.getLegalizationFee();		// add legalization
		pd.put("Legalization", portFees.getLegalizationFee());
		Integer contQty = order.getContQnt();
		Integer contSize = order.getContSize();
		total += portFees.getContLiftFee() * contQty; // add container lift fee
		pd.put("Container Lift", portFees.getContLiftFee() * contQty);
		
		if (((pay!=null && pay.getDeposit()==0) || pay==null)  &&  contSize==20){
			total += portFees.getDepositCont20()* contQty; // container charge for 20
			pd.put("Container service Charge", portFees.getDepositCont20()* contQty);
		}else if (((pay!=null && pay.getDeposit()==0) || pay==null)  &&  contSize==40){
			total += portFees.getDepositCont40()* contQty; // container charge for 40
			pd.put("Container service Charge", portFees.getDepositCont40()* contQty);
		}
		
		pd.put("Consumer Tax",  portFees.getConsumerTax()/100 * total);
		total += portFees.getConsumerTax()/100 * total;  // add consumer tax

		return new Estimate(total,pd);

	}
	
	
	public Estimate customs(Double totalCnfValue,String currency,Item item){
		System.out.println("**************** currency " + currency);
		Exchange cur = exchangeDao.get("Customs", "Custom",currency, "AOA");
		if(cur==null){
			cur = exchangeDao.get("Other", "Other",currency, "AOA");
			if(cur==null){
				return null;
			}
		}

		double total=0;JSONObject pd = new JSONObject();		
		double totalTaxPercent = item.getFinancialServices() + item.getStampTax() + 
								 item.getConsumerTax() + item.getFees() + item.getOthers();
	
		total = totalCnfValue * totalTaxPercent * cur.getRate();
		pd.put("Financial Service", item.getFinancialServices() * totalCnfValue* cur.getRate());
		pd.put("Stamp tax",item.getStampTax()*totalCnfValue* cur.getRate());
		pd.put("Consumer tax",item.getConsumerTax()*totalCnfValue* cur.getRate());
		pd.put("Fees",item.getFees()*totalCnfValue* cur.getRate());
		pd.put("Others",item.getOthers()*totalCnfValue* cur.getRate());

		return new Estimate(total,pd);
	}
	
	public Estimate transport(Integer contQty,Integer contSize){
		double total=0;JSONObject pd = new JSONObject();
		if(contSize==20){
			total = Double.parseDouble(miscDao.getByName("Container Transport Fee 20ft(Est/cont-AOA)").getValue())*contQty;
		}else {
			total = Double.parseDouble(miscDao.getByName("Container Transport Fee 40ft(Est/cont-AOA)").getValue()) *contQty;
		}
		pd.put("Contaner Transport",total);
		return new Estimate(total,pd);
	}
	
	
	public Estimate terminal(Integer contQty,Integer contSize,String terminalName){
		Double rate =0d;double tmp=0;

		Exchange cur = exchangeDao.get("Terminal", terminalName,"USD", "AOA");
		if(cur!=null){
			rate = cur.getRate();
		}
		if(cur==null){
			rate = exchangeDao.getAvg("Terminal","USD", "AOA");
			if(rate==null){
				cur = exchangeDao.get("Other", "Other","USD", "AOA");
				if(cur==null){
					return null;
				}else{
					rate = cur.getRate();
				}
			}
		}
		
		double total=0;JSONObject pd = new JSONObject();
		if(contSize==20){
			tmp = Double.parseDouble(miscDao.getByName("Terminal Offloading tariff 20ft(Est/cont-USD)").getValue())*rate * contQty;
		}else {
			tmp = Double.parseDouble(miscDao.getByName("Terminal Offloading tariff 40ft(Est/cont-USD)").getValue())*rate * contQty;
		}	
		pd.put("Offloading Tariff",tmp);
		total +=tmp;
		
		tmp = Double.parseDouble(miscDao.getByName("Terminal TVA & Tax %(Est/bill-AOA)").getValue()) * tmp/100;
		pd.put("TVA & Tax",tmp);
		total +=tmp;
		
		tmp = Double.parseDouble(miscDao.getByName("Terminal Admin & service charge(Est/bill-AOA)").getValue());
		pd.put("Admin & service charge",tmp);
		total +=tmp;
		
		return new Estimate(total,pd);
	}
	
	
	public Estimate port(Integer contQty,Integer contSize){
		Exchange cur = exchangeDao.get("Port", "Port","USD", "AOA");
		if(cur==null){
			cur = exchangeDao.get("Other", "Other","USD", "AOA");
			if(cur==null){
				return null;
			}
		}
		double total=0;double temp=0;JSONObject pd = new JSONObject();
		temp = Double.parseDouble(miscDao.getByName("Port documentation and Copy fee(Est/bill-USD)").getValue());
		pd.put("documentation and Copy",temp*cur.getRate());
		total += temp;
		
		temp = Double.parseDouble(miscDao.getByName("Port Other Fee(Est/bill-USD)").getValue());
		pd.put("Other",temp*cur.getRate());
		total += temp;
			
		if(contSize==20){
			temp = Double.parseDouble(miscDao.getByName("Port expenses Fee 20ft(Est/Cont-USD)").getValue());
		}else {
			temp = Double.parseDouble(miscDao.getByName("Port expenses Fee 40ft(Est/Cont-USD)").getValue());
		}
		
		pd.put("expenses",temp*contQty*cur.getRate());
		total += temp;
		return new Estimate(total,pd);
	}
	
	
	public Estimate certificateOfQuality(Integer contQty,Integer contSize){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate of Quality fee(Est/cont-AOA)").getValue());
		pd.put("Certificate of Quality",total);
		return new Estimate(total,pd);
	}
	
	public Estimate certificateOfHealth(Integer contQty,Integer contSize){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate Of Health (Est/cont-AOA)").getValue());
		pd.put("Certificate of Health",total);
		return new Estimate(total,pd);
	}
	
	public Estimate bromangol(Integer contQty,Integer contSize){
		double total=0;double temp=0;JSONObject pd = new JSONObject();		
		Integer sampleQty = Integer.parseInt(miscDao.getByName("Bromangol samples(Est/bill-#)").getValue());
		temp = Double.parseDouble(miscDao.getByName("Bromangol Process Fee(Est/sample-AOA)").getValue()) * sampleQty;
		pd.put("Processing",temp);
		total += temp;
		
		temp = Double.parseDouble(miscDao.getByName("Bromangol PIC %(Est/bill-AOA)").getValue())*total/100;
		pd.put("PIC",temp);
		total += temp;
		
		temp = Double.parseDouble(miscDao.getByName("Bromangol Sample collection & prep Fee(Est/bill-AOA)").getValue());
		pd.put("Sample collection & prep",temp);
		total += temp;
		
		return new Estimate(total,pd);
	}
	
	
	public Estimate phytosanitary(){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate of Phytosanitary Fee(Est/bill-AOA)").getValue());
		pd.put("Phytosanitary",total);
		return new Estimate(total,pd);
	}
	
	public Estimate license(Integer contQty){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate of License fee(Est/cont-AOA)").getValue())*contQty;
		pd.put("Certificate of License",total);
		return new Estimate(total,pd);
	}
	
	public Estimate forwardingAgent(Double totalAmount,String currency){
		double total=0;JSONObject pd = new JSONObject();
		Exchange cur = exchangeDao.get("Customs", "Customs",currency, "AOA");
		if(cur==null){
			cur = exchangeDao.get("Other", "Other",currency, "AOA");
			if(cur==null){
				return null;
			}
		}
		total = Double.parseDouble(miscDao.getByName("Forwarding agent %(Est/bill-AOA)").getValue()) * cur.getRate()/100;
		pd.put("Forwarding agent",total);
		return new Estimate(total,pd);
	}
	
	public Estimate penality(Integer contQty,Integer contSize){
//		double total=0;JSONObject pd = new JSONObject();
//		if (daysInPort >0 && portFees.getContFreeDays() - daysInPort > 0){ // add penality if exists
//			total += portFees.getDailyPenality() * (portFees.getContFreeDays() - daysInPort);
//			pd.put("Penality", portFees.getDailyPenality() * (portFees.getContFreeDays() - daysInPort));
//		}
//		pd.put("Forwarding agent",total);
//		return new EstimateDetail(total,pd);
		return null;
	}
	
	
}



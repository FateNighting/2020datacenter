package com.datacenter.util;

import org.springframework.stereotype.Service;


@Service
public class B2cDataSource {
	
//	private final String type= "B2C";
//	
//	//连接数据库
//	@Autowired
//	private B2CBlackMapper bbmapper;
//	@Autowired
//	private B2CWhiteMapper bwmapper;
//	@Autowired
//	private B2CMapper bgmapper;
//	@Autowired
//	private ApplyTableMapper applymapper;
//	
//	
//	private SocketService socketservice = new SocketService();
//
//	//黑样本数
//	private Integer blacknum;
//	private List<B2CBlack> blacks;
//	//白样本数
//	private Integer whitenum;
//	private List<B2CWhite> whites;
//	//测试样本总数
//	private Integer testnum;
//	//测试记录
//	private ApplyTable apply;
//	//四个测试结果数
//	private Integer true_true = 0;
//	private Integer true_false = 0;
//	private Integer false_true = 0;
//	private Integer false_false = 0;
//	//端口
//	private int PORT;
//	//IP地址
//	private String ADDRESS;
//
//
//	public void sendData() {
//		// TODO Auto-generated method stub
//		Random random = new Random();
//		float probability = blacknum/(float)(whitenum+blacknum);
//		String socketResult = "";
//		Integer step = (testnum / 20)==0?1:testnum / 20;
//		Integer count_step = 0;
//		while(true) {
//			count_step++;
//			if(count_step%step==0) {
//				update_apply((testnum-whitenum-blacknum)/(double)testnum);
//			}
//			if(blacknum>0 ) {
//				if(blacks==null || blacks.size() ==0) {
//					//System.out.println(bbmapper==null);
//					blacks = bbmapper.getRandRecord();
//				}
//			}
//			if(whitenum>0) {
//				if(whites==null || whites.size() ==0) {
//					whites = bwmapper.getRandRecord();
//				}
//			}
//			
//			if(blacknum>0 && random.nextFloat()<=probability ) {
//				//send black
////				JSONObject json  = JSONObject.fromObject(blacks.get(0));
//
//				String message = JSONValue.toJSONString(blacks.get(0));
//				
//				//System.out.print(message);
//				try {
//					socketResult = socketservice.useSocket(ADDRESS, PORT, message);
//					//System.out.println(" : " + socketResult);
//					if(socketResult.equals("1")) {
//						false_false++;
//					}else if(socketResult.equals("0")){
//						false_true++;;
//					}else {
//						false_false++;
//					}
//					blacks.remove(0);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				blacknum--;
//			}
//			else if(whitenum>0){
//				
//				//send white
//				//String message = JSONObject.fromObject(whites.get(0));
//				String message = JSONValue.toJSONString(whites.get(0));
//				//System.out.print(message);
//				try {
//					socketResult = socketservice.useSocket(ADDRESS, PORT, message);
//					//System.out.println(" : " + socketResult);
//					if(socketResult.equals("1")) {
//						true_false++;
//					}else if(socketResult.equals("0")){
//						true_true++;;
//					}else {
//						true_false++;
//					}
//					whites.remove(0);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				whitenum--;
//			}
//			System.out.println(whitenum + ":" + blacknum);
//			if(whitenum==0 && blacknum==0) {
//				break;
//			}
//		}
//	}
//	
//
//	public void init(ApplyTable apply,ModelManager model) {
//		// TODO Auto-generated method stub
//		if(apply.getSampleNumber2()==null || apply.getSampleNumber2().equals("")) {
//			blacknum = 0;
//		}else {
//			blacknum = Integer.parseInt(apply.getSampleNumber2());
//		}
//		if(apply.getSampleNumber1()==null || apply.getSampleNumber1().equals("")) {
//			whitenum = 0;
//		}else {
//			whitenum = Integer.parseInt(apply.getSampleNumber1());
//		}
//		PORT = Integer.parseInt(model.getModelPort());
//		ADDRESS = model.getModelIp();
//		testnum = blacknum + whitenum;
//		this.apply = apply;
//		true_true = 0;
//		true_false = 0;
//		false_true = 0;
//		false_false = 0;
//	}
//
//	public void update_apply(double jindu) {
//		
//		apply.setNorm20Rel(String.valueOf(jindu));
//		applymapper.updateByPrimaryKey(apply);
//	}
//
//	public String getType() {
//		// TODO Auto-generated method stub
//		return this.type;
//	}
//
//	public Integer getTrue_true() {
//		return true_true;
//	}
//
//	public Integer getTrue_false() {
//		return true_false;
//	}
//
//	public Integer getFalse_true() {
//		return false_true;
//	}
//
//	public Integer getFalse_false() {
//		return false_false;
//	}
//
//
//	public String get_target() {
//		// TODO Auto-generated method stub
//		double TP = 0;
//		double FP = 0;
//		double TN = 0;
//		double FN = 0;
//		double accuracy = 0; //精确率
//		double precision = 0; //准确率
//		double recall = 0; //召回率
//		double intrevention = 0; //干扰率
//		double F1 = 0; //F1
//		double KS = 0;
//		TP = false_false;
//		FP = true_false;
//		TN = true_true;
//		FN = false_true;
//		//准确率
//		precision = TP/(TP+FP);
//		//精确率
//		accuracy = (TP+TN)/(TP+FP+TN+FN);
//		//召回率
//		recall = (TP)/(TP+FN);
//		//干扰率
//		intrevention = (FP)/(TN+FP);
//		//F1
//		F1 = (2*precision*recall)/(precision+recall);
//		//KS
//		KS = recall - intrevention;
//		Double[] result = new Double[20];
//		System.out.println("TP:" + TP + ",FP:" + FP + ",TN:" + TN + ",FN:" + FN);
//		for (int i = 0; i < result.length; i++) {
//			result[i] = 0.0;
//		}
//		result[0] = precision;
//		result[1] = accuracy;
//		result[2] = recall;
//		result[3] = intrevention;
//		result[4] = F1;
//		result[11] = KS;
//		return Arrays.toString(result);
//		
//	}
//


}

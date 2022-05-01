package com.datacenter.service;

import com.datacenter.bean.Config;
import com.datacenter.bean.Countnews;
import com.datacenter.bean.News;
import com.datacenter.cache.BaseDataCache;
import com.datacenter.dao.*;
import com.datacenter.util.DateUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReceiveServiceImpl implements ReceiveService {
	
	@Autowired
	private GeturlMapper geturl;  //table: geturl  代表一张表
	@Autowired
	private DetectionMapper detection; //table: detecion
	
	@Autowired
	private ConfigMapper config;
	@Autowired
	private NewsMapper ne;
	@Autowired
	private NewsallMapper nall;
	@Autowired
	private CountnewsMapper count;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override  //写表->将配置信息存入数据库
	public void receiveSequence(Statics statics, HttpServletRequest request) {  //

		Config cfg = Config.getInstance();
		Config.resetConfig();

		cfg.setConfigTitle(request.getParameter("cfg_name"));
		cfg.setConfigUrl(request.getParameter("cfg_url"));
		cfg.setConfigModel(request.getParameter("cfg_model"));
		cfg.setConfigStart(request.getParameter("cfg_begin"));
		cfg.setConfigStop(request.getParameter("cfg_end"));		
		cfg.setConfigDays(Integer.parseInt(request.getParameter("cfg_day")));
		cfg.setConfigState(request.getParameter("cfg_state"));
		cfg.setConfigTime(request.getParameter("cfg_time"));

		cfg.setDataCapability(Integer.parseInt(request.getParameter("dataCount")));
		cfg.setDataLimit(Integer.parseInt(request.getParameter("allowDataCount")));
	}






	@Override  //获取采样模型信息并调用爬虫执行采样
	public void receiveModelOne() {
		long time = System.currentTimeMillis();
		// TODO Auto-generated method stub
		Config config = Config.getInstance();
		String reurl = config.getConfigUrl();
		String remodel = config.getConfigModel();
		String rebegin = config.getConfigStart();
		String restop = config.getConfigStop();
		int reday = config.getConfigDays();
		BaseDataCache.testDataCache = new CopyOnWriteArrayList<>();
		int countId = 1;
		//随机采样
		if("随机采样".equals(remodel)) {
			List<String> stringlist=new ArrayList<>();
			for(int i = 0; i < reday;){
				Date date = randomDate(rebegin,restop);
				if (stringlist.contains(sdf.format(date))) continue;
				i++;
				stringlist.add(sdf.format(date));
			}
			//从数据库获取指定日期的数据数量count（*）
			String str;
			int ssum;
			int sumall=0;
			String countnews;
			Countnews cou;

			for(String s:stringlist) {
				 ssum=nall.selectByDate(s); //获取指定日期的数据量的数目

				 /*for (int j=0;j<=reday;j++) {
					 all[j]=ssum;
				 }*/
				 sumall=sumall+ssum;  //累加
				 System.out.println(sumall);
//				 str=Integer.toString(sumall);
				 countnews="1";
				 cou = new Countnews();
				 cou.setCountSum(sumall);
				 cou.setCountAll(countnews);
				 cou.setCountId(countId++);
				try {
					TimeUnit.MILLISECONDS.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				count.insert(cou);
				BaseDataCache.testDataCache.addIfAbsent(cou);
			}
		}
		//水库采样
		else {
			try {
				List<String> waterlist = reservoirSampling(rebegin, restop, reday);
				int sumall=0;
				Countnews cou;
				for(String s:waterlist) {
					int ssum=nall.selectByDate(s);
					 sumall += ssum;
					 System.out.println(sumall);
//					 String str = String.valueOf(sumall);
					 String countnews = "1";
					 cou = new Countnews();
					 cou.setCountSum(sumall);
					 cou.setCountAll(countnews);
					 cou.setCountId(countId++);
					TimeUnit.MILLISECONDS.sleep(250);
//					 count.insert(cou);
					 BaseDataCache.testDataCache.add(cou);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		//config.insert(cfg);
		//数组方式存随机生成日期
		//String [] s=new String[reday];
		//列表方式存随机生成日期



	 /* //调用python程序并传参
		StringBuilder builder = new StringBuilder();
		builder.append("python").append(" ").append("D:\\pyFile\\connection\\connectionOne.py").append(" ");//爬虫程序路径
		for (String s : stringlist) {
		   builder.append(s).append(" ");
		}
		try {
		   Process process = Runtime.getRuntime().exec(builder.toString());
		} catch (Exception e) {
		   e.printStackTrace();
		}*/
	}

//	@Test
//	public void test1(){
//		System.out.println(sdf.format(randomDate("2020-01-02","2021-12-29")));
//		System.out.println(sdf.format(randomDate("2020-01-02","2021-12-29")));
//		System.out.println(sdf.format(randomDate("2020-01-02","2021-12-29")));
//		System.out.println(sdf.format(randomDate("2020-01-02","2021-12-29")));
//		System.out.println(sdf.format(randomDate("2020-01-02","2021-12-29")));
//		System.out.println(sdf.format(randomDate("2020-01-02","2021-12-29")));
//	}
	//随机生成日期
	private Date randomDate(String beginDate,String endDate){
		try {
			Date start = sdf.parse(beginDate);
			Date end = sdf.parse(endDate);

			if(start.getTime() >= end.getTime()){
				return null;
			}
			long date = random(start.getTime(),end.getTime());
			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//随机生成日期
	private long random(long begin,long end){
		        long rtn = begin + (long)(Math.random() * (end - begin));
		        if(rtn == begin || rtn == end){
		            return random(begin,end);
		        }
		        return rtn;
		    }
	//水库采样
	public List<String> findDates(String dBegin, String dEnd) throws ParseException {
		        //日期工具类准备
		        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		        //设置开始时间
		        Calendar calBegin = Calendar.getInstance();
		        calBegin.setTime(format.parse(dBegin));

		        //设置结束时间
		        Calendar calEnd = Calendar.getInstance();
		        calEnd.setTime(format.parse(dEnd));

		        //装返回的日期集合容器
		        List<String> Datelist = new ArrayList<String>();

		        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
		        while (format.parse(dEnd).after(calBegin.getTime()))  {
		            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
		            calBegin.add(Calendar.DAY_OF_MONTH, 1);
		            Datelist.add(format.format(calBegin.getTime()));
		        }

		        //打印结果 [2018-10-01, 2018-10-02, 2018-10-03, 2018-10-04, 2018-10-05, 2018-10-06, 2018-10-07, 2018-10-08, 2018-10-09, 2018-10-10, 2018-10-11, 2018-10-12, 2018-10-13, 2018-10-14, 2018-10-15, 2018-10-16, 2018-10-17, 2018-10-18, 2018-10-19, 2018-10-20, 2018-10-21, 2018-10-22, 2018-10-23, 2018-10-24, 2018-10-25]
		        return Datelist;
		    }
	//水库采样
	//k[]表示源数组  m表示水池大小
	public List<String> reservoirSampling ( String dBegin, String dEnd, int m) throws ParseException {
		        //b为水池
		        List<String> k=findDates(dBegin,dEnd);
		        List<String> b = new ArrayList<String>(m){};

		        if (k.size() <= m)
		            return new ArrayList<>();
		        else if (k.size() > m) {
		            for (int j = 0; j < k.size(); j++) {
		               // System.out.println(k.size());
		                if (j < m)
		                    b.add(k.get(j));     // 将前m个数据存入数组

		                else if (j >= m) {

		                    //从0-j中随机出一个数
		                    int r = new Random().nextInt(j+1);
		                    if (r < m)
		                        b.set(r, k.get(j));    //如果随机出的r<水池大小 ，则进行替换
		                }
		            }
		        }
		        return b;
		    }	 

		 
		 		 
	@Override   //读表   news
	public JSONObject getApply() throws JSONException {
		//Detection dt=detection.selectByPrimaryKey(1);
		//Detection dt=detection.selectByUrl("http://www.chinanews.com");
		Gson gson = new Gson();
		String str = "";
		
		List<News> modellist = new ArrayList<News>();
		modellist=ne.selectByBelong("chinanews");
		/*String rename=dt.getReName();
		String reurl=dt.getReUrl();
		String resurl=dt.getReSurl();
		String readdtime=dt.getReAddtime();
		String reitem=dt.getReItem();
		String reelement=dt.getReElement();
		String retree=dt.getReTree();
		Integer relimit=dt.getReLimit();*/
		//System.out.println(modellist.get(0).getReAddtime());//测试取值
		JSONObject json=new JSONObject();            
		
		str = gson.toJson(modellist);
		
		json.put("new",str);
		/*json.put("reurl", reurl);
		json.put("resurl", resurl);
		json.put("readdtime", readdtime);
		json.put("reitem", reitem);
		json.put("reelement", reelement);
		json.put("retree", retree);
		json.put("relimit", relimit);*/
		return json;
	}

	@Override   //读表countnews 将数据发给前端
	public JSONObject getCountnews() throws JSONException {

		Gson gson = new Gson();
		String str = "";
		List<Countnews> modellist = new ArrayList<Countnews>();
		//ArrayList<String> modellist=new ArrayList();
		modellist=count.selectByCountall("1");

		JSONObject json=new JSONObject();
		str = gson.toJson(modellist);
		json.put("count", str);
		return json;
	}

	@Override
	public Triplet<List<Countnews>,Double,Double> getCountNews() {

//		List<Countnews> countnews = count.selectByCountall("1");
		Config config = Config.getInstance();
		int maxCollectDay = (int)(((double)config.getDataLimit() / config.getDataCapability()) * 365);
		return Triplet.with(BaseDataCache.testDataCache,
				Math.min(1,BaseDataCache.testDataCache.size() == 0 ?
						1.0 :
						BaseDataCache.testDataCache.size() / (double)Config.getInstance().getConfigDays()),
				Math.min(1, BaseDataCache.baseDataSizeCache.size() == 0 ?
						1.0 :
						BaseDataCache.baseDataSizeCache.size() / (double)maxCollectDay)
		);
	}

	@Override   //读表news,统计模态为文本的条数，将统计数据发给前端
	public JSONObject selectByModeltxt() throws JSONException {
		String str = "";
		Gson gson = new Gson();
		
		int nnn=nall.selectByModel("文本");

		//int a=102312;
		System.out.println("文本数量为："+nnn);
		JSONObject json=new JSONObject();            
		str = gson.toJson(nnn);
		json.put("txtCount", str);
		
		return json;
		
	}
	@Override   //读表news,统计模态为图片的条数，将统计数据发给前端
	public JSONObject selectByModelpic() throws JSONException {
		String str = "";
		Gson gson = new Gson();
		
		int nnn=nall.selectByModel("图片");
			
		//int a=102312;
		//System.out.println(nnn);
		JSONObject json=new JSONObject();            
		str = gson.toJson(nnn);
		json.put("picCount", str);
		
		return json;
		
	}
	@Override   //读表news,统计模态为视频的条数，将统计数据发给前端
	public JSONObject selectByModelvideo() throws JSONException {
		String str = "";
		Gson gson = new Gson();
		
		int nnn=nall.selectByModel("视频");
			
		//int a=102312;
		//System.out.println(nnn);
		JSONObject json=new JSONObject();            
		str = gson.toJson(nnn);
		json.put("videoCount", str);
		
		return json;
		
	}

	@Override   //读表newsall,统计页面需要的不同的数据，将统计数据发给前端
	public JSONObject getCount() throws JSONException {
		String str = "";
		Gson gson = new Gson();
		JSONObject json=new JSONObject();
		List<String> s_type=nall.groupByNewsType();
		List<Integer> num_type = nall.selectByNewsType();
		List<String> s_modal=nall.groupByNewsModal();
		List<Integer> num_modal = nall.selectByNewsModal();
		List<Integer> num_month = nall.groupByMonth();
		List<Integer> num_modal_month=nall.groupModalByMonth("文本");
		//int a=102312;
		//System.out.println(nnn);
		String [] s_month={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
		//不同月份不同modal的数据量
		String a_modal_month="news_modal_monthCount:{文本：{";
		for(int i=0;i<s_month.length;i++){
			if(i==0)
			a_modal_month+=(s_month[i]+":"+num_modal_month.get(i).toString());
			else a_modal_month+=(","+s_month[i]+":"+num_modal_month.get(i).toString());
		}
		a_modal_month+="},图片：{";

		num_modal_month=nall.groupModalByMonth("图片");
		for(int i=0;i<s_month.length;i++){
			if(i==0)
				a_modal_month+=(s_month[i]+":"+num_modal_month.get(i).toString());
			else a_modal_month+=(","+s_month[i]+":"+num_modal_month.get(i).toString());
		}
		a_modal_month+="},视频：{";

		num_modal_month=nall.groupModalByMonth("视频");
		for(int i=0;i<s_month.length;i++){
			if(i==0)
				a_modal_month+=(s_month[i]+":"+num_modal_month.get(i).toString());
			else a_modal_month+=(","+s_month[i]+":"+num_modal_month.get(i).toString());
		}
		a_modal_month+="}}";
		//不同月份的数据量
		String a_month="news_monthCount:{";
		for(int i=0;i<num_month.size();i++){
			if(i==0)
			a_month+=(s_month[i]+":"+num_month.get(i).toString());
			else a_month+=(","+s_month[i]+":"+num_month.get(i).toString());
		}
		a_month+="}";
		//不同type的数量
		String a_type="news_typeCount:{";
		for(int i=0;i<s_type.size();i++){
			if(i==0)
			a_type+=(s_type.get(i).toString()+":"+num_type.get(i).toString());
			else a_type+=(","+s_type.get(i).toString()+":"+num_type.get(i).toString());
		}
		a_type+="}";

		//不同modal的数量
		String a_modal="news_modalCount:{";
		for(int i=0;i<s_modal.size();i++){
			if(i==0)
			a_modal+=(s_modal.get(i).toString()+":"+num_modal.get(i).toString());
			else a_modal+=(","+s_modal.get(i).toString()+":"+num_modal.get(i).toString());
		}
		a_modal+="}";

		str=gson.toJson(a_modal_month);

		str += gson.toJson(a_month);

		str += gson.toJson(a_type);

		str+=gson.toJson(a_modal);

		json.put("Count", str);
		return json;
	}
//	@Override//返回不同model的数量
//	JSONObject selectByNewsModel() throws JSONException{
//
//	}
//	@Override//返回不同月份对于的不同model的数量
//	JSONObject selectByMonth() throws JSONException{
//
//	}
}

	


package com.datacenter.controller;

import EvoAn.MatlabEvoAn;
import com.datacenter.bean.Countnews;
import com.datacenter.bean.Deviceresult;
import com.datacenter.bean.SampingResult;
import com.datacenter.bean.WellConfig;
import com.datacenter.cache.BaseDataCache;
import com.datacenter.cache.CrossSessionCache;
import com.datacenter.dao.GetParameterMapper;
import com.datacenter.dto.EstimateResultDto;
import com.datacenter.entity.Parameters;
import com.datacenter.service.BaseService;
import com.datacenter.service.ReceiveService;
import com.datacenter.service.Statics;
import com.datacenter.util.CsvToJsonUtil;
import com.datacenter.util.JPythonUtil;
import com.datacenter.vo.WellConfigVo;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value= "/datacenter")
@Slf4j
public class DataController {

//	@Resource
//	private DataSource dataSource;
	@Autowired
	private ReceiveService receiveservice;
	
	@Autowired
	private Statics statics;

	@Resource
	private BaseService baseService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private GetParameterMapper getParameterMapper;

	@Value("${python-script.sampling-url}")
	private String samplingUrl;

	@Value("${python-script.estimate-url1}")
	private String estimateUrl1;

	//获取前端发送的6个参数
	@PostMapping("/getJson")
	public Parameters getParameter(@RequestParam("data_set") String data_set, @RequestParam("well_size") String well_size,
								   @RequestParam("Interval_multiple") String Interval_multiple, @RequestParam("sampling_rate") String sampling_rate,
								   @RequestParam("Abnormal_upper_limit") String Abnormal_upper_limit, @RequestParam("Abnormal_lower_limit") String Abnormal_lower_limit) throws IOException {
		Parameters parameter = new Parameters();
		parameter.setData_set(data_set);
		parameter.setWell_size(well_size);
		parameter.setInterval_multiple(Interval_multiple);
		parameter.setSampling_rate(sampling_rate);
		parameter.setAbnormal_upper_limit(Abnormal_upper_limit);
		parameter.setAbnormal_lower_limit(Abnormal_lower_limit);
		//执行两个py文件
		List<String> s= JPythonUtil.getSample(parameter.getData_set(),parameter.getWell_size(),parameter.getInterval_multiple(),parameter.getSampling_rate(),parameter.getAbnormal_upper_limit(),parameter.getAbnormal_lower_limit());
		JPythonUtil.getSampleResult(parameter.getData_set(),parameter.getAbnormal_upper_limit(),parameter.getAbnormal_lower_limit(),s.get(0),s.get(1),s.get(2),parameter.getWell_size(),parameter.getInterval_multiple(),parameter.getSampling_rate());

		return parameter;
	}


	//发送sample的json数据给前端
	@RequestMapping("/postJson1")
	public String sendJson1() throws Exception {
		String jsons= CsvToJsonUtil.readFileByLines("/root/data_detection/model/Generate_Csv_File/sample.csv");
		return jsons;
	}
	//发送sampleResult的json数据给前端
	@RequestMapping("/postJson2")
	public String sendJson2() throws Exception {
		String jsons=CsvToJsonUtil.readFileByLines("/root/data_detection/model/Generate_Csv_File/sampleResult.csv");
		return jsons;
	}


	@GetMapping(value = "/evaluate")
	@CrossOrigin
	public Sextet<Double,Double,Double,Double,Double,Double> evaluateStatistic(){
		return baseService.evaluateStatistic();
	}





	@CrossOrigin
	@GetMapping(value = "/getsequence") // 获取勘探配置信息，存数据库 --> config表
	public void getSequence(HttpServletResponse response)
			throws IOException {



//		baseService.resetSourceTable();
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
//		System.out.println(request.getParameter("business_type"));
		receiveservice.receiveSequence(statics, request);

	}
	
	//读取模型、指定的日期范围和天数
	@CrossOrigin
	@RequestMapping(value = "/getmodel")
	public void getModel(HttpServletResponse response)
			throws IOException {
//		baseService.resetSourceTable();
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		new Thread(baseService::computeBaseArgs).start();
		new Thread(receiveservice::receiveModelOne).start();

		return;
	}



	@CrossOrigin
	@RequestMapping(value = "/getapplytype") // 读表news
	public void getUseType(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject applyType = receiveservice.getApply();
		out.println(applyType);
		return;
	}
	
	/*
	@CrossOrigin
	@RequestMapping(value = "/getcountnews") // 返回探测列表，将detection表的数据返回前端
	public void getCount(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject applyType = receiveservice.getCountnews();
		out.println(applyType);
		return;
	}
	*/

	@GetMapping(value = "/getcountnews")
	@CrossOrigin
	public Triplet<List<Countnews>,Double,Double> getCount(){
		return receiveservice.getCountNews();
	}


	@RequestMapping(value = "/gettext") // 返回文本数量
	public void getText(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject applyType = receiveservice.selectByModeltxt();
		out.println(applyType);
		return;
	}
	@RequestMapping(value = "/getpic") // 返回图片数量
	public void getPic(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject applyType = receiveservice.selectByModelpic();
		out.println(applyType);
		return;
	}

	@RequestMapping(value = "/getvideo") // 返回视频数量
	public void getVideo(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject applyType = receiveservice.selectByModelvideo();
		out.println(applyType);
		return;
	}

	@RequestMapping(value = "/getcount") // 返回页面所需要的各项数据
	public void getNewsType(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject applyType = receiveservice.getCount();
		out.println(applyType);
		return;
	}
	/*@CrossOrigin
	@RequestMapping(value = "/getsampling") //返回采样策略表，将sampling表的数据返回前端
	public void getSampType(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject sampType = receiveservice.getSampling();
		out.println(sampType);
		return;
	}*/


	@ApiOperation("提交评估配置的时候，即生成评估数据，此时需要重置数据库的内容")
	@GetMapping(value = "/reset")
	public int resetSourceTable(){
		return baseService.resetSourceTable();
	}



	@CrossOrigin
	@ApiOperation(value = "水井采样参数提交",httpMethod = "POST")
	@PostMapping(value = "/wellConfig")
	public void wellConfigSubmit(@RequestBody WellConfigVo wellConfigVo){
		CrossSessionCache.sessionAccessTime = 1;
		WellConfig.getInstance().initByVo(wellConfigVo);
		BaseDataCache.wellSampleCache = new ArrayList<>();
		log.error(WellConfig.getInstance().toString());
		log.error(wellConfigVo.toString());
	}

	@CrossOrigin
	@ApiOperation("开始水井采样")
	@PostMapping(value = "/wellSampling")
	public Triplet<Double, Double, String> wellSampling(){
		return baseService.wellSampling(WellConfig.getInstance().getDataSource(), WellConfig.getInstance().getWellSize(),
				WellConfig.getInstance().getIntervalMagnification(), WellConfig.getInstance().getSamplingRate(),
				WellConfig.getInstance().getAbnormalUpperLimit(), WellConfig.getInstance().getAbnormalLowerLimit());
	}

	@CrossOrigin
	@ApiOperation(value = "模型评估手段" ,httpMethod = "GET")
	@GetMapping(value = "/estimateModel")
	public EstimateResultDto estimateModel(){
		return baseService.estimateModel();
	}


	@CrossOrigin
	@ApiOperation(value = "获取采样对比图" )
	@GetMapping(value = "/samplingCompare")
	public List<SampingResult> sampingComparision() throws IOException {
		return baseService.samplingCompare();
	}

	@CrossOrigin
	@ApiOperation(value = "获取采样估计值" )
	@GetMapping(value = "/samplingValue")
	public Triplet<List<Countnews>,Double, Double> sampingValue() {
		Triplet<List<Countnews>,Double, Double> res = baseService.samplingValue(CrossSessionCache.sessionAccessTime);
		if (res.getValue1() == 1)
			CrossSessionCache.sessionAccessTime = 10;
		else
			CrossSessionCache.sessionAccessTime += 1;
		log.warn(CrossSessionCache.sessionAccessTime + "------------------------------");
		return res;
	}

//	@Test
//	@GetMapping(value = "/test")
//	public List<String> test1(){
//		System.out.println(estimateUrl1);
//		List<String> s = JPythonUtil.pyScriptHelper(new String[]{"python",estimateUrl1 ,
//				"HSI2014",
//				"1.5","0.5","44.46","149",
//				"/usr/share/pyshared/DSModel/DSModel/DataSet/sample.csv"});
//		return s;
//	}

	@GetMapping(value = "/evaluateKe")
	public List<Deviceresult> getResult() throws MWException{
		return baseService.evaluateLiuKe();
	}

//	@GetMapping(value = "/test")
	@Test
	public void test12() throws MWException {
		MatlabEvoAn matlabEvoAn = new MatlabEvoAn();

		log.warn("--------------------------ok228--------------------");
		Object[] result = matlabEvoAn.EvoAn(3, 2, 200, 3,"F:\\ProjectCode\\2020datacenter\\src\\main\\resources\\Aggregation.csv");
//		Object[] result = matlabEvoAn.EvoAn(3, Radius, Decay, MinThreshold,path);
		log.warn("--------------------------ok231--------------------");
		double[] data1 = MWNumericArray.class.cast(result[0]).getDoubleData();
		double[] data2 = MWNumericArray.class.cast(result[1]).getDoubleData();
		double[] data3 = MWNumericArray.class.cast(result[2]).getDoubleData();
		log.warn("--------------------------ok235--------------------");

		List<Deviceresult> res = new ArrayList<>();
		for (int i = 0; i< data1.length;i++) {
			res.add(new Deviceresult(data1[i],data2[i],data3[i]));
		}
		System.out.println(res);
//		return res;
	}

}

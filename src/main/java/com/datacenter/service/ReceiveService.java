package com.datacenter.service;

import com.datacenter.bean.Countnews;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReceiveService {
	
    //存表->用户业务申请的各项需求指标

	void receiveSequence(Statics statics, HttpServletRequest request);
	void receiveModelOne();
	JSONObject getApply() throws JSONException;
	JSONObject selectByModeltxt() throws JSONException;//返回文本的数量
	JSONObject selectByModelpic() throws JSONException;//返回图片的数量
	JSONObject selectByModelvideo() throws JSONException;//返回视频的数量
	JSONObject getCount() throws JSONException;//返回不同type的数量
	JSONObject getCountnews() throws JSONException;
	
	Triplet<List<Countnews>,Double,Double> getCountNews();
	
}

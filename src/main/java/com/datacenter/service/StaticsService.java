package com.datacenter.service;

import org.json.JSONException;
import org.json.JSONObject;

public interface StaticsService {
	
	
	public void updateCount(boolean sign,boolean result);
	public JSONObject get_target()throws JSONException;
	public boolean updateModels(ReceiveService receiveservice);
}

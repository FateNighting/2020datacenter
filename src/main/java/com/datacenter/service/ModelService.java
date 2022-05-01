package com.datacenter.service;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ModelService {

void receiveModelOne(Statics statics, HttpServletRequest request);
	
	JSONObject getApplyType() throws JSONException;
	JSONObject getSampling() throws JSONException;
}

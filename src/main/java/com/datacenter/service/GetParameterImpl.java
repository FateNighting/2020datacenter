package com.datacenter.service;

import com.datacenter.dao.GetParameterMapper;
import com.datacenter.entity.Parameters;
import com.datacenter.util.JPythonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetParameterImpl implements GetParameterMapper{

    @Autowired
    private GetParameterMapper getParameterMapper;

    @Override
    public Parameters getParameter(Parameters parameters) {
        return getParameterMapper.getParameter(parameters);
    }
}

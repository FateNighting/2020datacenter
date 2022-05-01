package com.datacenter.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.yaml")
@Scope("singleton")
public class Statics {//全局变量所在

}

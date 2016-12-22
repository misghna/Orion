package com.sesnu.orion.web.utility;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.aws.context.annotation.ConditionalOnMissingAmazonClient;
//import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.partitions.model.Region;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;


@Configuration
public class SnsConfiguration {

	//static BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAIYKVBYMY2KIYCFJQ", "aEe/cgcnzgBPt7aeh7JeW2mJm2pi+vvsP/Ib4eJ9");

	@Autowired
	ConfigFile conf;
	@Autowired
	Util util;
	
	@Bean
	public AmazonSNS amazonSNS() {
//		String awsAccKey = util.decryptText(conf.getProp().getProperty("awsAccKey"));
//		String awsSecKey = util.decryptText(conf.getProp().getProperty("awsSecKey"));
//		
//		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccKey, awsSecKey);
		AmazonSNSClient amazonSNSClient = new AmazonSNSClient(getAwsCred());

		return amazonSNSClient;
	}
	
	public BasicAWSCredentials getAwsCred (){
		String awsAccKey = util.decryptText(conf.getProp().getProperty("awsAccKey"));
		String awsSecKey = util.decryptText(conf.getProp().getProperty("awsSecKey"));
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccKey, awsSecKey);
		return awsCreds;
	}
	
	
	public String getBucketName(){
		return conf.getProp().getProperty("s3BucketName");
	}
	
}


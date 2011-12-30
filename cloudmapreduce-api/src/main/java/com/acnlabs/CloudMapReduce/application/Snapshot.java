package com.acnlabs.CloudMapReduce.application;

import com.acnlabs.CloudMapReduce.Global;
import com.acnlabs.CloudMapReduce.QueueManager;
import com.acnlabs.CloudMapReduce.SimpleQueue;
import com.acnlabs.CloudMapReduce.S3FileSystem;
import com.acnlabs.CloudMapReduce.S3Item;
import com.amazon.s3.*;
//import com.amazon.services.s3;
import com.amazonaws.queue.model.Message;
import org.apache.log4j.Logger;

/*
 * d3 This class is used to produce snapshot in directory
	It takes outputQueue,s3Path,accessKeyId,secretAccessKey
	NOTE: have to modify as per finalize
*/

public class Snapshot{
	private SimpleQueue outputQueue;
	String accessKeyId;
	String secretAccessKey;
	private Logger  logger = Logger.getLogger("com.acnlabs.CloudMapReduce.MapReduce");
	private S3FileSystem s3FileSystem;
	private String s3Path;
	AWSAuthConnection conn;
	public Snapshot(SimpleQueue outputQueue,String s3Path,String accessKeyId, String secretAccessKey)
	{
		this.outputQueue = outputQueue;
		this.s3Path=s3Path;
		this.accessKeyId=accessKeyId;
		this.secretAccessKey=secretAccessKey;
	}
	

public void ShowSnapshot()
{
	String outputString="";
	s3FileSystem=new S3FileSystem(accessKeyId, secretAccessKey);
	
	logger.info("Reading OutputQueue");
	//reading from outputQueue passed as param
	try{
	for (Message msg : outputQueue) { 
		String value = msg.getBody();
		logger.info("\n\n\nMessageBody:" + value);
		outputString=outputString + value;
	}
		
		// creating bucket and uploading file NOTE: its static right now, make it dynamic, TBD
		logger.info("\n\n\nCreating new Bucket and Pushing data");
		S3Item s= s3FileSystem.getItem("/radixanddreamz/output");
		s=s.addDir("firstSnapshot");
		s=s.upload("amit.txt", outputString.getBytes());
	}
	catch(Exception e)
	{
		logger.warn(e);
	}
	
}
}

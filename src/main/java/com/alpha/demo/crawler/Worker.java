package com.alpha.demo.crawler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.alpha.demo.crawler.model.AbstractCategory;
import com.alpha.demo.crawler.parse.HtmlParser;
import com.alpha.demo.crawler.write.ExcelWriter;

public class Worker {
	
	private static final Logger logger = Logger.getLogger(Worker.class);
	
	/**
	 * task queue. HtmlParser put task into it; ExcelWriter takes task from it.
	 */
    private BlockingQueue<AbstractCategory> taskQueue = new LinkedBlockingQueue<AbstractCategory>(1000);

    private ExcelWriter writer;
    private String url;
    
    private volatile boolean parseFinished = false;
  
    public Worker(String url) {
    	this.url = url;
    }
    
    /**
     * start parse & write
     */
    public void start() {
    	Thread writerThread = startWirterThread();
    	parse();
    	if (writer != null) {
    		writer.close();
    	}
    	try {
			writerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    }
 
    /**
     * parse the html
     */
    private void parse() {
    	HtmlParser parser = new HtmlParser();
    	parser.parse(url, this);
    	parseFinished();
    }
    
    /**
     * start a new Thread to write excel
     * @return
     */
    private Thread startWirterThread() {
    	Thread writerThread = new Thread(new Runnable() {
            @Override
            public void run() {
            	writer = new ExcelWriter(Worker.this);
            	while (true) {
            		if (parseFinished && taskQueue.isEmpty()) {
            			break;
            		}
					writer.write(takeTask());
            	}
            	writer.close();
            }
        });
    	writerThread.start();
    	return writerThread;
    }
  
    public void parseFinished() {
    	parseFinished = true;
    }
    
    public void putTask(AbstractCategory category) {
    	try {
			taskQueue.put(category);
			logger.info("Put task: " + category);
		} catch (InterruptedException e) {
			logger.error("Fail to put task: " + category);
			e.printStackTrace();
		}
    }
    
    public AbstractCategory takeTask() {
    	try {
    		AbstractCategory category = taskQueue.take();
			logger.info("Take task: " + category);
			return category;
		} catch (InterruptedException e) {
			logger.error("Fail to take task: ");
			e.printStackTrace();
		}
    	return null;
    }
    
    
    public void write() {
    	
    }
    
  
}

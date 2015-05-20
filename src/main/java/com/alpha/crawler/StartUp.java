package com.alpha.crawler;

import com.alpha.crawler.util.Consts;
import com.alpha.crawler.util.LogUtil;

public class StartUp {
	
    public static void main( String[] args ) {
    	LogUtil.initLog4j();
    	new Worker(Consts.CRAWL_URL).start();
    }
}

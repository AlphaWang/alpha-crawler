//
/////////////////////////////////////////////////////////////////
//                 C O P Y R I G H T  (c) 2014
//             A G F A - G E V A E R T  G R O U P
//                    All Rights Reserved
/////////////////////////////////////////////////////////////////
//
//       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
//                    Agfa-Gevaert Group
//      The copyright notice above does not evidence any
//     actual or intended publication of such source code.
//
/////////////////////////////////////////////////////////////////
//
//
package com.alpha.demo.crawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jsoup.helper.StringUtil;

public class Consts {

	public static final String APP_PATH = System.getProperty("user.dir");
	public static final String CONF_PATH = APP_PATH + File.separator + "conf" + File.separator;
	public static final String LOG4J_FILE = CONF_PATH + "log4j.properties";
	public static final String CONF_FILE = CONF_PATH + "crawl.properties";
	
	public static final String OUTPUT_PATH = APP_PATH + File.separator + "output" + File.separator ;
	
	public static final String CRAWL_URL;
	//#baby http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dbaby-products
	//#1 http://www.amazon.com/s/ref=sr_nr_n_0?rh=n%3A165796011%2Cn%3A!165797011%2Cn%3A2402554011&bbn=165797011&ie=UTF8&qid=1402388268&rnid=165797011
	//#last http://www.amazon.com/b/ref=sr_aj?node=166842011&ajr=0

	static {
		Properties prop = new Properties();
        try {
			prop.load(new FileInputStream(CONF_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			String url = prop.getProperty("CRAWL_URL");
	        if (StringUtil.isBlank(url)) {
	        	CRAWL_URL = "http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dbaby-products";
	        } else {
	        	CRAWL_URL = url;
	        }
		}
        
	}
}

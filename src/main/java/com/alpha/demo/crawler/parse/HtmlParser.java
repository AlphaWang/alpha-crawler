
package com.alpha.demo.crawler.parse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alpha.demo.crawler.Worker;
import com.alpha.demo.crawler.model.AbstractCategory;
import com.alpha.demo.crawler.model.Category;
import com.alpha.demo.crawler.util.NoSubCategoryException;
import com.alpha.demo.crawler.util.ParseHtmlException;


public class HtmlParser {
	
	private static final Logger logger = Logger.getLogger(HtmlParser.class);
	private static final int TIME_OUT = 30 * 1000;
	
	private ExecutorService parserExector = Executors.newFixedThreadPool(10);
	
	private static class InstanceHolder {
		public static HtmlParser instance = new HtmlParser();
	}
	
	public static HtmlParser getInstance() {
		return InstanceHolder.instance;
	}
	
	public HtmlParser() {
		
	}
	
	/**
	 * Parse the given url
	 * @param url
	 * @param worker
	 * @return
	 */
	public AbstractCategory parse(String url, Worker worker) {
		Elements list = null;
		try {
			list = getCategoryListElements(url);
		} catch (ParseHtmlException e) {
			logger.error(e.getMessage());
			return null;
		}

		Element currentElem = getCurrentElement(list);
		if (currentElem == null) {
			logger.warn("Can not find category title in " + url);
			return null;
		}
		AbstractCategory rootCategory = new Category(currentElem.html(), url, 0, null);
		worker.putTask(rootCategory);
		
		for (Element elem : getSubElements(list)) {
			Element aElem = elem.select("a").first();
			Element spanElem = aElem.select("span").first();
			AbstractCategory childCategory = new Category(spanElem.html(), aElem.attr("href"), 1, rootCategory);
			rootCategory.addChild(childCategory);
			worker.putTask(childCategory);
		}
		
		// parse children
		for (AbstractCategory subCategory : rootCategory.getChildren()) {
			parse(subCategory, worker);
		}
		logger.info("All Category Tree: \n" + rootCategory.print());
		return rootCategory;
	}
	
	/**
	 * parse sub categories of currentCategory
	 * @param currentCategory
	 * @param worker
	 */
	private void parse(AbstractCategory currentCategory, Worker worker) {
		String url = currentCategory.getUrl();
		String name = currentCategory.getName();
		Elements list = null;
		try {
			list = getCategoryListElements(url);
		} catch (NoSubCategoryException e) {
			logger.info("Parsing Category: [" + name + "]. (Leaf Category)");
			return;
		} catch (ParseHtmlException e) {
			logger.warn("Parsing Category: [" + name + "]. " + e.getMessage());
			return;
		}
		logger.info("Parsing Category: [" + name + "].");
		
		addSubCategory(currentCategory, getSubElements(list), worker);
		
		for (AbstractCategory subCategory : currentCategory.getChildren()) {
			parse(subCategory, worker);
		}
	}
	
	/**
	 * get category list elements from the url
	 * @param url
	 * @return
	 * @throws ParseHtmlException
	 */
	private Elements getCategoryListElements(String url) throws ParseHtmlException{
		Document doc = getDocument(url);
		Element sectionElem = getCategorySectionElement(doc);
		if (sectionElem == null) {
			logger.debug("Can not find category section in " + url);
			throw new NoSubCategoryException("Can not find category section in " + url);
		}
		Elements listElems = getCategoryListElements(sectionElem);
		if (listElems == null) {
			logger.debug("Can not find category list in " + url);
			throw new NoSubCategoryException("Can not find category list in " + url);
		}
		return listElems;
	}
	
	private Elements getCategoryListElements(Element sectionElement) {
		return sectionElement.select("ul li");
	}
	
	private Element getCategorySectionElement(Document doc) {
		return doc.select("div.categoryRefinementsSection").first();
	}
	
	/**
	 * Connect to url, and get Document.
	 * @param url
	 * @return
	 * @throws ParseHtmlException
	 */
	private Document getDocument(String url) throws ParseHtmlException {
		try {
			return Jsoup.connect(url).timeout(TIME_OUT).get();
		} catch (IOException e) {
			logger.error("Can not find category list in " + url);
			throw new ParseHtmlException("Fail to parse " + url + "\n" + e);
		}
	}
	
	private Element getCurrentElement(Elements categoryListElems) {
		return categoryListElems.select("strong").first();
	}
	
	private List<Element> getSubElements(Elements elements) {
		int titleIndex = 0;
		for (int i = 0, size = elements.size(); i < size; i++ ) {
			Element elem = elements.get(i);
			Element title = elem.select("strong").first();
			if (title != null){
				titleIndex = i;
				break;
			}
		}
		return elements.subList(titleIndex + 1, elements.size());
	}
	
	
	private void addSubCategory(AbstractCategory currentCategory, List<Element> subElements, Worker worker) {
		for (Element subElem : subElements) {
			Element aElem = subElem.select("a").first();
			Element spanElem = aElem.select("span").first();
			AbstractCategory childCategory = new Category(spanElem.html(), aElem.attr("href"),
					currentCategory.getLevel() + 1, currentCategory);
			currentCategory.addChild(childCategory);
			worker.putTask(childCategory);
		}
		
	}


}

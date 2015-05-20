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
package com.alpha.demo.crawler.write;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;

import com.alpha.demo.crawler.Worker;
import com.alpha.demo.crawler.model.AbstractCategory;
import com.alpha.demo.crawler.util.Consts;

public class ExcelWriter {
	private static final Logger logger = Logger.getLogger(ExcelWriter.class);
	
	private WritableWorkbook book;
	private WritableSheet sheet;
	
	private int maxRow = 0;
	private Worker worker;
	
	/**
	 * init the excel
	 * @param worker
	 */
	public ExcelWriter(Worker worker) {
		String filePath = Consts.OUTPUT_PATH + "crawl_" + System.currentTimeMillis() + ".xls";
		logger.info("OUTPUT FILE: " + filePath);
		File file = new File(filePath);
		
        try {
        	if (!file.exists()) {
    			file.createNewFile();
    		}
			book = Workbook.createWorkbook(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sheet = book.createSheet("crwal_result", 0);
        this.worker = worker;
	}
	
	/**
	 * write an AbstractCategory into excel
	 * @param category
	 */
	public void write(AbstractCategory category) {
		if (category.getParent() == null) {
			write(category, ++maxRow);
		} else {
			int row = findRow(category);
			if (row < 0) {
				worker.putTask(category);
			}
			write(category, row);
		}
//		close();
	}
	
	private void write(AbstractCategory category, int row){
		int level = category.getLevel();
		String name = category.getName();
		
		Label label = new Label(level, row, name);
		sheet.insertRow(row);
		maxRow++;
		try {
			logger.info("Writing Category: ("+row+ ", "+level +") [" + category + "]");
			sheet.addCell(label);
			if (!StringUtil.isBlank(category.getUrl())) {
				URL url = new URL(category.getUrl());
				WritableHyperlink link = new WritableHyperlink(level+1, row, url);
			    sheet.addHyperlink(link);
			}
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if (book != null) {
			try {
				book.write();
				book.close();
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * get the row number
	 * @param category
	 * @return
	 */
	private int findRow(AbstractCategory category) {
		int parentRow = findParentRow(category);
		if (parentRow < 0) {
			return parentRow;
		}
		Cell[] col = sheet.getColumn(category.getParent().getLevel());
		for (int row = ++parentRow; row < col.length && row <= maxRow; row++) {
			if (!StringUtil.isBlank(col[row].getContents())) {
				return row;
			}
		}
		return maxRow;
	}
	
	private int findParentRow(AbstractCategory category) {
		Set<Integer> parentIndex = new HashSet<Integer>();
		AbstractCategory p = category.getParent();
		int level = p.getLevel();
		Cell[] pCol = sheet.getColumn(level);
		for (int row = 0; row < pCol.length; row++) {
			if (pCol[row].getContents().equalsIgnoreCase(p.getName())) {
				parentIndex.add(row);
			}
		}
		if (parentIndex.size() == 0) {
			return -1;
		} else if (parentIndex.size() == 1) {
			return parentIndex.iterator().next();
		} else {
			Cell[] grandCol = sheet.getColumn(level-1);
			for (int i : parentIndex) {
				String grandP = null;
				for (int j = i; j > 0; j--) {
					grandP = grandCol[j].getContents();
					if (!StringUtil.isBlank(grandP)) {
						break;
					}
				}
				if (p.getParent().getName().equalsIgnoreCase(grandP)) {
					return i;
				}
			}
			return parentIndex.iterator().next();
		}
	}
	
	private AbstractCategory getParent(AbstractCategory category, int level) {
		if (category.getParent() == null) {
			return category;
		}
		while (true) {
			if (category.getLevel() == level) {
				return category;
			}
			category = category.getParent();
		}
			
	}

}

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
package com.alpha.demo.crawler.model;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class Category extends AbstractCategory{
	
	private String name;
	private String url;
	private int level;
	private AbstractCategory parent;
	private Collection<AbstractCategory> children;
	
	public Category(String name, String url, int level, AbstractCategory parent) {
		this.name = name;
		this.url = url;
		this.level = level;
		this.parent = parent;
		this.children = new CopyOnWriteArrayList<AbstractCategory>();
	}

	@Override
	public void addChild(AbstractCategory child) {
		children.add(child);
	}

	@Override
	public void removeChild(AbstractCategory child) {
		children.remove(child);	}

	@Override
	public Collection<AbstractCategory> getChildren() {
		return Collections.unmodifiableCollection(children);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUrl() {
		return url;
	}



	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public AbstractCategory getParent() {
		return parent;
	}

}

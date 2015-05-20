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

public abstract class AbstractCategory {
	
	public abstract void addChild(AbstractCategory child);
	public abstract void removeChild(AbstractCategory child);
	public abstract Collection<AbstractCategory> getChildren();
	public abstract AbstractCategory getParent();
	
	public abstract String getName();
	public abstract String getUrl();
	public abstract int getLevel();
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < getLevel(); i++) {
			sb.append("\t");
		}
		sb.append(getName()).append(" - ").append(getUrl());
		return sb.toString();
	}
	
	public String print() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < getLevel(); i++) {
			sb.append("\t");
		}
		sb.append(getName()).append(" - ").append(getUrl()).append("\n");
		for (AbstractCategory child : getChildren()) {
			sb.append(child.print());
		}
		return sb.toString();
	}
}

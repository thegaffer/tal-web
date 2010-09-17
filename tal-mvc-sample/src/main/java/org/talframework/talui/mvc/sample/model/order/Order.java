/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.talui.mvc.sample.model.order;

import java.util.Date;

import org.talframework.talui.mvc.sample.model.common.Address;

/**
 * Simple Bean for prototyping
 * 
 * @author Tom Spencer
 */
public interface Order {
	
	/**
	 * @return the id
	 */
	public Object getId();
	
	/**
	 * @return the account
	 */
	public String getAccount();
	
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account);
	
	/**
	 * @return the collectionDate
	 */
	public Date getCollectionDate();
	
	/**
	 * @param collectionDate the collectionDate to set
	 */
	public void setCollectionDate(Date collectionDate);
	
	/**
	 * @return the collectionTime
	 */
	public Date getCollectionTime();
	
	/**
	 * @param collectionTime the collectionTime to set
	 */
	public void setCollectionTime(Date collectionTime);
	
	/**
	 * @return the service
	 */
	public String getService();
	
	/**
	 * @param service the service to set
	 */
	public void setService(String service);
	
	/**
	 * @return the goodsType
	 */
	public String getGoodsType();
	
	/**
	 * @param goodsType the goodsType to set
	 */
	public void setGoodsType(String goodsType);
	
	/**
	 * @return the goodsNumber
	 */
	public int getGoodsNumber();
	
	/**
	 * @param goodsNumber the goodsNumber to set
	 */
	public void setGoodsNumber(int goodsNumber);
	
	/**
	 * @return the goodsWeight
	 */
	public String getGoodsWeight();
	
	/**
	 * @param goodsWeight the goodsWeight to set
	 */
	public void setGoodsWeight(String goodsWeight);
	
	/**
	 * @return the collection
	 */
	public Address getCollection();
	
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Address collection);
	
	/**
	 * @return the delivery
	 */
	public Address getDelivery();
	
	/**
	 * @param delivery the delivery to set
	 */
	public void setDelivery(Address delivery);
	
}

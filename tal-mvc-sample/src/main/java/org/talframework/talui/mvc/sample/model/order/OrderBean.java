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
public class OrderBean implements Order {
	private String id = null;
	private String account = null;
	
	private Address collection = null;
	private Address delivery = null;
	
	private Date collectionDate = null;
	private Date collectionTime = null;
	private String service = null;
	
	private String goodsType = null;
	private int goodsNumber = 1;
	private String goodsWeight = null;
	
	/**
	 * @return the id
	 */
	public Object getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Object id) {
		if( id == null ) this.id = null;
		else if( id instanceof Object[] ) this.id = ((Object[])id)[0].toString();
		else this.id = id.toString();
	}
	
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the collectionDate
	 */
	public Date getCollectionDate() {
		return collectionDate;
	}
	/**
	 * @param collectionDate the collectionDate to set
	 */
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}
	/**
	 * @return the collectionTime
	 */
	public Date getCollectionTime() {
		return collectionTime;
	}
	/**
	 * @param collectionTime the collectionTime to set
	 */
	public void setCollectionTime(Date collectionTime) {
		this.collectionTime = collectionTime;
	}
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * @return the goodsType
	 */
	public String getGoodsType() {
		return goodsType;
	}
	/**
	 * @param goodsType the goodsType to set
	 */
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	/**
	 * @return the goodsNumber
	 */
	public int getGoodsNumber() {
		return goodsNumber;
	}
	/**
	 * @param goodsNumber the goodsNumber to set
	 */
	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	/**
	 * @return the goodsWeight
	 */
	public String getGoodsWeight() {
		return goodsWeight;
	}
	/**
	 * @param goodsWeight the goodsWeight to set
	 */
	public void setGoodsWeight(String goodsWeight) {
		this.goodsWeight = goodsWeight;
	}
	/**
	 * @return the collection
	 */
	public Address getCollection() {
		return collection;
	}
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Address collection) {
		this.collection = collection;
	}
	/**
	 * @return the delivery
	 */
	public Address getDelivery() {
		return delivery;
	}
	/**
	 * @param delivery the delivery to set
	 */
	public void setDelivery(Address delivery) {
		this.delivery = delivery;
	}
}

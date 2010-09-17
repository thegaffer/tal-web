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

package org.talframework.talui.mvc.model.message;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Simple and default implementation of the 
 * {@link ClientMessage} interface.
 * 
 * @author Tom Spencer
 */
public final class ClientMessageImpl implements ClientMessage, Serializable {
	private final static long serialVersionUID = 1L;

	/** Holds the field the message applies to */
	private final String field;
	/** Holds the code of the message */
	private final String code;
	/** Holds the parameters of the message (if any) */
	private final Object[] params;
	
	public ClientMessageImpl(String code) {
		this.field = null;
		this.code = code;
		this.params = null;
	}
	
	public ClientMessageImpl(String code, Object[] params) {
		this.field = null;
		this.code = code;
		this.params = params;
	}
	
	public ClientMessageImpl(String field, String code) {
		this.field = field;
		this.code = code;
		this.params = null;
	}
	
	public ClientMessageImpl(String field, String code, Object[] params) {
		this.field = field;
		this.code = code;
		this.params = params;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field != null ? field : "global";
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}
	
	@Override
	public String toString() {
		return "Message: field=" + getField() + ", code=" + getCode() + ", params=" + getParams();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + Arrays.hashCode(params);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientMessageImpl other = (ClientMessageImpl) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (!Arrays.equals(params, other.params))
			return false;
		return true;
	}
}

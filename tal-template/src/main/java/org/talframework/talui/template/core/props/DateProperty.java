/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.core.props;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.template.RenderModel;

/**
 * Extends simple property for date fields allowing 
 * optional format (either as a pattern or as 
 * DateFormat date and time styles).
 * 
 * @author Tom Spencer
 */
public class DateProperty extends SimpleProperty implements org.talframework.talui.template.behaviour.property.DateProperty {
	private static final Log logger = LogFactory.getLog(DateProperty.class);

	/** Holds the date style to output (if -1 date is not output) */
	private String dateStyle = "short";
	/** The converted date style */
	private short dateFormatStyle = DateFormat.SHORT;
	/** Holds the custom date pattern (if there is one) */
	private String datePattern = null;
	/** Internally set if there is a date pattern */
	private SimpleDateFormat datePatternFormatter = null;
	/** Holds the time style to output (if -1 time is not output) */
	private String timeStyle = null;
	/** The converted date style */
	private short timeFormatStyle = -1;
	/** Holds the custom time pattern (if there is one) */
	private String timePattern = null;
	/** Internally set if there is a time pattern */
	private SimpleDateFormat timePatternFormatter = null;
	
	@Override
	public String getType() {
		// Possibly change if we are a time-prop or datetime-prop?
		return "date-prop";
	}
	
	/**
	 * Formats the value as a date, time or date and time
	 */
	@Override
	public String getValue(RenderModel model) {
		String dt = null;
		if( datePatternFormatter != null || dateFormatStyle >= 0 ) {
			dt = getDateValue(model);
		}
		
		String tm = null;
		if( timePatternFormatter != null || timeFormatStyle >= 0 ) {
			tm = getTimeValue(model);
		}
		
		if( dt == null && tm == null ) return null;
		else if( dt != null && tm == null ) return dt;
		else if( dt == null && tm != null ) return tm;
		else return model.getTempBuffer().append(dt).append(' ').append(tm).toString();
	}
	
	/**
	 * Formats only the date component
	 */
	public String getDateValue(RenderModel model) {
		Object val = model.getCurrentNode().getProperty(getName());
		if( val instanceof Date ) {
			Locale l = model.getLocale() != null ? model.getLocale() : Locale.getDefault();
			
			if( datePatternFormatter != null ) {
				return datePatternFormatter.format(val);
			}
			else {
				int style = dateFormatStyle;
				if( style < 0 ) {
					logger.info("A date value has been requested at render time, but no date format is set: " + getName());
					style = DateFormat.SHORT;
				}
				DateFormat df = DateFormat.getDateInstance(style, l);
				return df.format(val);
			}
		}
		else {
			return val != null ? val.toString() : null;
		}
	}
	
	/**
	 * Formats only the time component
	 */
	public String getTimeValue(RenderModel model) {
		Object val = model.getCurrentNode().getProperty(getName());
		if( val instanceof Date ) {
			Locale l = model.getLocale() != null ? model.getLocale() : Locale.getDefault();
			
			if( timePatternFormatter != null ) {
				return timePatternFormatter.format(val);
			}
			else {
				int style = timeFormatStyle;
				if( style < 0 ) {
					logger.info("A time value has been requested at render time, but no time format is set: " + getName());
					style = DateFormat.SHORT;
				}
				DateFormat df = DateFormat.getTimeInstance(style, l);
				return df.format(val);
			}
		}
		else {
			return val != null ? val.toString() : null;
		}
	}
	
	public boolean isDate() {
		return dateStyle != null; 
		
	}
	
	public boolean isTime() {
		return timeStyle != null;
	}
	
	public String getDatePattern(RenderModel model) {
		return datePattern;
	}
	
	public String getDateFormat(RenderModel model) {
		return dateStyle;
	}
	
	public String getTimePattern(RenderModel model) {
		return timePattern;
	}
	
	public String getTimeFormat(RenderModel model) {
		return timeStyle;
	}
	
	/**
	 * @return the dateStyle
	 */
	public String getDateStyle() {
		return dateStyle;
	}

	/**
	 * @param dateStyle the dateStyle to set
	 */
	public void setDateStyle(String dateStyle) {
		this.dateStyle = dateStyle;
		if( "full".equals(dateStyle) ) dateFormatStyle = DateFormat.FULL;
		else if( "long".equals(dateStyle) ) dateFormatStyle = DateFormat.LONG;
		else if( "medium".equals(dateStyle) ) dateFormatStyle = DateFormat.MEDIUM;
		else if( "short".equals(dateStyle) ) dateFormatStyle = DateFormat.SHORT;
		else dateFormatStyle = -1;
	}

	/**
	 * @return the datePattern
	 */
	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * @param datePattern the datePattern to set
	 */
	public void setDatePattern(String datePattern) {
		this.datePatternFormatter = null;
		this.datePattern = datePattern;
		if( datePattern != null ) datePatternFormatter = new SimpleDateFormat(datePattern);
	}

	/**
	 * @return the timeStyle
	 */
	public String getTimeStyle() {
		return timeStyle;
	}

	/**
	 * @param timeStyle the timeStyle to set
	 */
	public void setTimeStyle(String timeStyle) {
		this.timeStyle = timeStyle;
		if( "long".equals(timeStyle) ) timeFormatStyle = DateFormat.LONG;
		else if( "short".equals(timeStyle) ) timeFormatStyle = DateFormat.SHORT;
		else timeFormatStyle = -1;
	}

	/**
	 * @return the timePattern
	 */
	public String getTimePattern() {
		return timePattern;
	}

	/**
	 * @param timePattern the timePattern to set
	 */
	public void setTimePattern(String timePattern) {
		this.timePatternFormatter = null;
		this.timePattern = timePattern;
		if( timePattern != null ) timePatternFormatter = new SimpleDateFormat(timePattern);
	}
}

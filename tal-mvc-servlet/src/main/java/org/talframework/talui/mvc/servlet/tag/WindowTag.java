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

package org.talframework.talui.mvc.servlet.tag;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.PageConfig;
import org.talframework.talui.mvc.config.WindowConfig;
import org.talframework.talui.mvc.servlet.util.RequestAttributeUtils;

/**
 * This JSP tag outputs a window that re-directs to a 
 * Window in the current page/app. The current page
 * and app must be in the request scope.
 * 
 * <p>The HTML generates is</p>
 * 
 * <code>
 * <div id="name-wrapper">
 *   ... If there is a header ...
 *   <div id="name-header">
 *       <div id="name-title">Window Name</div>
 *       <div id="name-actions" style="float:right">
 *         ... Body here for the possible window actions ...
 *       </div>
 *   </div>
 *   
 *   <div id="name" refreshWindow="context/app/asyncRender/page/window">
 *   	... Redirect to content, or JS to make render on load ...
 *   </div>
 * </div>
 * </code>
 * 
 * @author Tom Spencer
 */
public class WindowTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	/** The name of the window to re-direct to */
	private String window = null;
	/** The style for the outer div holding window (optional) */
	private String style = null;
	/** The style for an outer div if required */
	private String outerStyle = null;
	/** Determines if all rendering is asynchronous */
	private boolean initialAsync = false;
	/** Determines if there is a header element to this window */
	private boolean header = false;
	

	@Override
	public int doStartTag() throws JspException {
		AppConfig app = (AppConfig)pageContext.findAttribute(RequestAttributeUtils.APP);
		if( app == null ) throw new JspException("Cannot use WindowTag if not in a Web MVC environment. App is missing");
		PageConfig page = (PageConfig)pageContext.findAttribute(RequestAttributeUtils.PAGE);
		if( page == null ) throw new JspException("Cannot use WindowTag if not in a Web MVC environment. Page is missing");
		WindowConfig windowConfig = page.getWindow(window);
		if( page == null ) throw new JspException("Cannot use WindowTag if not in a Web MVC environment. Window is missing");

		StringBuilder builder = new StringBuilder();
		
		if( style == null ) style = "window";
		
		if( outerStyle != null ) builder.append("<div class=\"").append(outerStyle).append("\">\n");
		builder.append("<div id=\"").append(windowConfig.getNamespace()).append("-wrapper\" class=\"").append(style).append("\">\n");
		if( header ) {
			builder.append("  <div id=\"").append(windowConfig.getNamespace()).append("-header\" class=\"").append(style).append("-header\">");
			builder.append("<span id=\"").append(windowConfig.getNamespace()).append("-title\" class=\"").append(style).append("-title\">Window Name</span>");
			builder.append("<span id=\"").append(windowConfig.getNamespace()).append("-actions\" class=\"").append(style).append("-actions\">");
			
			builder.append("<span id=\"").append(windowConfig.getNamespace()).append("\" class=\"window-refresh\">&nbsp;</span>");
		}
		
		try {
			pageContext.getOut().write(builder.toString());
		}
		catch( IOException e ) {
			throw new JspException("Failed to output window tag start", e);
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		AppConfig app = (AppConfig)pageContext.findAttribute(RequestAttributeUtils.APP);
		PageConfig page = (PageConfig)pageContext.findAttribute(RequestAttributeUtils.PAGE);
		WindowConfig windowConfig = page.getWindow(window);
		
		try {
			StringBuilder builder = new StringBuilder();
			
			builder.append(((HttpServletRequest)pageContext.getRequest()).getContextPath());
			builder.append("/").append(app.getName());
			builder.append("/").append("asyncRender");
			builder.append("/").append(page.getName());
			builder.append("/").append(window);
			String refreshUrl = builder.toString();
			
			builder.setLength(0);
			builder.append("/").append(app.getName());
			builder.append("/").append("render");
			builder.append("/").append(page.getName());
			builder.append("/").append(window);
			String renderUrl = builder.toString();
			
			builder.setLength(0);
			if( header ) {
				builder.append("</span>");
				builder.append("</div>\n");
			}
			
			builder.append("  <div id=\"").append(windowConfig.getNamespace()).append("-content\" refreshWindow=\"");
			builder.append(refreshUrl);
			builder.append("\" class=\"").append(style).append("-content\">\n");
			
			// Add in actual content
			builder.append("  <!-- *** Window Content Starts *** -->\n\n");

			// ... Redirect to content, or JS to make render on load ...
			if( initialAsync ) {
				// TODO: Need ability to localise text
				builder.append("<div id=\"").append(windowConfig.getNamespace()).append("\" class=\"async-panel\">You must have JavaScript enabled to view this panel</div>");
				
				
				/*builder.append("<script type=\"text/javascript\">\n");
				builder.append("refreshMVCWindow('").append(windowConfig.getNamespace()).append("');\n");
				builder.append("</script>");*/
			}
			else {
				pageContext.getOut().write(builder.toString());
				pageContext.getOut().flush();
				pageContext.getServletContext().getRequestDispatcher(renderUrl).include(pageContext.getRequest(), pageContext.getResponse());
				pageContext.getOut().flush();
				builder.setLength(0);
			}
			
			builder.append("\n\n  <!-- *** Window Content Ends *** -->\n");
			builder.append("  </div>\n");
			builder.append("</div>\n");
			if( outerStyle != null ) builder.append("</div>");
		
			pageContext.getOut().write(builder.toString());
		}
		catch( IOException e ) {
			throw new JspException("Failed to output window tag end", e);
		}
		catch( ServletException e ) {
			throw new JspException("Failed to redirect to template", e);
		}
		
		return EVAL_PAGE;
	}

	/**
	 * @return the window
	 */
	public String getWindow() {
		return window;
	}

	/**
	 * @param window the window to set
	 */
	public void setWindow(String window) {
		this.window = window;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the initialAsync
	 */
	public boolean isInitialAsync() {
		return initialAsync;
	}

	/**
	 * @param initialAsync the initialAsync to set
	 */
	public void setInitialAsync(boolean initialAsync) {
		this.initialAsync = initialAsync;
	}

	/**
	 * @return the header
	 */
	public boolean isHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(boolean header) {
		this.header = header;
	}

	/**
	 * @return the outerStyle
	 */
	public String getOuterStyle() {
		return outerStyle;
	}

	/**
	 * @param outerStyle the outerStyle to set
	 */
	public void setOuterStyle(String outerStyle) {
		this.outerStyle = outerStyle;
	}
}

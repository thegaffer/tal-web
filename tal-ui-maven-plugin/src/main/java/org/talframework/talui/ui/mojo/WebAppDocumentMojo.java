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

package org.tpspencer.tal.ui.mojo;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.xml.sax.InputSource;

/**
 * This Mojo will create the documentation for the 
 * application based on a Web Application
 * 
 * @goal webapp-document
 * @phase prepare-package
 */
public class WebAppDocumentMojo extends BaseDocumentMojo {
	
	/**
	 * Holds the path of the application context files
	 * 
	 * @parameter default-value="${basedir}/src/main/webapp/WEB-INF"
	 * @required
	 */
	private File configLocation;
	
	/**
	 * Holds the name of the application to document
	 * 
	 * @parameter
	 * @required
	 */
	private String appName;
	
	/**
	 * Holds the main output directory
	 * 
	 * @parameter default-value="${basedir}/src/main/resources/META-INF/doc/"
	 * @required
	 */
	private File outputDir;
	
	/**
	 * Actual execute method to load the app configuration and
	 * produce the documentation for it.
	 */
    public void execute() throws MojoExecutionException {
    	getLog().info("Documenting app: " + appName);
    	getLog().info("Documenting config: " + configLocation);
    	getLog().info("Documentation Output: " + outputDir);
    	
    	// Load the application context
    	if( configLocation == null || !configLocation.isDirectory() ) {
    		throw new MojoExecutionException("Cannot document as config location is not a directory: " + configLocation);
    	}
    	
    	// Get all Spring files
    	File[] configs = configLocation.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if( name.startsWith("applicationContext") &&
						name.endsWith(".xml") ) {
					return true;
				}
				
				return false;
			}
		});
    	
    	GenericApplicationContext ctx = new GenericApplicationContext();
    	XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
    	xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
    	
    	// Convert the filenames for Spring
    	try {
	    	for( int i = 0 ; i < configs.length ; i++ ) {
				getLog().debug("Reading Application Context: " + configs[i]);
				xmlReader.loadBeanDefinitions(new InputSource(new FileReader(configs[i])));
			}
    	}
    	catch( Exception e ) {
    		throw new MojoExecutionException("Could not read input: " + e.getMessage(), e);
    	}
    	
    	ctx.refresh();
    	
    	super.produceDocuments(ctx, appName, outputDir);
    }
}

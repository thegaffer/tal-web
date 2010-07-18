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

import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This Mojo will create the documentation for the 
 * application based on a standard app config class
 * path resource
 * 
 * @goal app-document
 * @phase prepare-package
 */
public class AppDocumentMojo extends BaseDocumentMojo {
	
	/**
	 * Holds the classpath resource holding the applications
	 * context
	 * 
	 * @parameter
	 * @required
	 */
	private String appConfig;
	
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
	 * @parameter default-value="${basedir}/src/main/resources/doc/"
	 * @required
	 */
	private File outputDir;
	
	/**
	 * Actual execute method to load the app configuration and
	 * produce the documentation for it.
	 */
    public void execute() throws MojoExecutionException {
    	getLog().info("Documenting app: " + appName);
    	getLog().info("Documenting config: " + appConfig);
    	getLog().info("Documentation Output: " + outputDir);
    	
    	ApplicationContext ctx = new ClassPathXmlApplicationContext(appConfig);
    	
    	super.produceDocuments(ctx, appName, outputDir);
    }
}

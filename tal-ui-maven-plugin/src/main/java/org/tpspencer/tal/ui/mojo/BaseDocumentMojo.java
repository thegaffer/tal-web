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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.context.ApplicationContext;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.boq.BillOfQuantityWriter;
import org.tpspencer.tal.mvc.document.compiler.AppCompiler;
import org.tpspencer.tal.mvc.document.spec.SpecificationWriter;

/**
 * This base class abstracts the actual document construction
 * that is used by the WebApp and App documenters.
 * 
 * @author Tom Spencer
 */
public abstract class BaseDocumentMojo extends AbstractMojo {

	public void produceDocuments(ApplicationContext ctx, String appName, File outputDir) throws MojoExecutionException {
		// Get the app
    	AppElement app = AppCompiler.compileApp(appName, ctx);
    	if( app == null ) throw new MojoExecutionException("Application does not exist in config: " + appName);
    	
    	// Make sure the dir exists
    	if( !outputDir.exists() ) outputDir.mkdirs();
    	
    	try {
    		getLog().info("Writing Bill of Quantity");
	    	BillOfQuantityWriter boq = new BillOfQuantityWriter(outputDir + File.separator + "BillOfQuantity.pdf");
			boq.createDocument(app);
	    	
			getLog().info("Writing Specifications");
	    	SpecificationWriter spec = new SpecificationWriter(outputDir + File.separator + "Specification.pdf");
	    	spec.createDocument(app);
    	}
    	catch( Exception e ) {
    		throw new MojoExecutionException("Cannot create documentation: " + e.getMessage(), e);
    	}
	}
}

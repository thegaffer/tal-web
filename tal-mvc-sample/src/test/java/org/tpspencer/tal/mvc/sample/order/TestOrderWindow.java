package org.tpspencer.tal.mvc.sample.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.Window;
import org.tpspencer.tal.mvc.config.PageConfig;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.model.ResolvedModelAttribute;
import org.tpspencer.tal.mvc.window.MultiViewWindow;

/**
 * Tests the order window class
 * 
 * @author Tom Spencer
 */
public class TestOrderWindow {
	
	private Mockery context = new JUnit4Mockery();
	private OrderWindow underTest = null;
	
	@Before
	public void setup() {
		underTest = new OrderWindow();
		
		final ModelResolver currentOrderResolver = context.mock(ModelResolver.class, "currentOrder");
        final ModelResolver ordersResolver = context.mock(ModelResolver.class, "orders");
        context.checking(new Expectations() {{
            allowing(currentOrderResolver).canNestResolver(); will(returnValue(false));
            allowing(ordersResolver).canNestResolver(); will(returnValue(false));
        }});
        
		List<ModelAttribute> attrs = new ArrayList<ModelAttribute>();
		attrs.add(new ResolvedModelAttribute("currentOrder", currentOrderResolver, null, null));
		attrs.add(new ResolvedModelAttribute("orders", ordersResolver, null, null));
		ModelConfiguration config = new ModelConfiguration("mock", attrs);
		underTest.model = config;
		
		underTest.listView = context.mock(View.class, "listView");
		underTest.orderView = context.mock(View.class, "orderView");
		underTest.orderForm = context.mock(View.class, "orderForm");
		underTest.newOrderForm = context.mock(View.class, "newOrder");
		
		underTest.selectOrderView = context.mock(Controller.class, "selectOrderView");
		underTest.selectOrderEdit = context.mock(Controller.class, "selectOrderEdit");
		underTest.selectCreateOrder = context.mock(Controller.class, "selectCreateOrder");
		underTest.submitOrderView = context.mock(Controller.class, "submitOrderView");
		underTest.submitOrderForm = context.mock(Controller.class, "submitOrder");
		underTest.submitNewOrder = context.mock(Controller.class, "submitNewOrder");
	}

	/**
	 * Ensures that the window is configurable
	 */
	@Test
	public void initialise() {
		PageConfig config = new PageConfig();
		Map<String, Object> windows = new HashMap<String, Object>();
		windows.put("test", underTest);
		config.setWindowMap(windows);
		
		// Tests
		Window window = config.getWindow("test").getWindow();
		Assert.assertNotNull(window);
		Assert.assertTrue(window instanceof MultiViewWindow);
		MultiViewWindow mw = (MultiViewWindow)window;
		
		Assert.assertNotNull(mw.getModel());
		Assert.assertNotNull(mw.getModel().getAttributes());
		Assert.assertNotNull(mw.getModel().getAttribute(OrderWindow.STATE));
		Assert.assertNotNull(mw.getModel().getAttribute(OrderWindow.CURRENT_ORDER));
		//Assert.assertNotNull(mw.getModel().getAttribute(OrderWindow.SELECTED_ORDER));
		Assert.assertNotNull(mw.getModel().getAttribute(OrderWindow.ORDERS));
		
		Assert.assertNotNull(mw.getDefaultView());
		Assert.assertNotNull(mw.getViews());
		
		Assert.assertNotNull(mw.getControllers());
		Assert.assertEquals(6, mw.getControllers().size());
		Assert.assertNotNull(mw.getControllers().get("viewOrder"));
		
		Assert.assertNotNull(mw.getActionMappings());
		
	}
}

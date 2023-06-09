package org.springframework.faces.webflow;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.faces.context.PartialViewContext;
import jakarta.faces.context.PartialViewContextWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.execution.View;
import org.springframework.webflow.test.MockRequestContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlowPartialViewContextTests {

	@AfterEach
	public void tearDown() throws Exception {
		RequestContextHolder.setRequestContext(null);
	}

	@Test
	public void testReturnFragmentIds() {
		String[] fragmentIds = new String[] { "foo", "bar" };

		RequestContext requestContext = new MockRequestContext();
		requestContext.getFlashScope().asMap().put(View.RENDER_FRAGMENTS_ATTRIBUTE, fragmentIds);
		RequestContextHolder.setRequestContext(requestContext);

		assertEquals(Arrays.asList(fragmentIds), new FlowPartialViewContext(null).getRenderIds());
	}

	@Test
	public void testNoFragmentIds() {
		final List<String> renderIds = Arrays.asList("foo", "bar");
		FlowPartialViewContext context = new FlowPartialViewContext(new PartialViewContextWrapper() {
			public Collection<String> getRenderIds() {
				return renderIds;
			}

			public void setPartialRequest(boolean isPartialRequest) {
			}

			public PartialViewContext getWrapped() {
				return null;
			}
		});

		RequestContextHolder.setRequestContext(new MockRequestContext());

		assertEquals(renderIds, context.getRenderIds());
	}

	@Test
	public void testReturnFragmentIdsMutable() {
		String[] fragmentIds = new String[] { "foo", "bar" };

		RequestContext requestContext = new MockRequestContext();
		requestContext.getFlashScope().asMap().put(View.RENDER_FRAGMENTS_ATTRIBUTE, fragmentIds);
		RequestContextHolder.setRequestContext(requestContext);

		Collection<String> renderIds = new FlowPartialViewContext(null).getRenderIds();
		renderIds.add("baz");

		assertEquals(Arrays.asList("foo", "bar", "baz"), renderIds);
	}

}

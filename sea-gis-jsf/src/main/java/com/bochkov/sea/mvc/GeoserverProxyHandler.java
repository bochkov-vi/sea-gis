package com.bochkov.sea.mvc;

import org.springframework.webflow.core.FlowException;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.FlowExecutionOutcome;
import org.springframework.webflow.mvc.servlet.AbstractFlowHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bochkov on 28.06.17.
 */
public class GeoserverProxyHandler extends AbstractFlowHandler {

    public String handleExecutionOutcome(FlowExecutionOutcome outcome,
                                         HttpServletRequest request, HttpServletResponse response) {
        if (outcome.getId().equals("bookingConfirmed")) {
            return "/booking/show?bookingId=" + outcome.getOutput().get("bookingId");
        } else {
            return "/hotels/index";
        }
    }

    @Override
    public String getFlowId() {
        return super.getFlowId();
    }

    @Override
    public MutableAttributeMap<Object> createExecutionInputMap(HttpServletRequest request) {
        return super.createExecutionInputMap(request);
    }

    @Override
    public String handleException(FlowException e, HttpServletRequest request, HttpServletResponse response) {
        return super.handleException(e, request, response);
    }
}

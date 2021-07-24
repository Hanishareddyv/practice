package testing.webapp.action;

import net.sourceforge.stripes.mock.MockServletContext;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.controller.DispatcherServlet;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.ContextLoader;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.HashMap;
import java.util.Map;

public class StripesTestFixture {
    private MockServletContext context;

    public void init() {
        context = new MockServletContext("");
        context.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                "classpath:/applicationContext-resources.xml, classpath:/applicationContext-dao.xml," +
                "classpath:/applicationContext-service.xml, /WEB-INF/applicationContext.xml");

        ServletContextListener contextListener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(context);
        contextListener.contextInitialized(event);

        // Add the Stripes Filter
        Map<String, String> filterParams = new HashMap<String, String>();
        filterParams.put("ActionResolver.Class", "testing.webapp.action.ActionResolver");
        filterParams.put("ActionResolver.Packages", "testing.webapp");
        filterParams.put("Interceptor.Classes", "net.sourceforge.stripes.integration.spring.SpringInterceptor");
        filterParams.put("LocalizationBundleFactory.ErrorMessageBundle", "messages");
        filterParams.put("LocalizationBundleFactory.FieldNameBundle", "messages");
        context.addFilter(StripesFilter.class, "StripesFilter", filterParams);

        // Add the Stripes Dispatcher
        context.setServlet(DispatcherServlet.class, "StripesDispatcher", null);
    }

    public MockServletContext getServletContext() {
        init();
        return context;
    }

    public void destroy() {
        for (Filter filter : context.getFilters()) {
          filter.destroy();
        }
    }
}

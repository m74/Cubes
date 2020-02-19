package ru.com.m74.cubes.springboot.example.jdbc;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * @author mixam
 * @since 18.05.16 18:57
 */
public class Initializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Application.class);

        servletContext.addListener(new org.springframework.web.context.ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "dispatcher",
                new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");


//        FilterRegistration.Dynamic filter = addFilter(servletContext, "sessionFilter", OpenSessionInViewFilter.class);
//        filter.setInitParameter("sessionFactoryBeanName", "entityManagerFactory");

//        addFilter(servletContext, "springSecurityFilterChain", DelegatingFilterProxy.class);
//        addFilter(servletContext, "corsFilter", CORSFilter.class, "/*");
    }

//    private FilterRegistration.Dynamic addFilter(ServletContext servletContext, String name, Class<? extends Filter> cls) {
//        FilterRegistration.Dynamic filter = servletContext.addFilter(name, cls);
//        filter.addMappingForUrlPatterns(null, true, "/*");
//        return filter;
//    }
}

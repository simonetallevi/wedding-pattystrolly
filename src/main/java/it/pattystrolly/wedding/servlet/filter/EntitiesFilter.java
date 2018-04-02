package it.pattystrolly.wedding.servlet.filter;

import com.googlecode.objectify.ObjectifyService;
import it.pattystrolly.wedding.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Slf4j
public class EntitiesFilter extends GenericServlet {

    static {
        for (Class<?> c : Utils.getDatastoreClasses("it.pattystrolly.wedding.model")) {
            ObjectifyService.register(c);
        }
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {}
}

package com.groupgames.web.webapp.servlets;

import com.groupgames.web.core.FreemarkerManager;
import com.groupgames.web.core.GameManager;
import com.groupgames.web.webapp.ServletError;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameBaseServlet extends HttpServlet {
    protected String webRootPath;
    protected GameManager gameManager;

    public void init(ServletConfig servletconfig) throws ServletException {
        super.init(servletconfig);

        ServletContext ctx = servletconfig.getServletContext();

        this.webRootPath = ctx.getRealPath("/");
        this.gameManager = GameManager.getInstance();
    }

    void respondWithTemplate(HttpServletResponse resp, String templatePath, Map<String, Object> templateData) {
        try {
            Template template = FreemarkerManager.getInstance(webRootPath).getTemplate(templatePath);

            setBasicHeaders(resp);
            template.process(templateData, resp.getWriter());

        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void respondWithError(HttpServletResponse resp, String templatePath, ServletError error){
        Map<String, Object> templateData = new HashMap<>();
        ServletError.setError(templateData, error);
        respondWithTemplate(resp, templatePath, templateData);
    }

    private void setBasicHeaders(HttpServletResponse resp) {
        resp.setContentType("text/html");
    }

    protected String readBody(HttpServletRequest request) {
        StringBuilder bodyBuffer = new StringBuilder();

        try {
            BufferedReader bodyReader = request.getReader();

            // Read all lines from the body into the bodyBuffer
            String line;
            while ((line = bodyReader.readLine()) != null) {
                bodyBuffer.append(line);
            }
        } catch (IOException e) {
            // Failure to read. Discard partially read data & return null
            e.printStackTrace();
            return null;
        }

        return bodyBuffer.toString();
    }
}

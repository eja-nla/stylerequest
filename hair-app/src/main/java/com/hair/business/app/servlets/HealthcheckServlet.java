package com.hair.business.app.servlets;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Olukorede Aguda on 23/05/2016.
 *
 *
 */
@Singleton
public class HealthcheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String response = "{\"code\":200,\"message\":\"I'm healthy. I Hope you are?\"}";
        resp.setContentType("application/json");
        resp.getOutputStream().print(response);

    }
}

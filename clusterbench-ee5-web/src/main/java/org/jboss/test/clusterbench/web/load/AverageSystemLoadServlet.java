/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.test.clusterbench.web.load;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.test.clusterbench.common.load.AverageSystemLoad;

/**
 * AverageSystemLoadServlet
 * 
 * @author Michal Babacek 
 *
 * This simple servlet is used for stressing the server's CPU.
 * 
 * Usage:
 * 
 * You may GET e.g. this URL: http://localhost:8080/clusterbench/averagesystemload?milliseconds=20000&threads=4
 * By doing so, there will be 4 threads with evil active-loops created.
 * These threads will be running for 20000 milliseconds.
 * 
 * After the aforementioned time, you shall receive a response saying something like:
 * 
 * DONE, I was stressing CPU with 4 evil threads for 20000 milliseconds (including warm-up).
 * 
 * NOTE: Do not forget to set some reasonable time-out on your client...
 * That's it. No more functionality. 
 */
public class AverageSystemLoadServlet extends HttpServlet {
   private static final Logger log = Logger.getLogger(AverageSystemLoadServlet.class.getName());
   private final AverageSystemLoad averageSystemLoad = new AverageSystemLoad();
   
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      int milliseconds = Integer.parseInt(request.getParameter("milliseconds"));
      int numberOfThreads = Integer.parseInt(request.getParameter("threads")); 
      response.setContentType("text/plain");
      String resultLogMessage = averageSystemLoad.spawnLoadThreads(numberOfThreads, milliseconds);
      log.log(Level.INFO,resultLogMessage);
      response.getWriter().print("DONE");
   }

   @Override
   public String getServletInfo() {
      return "By invoking AverageSystemLoadServlet, you stress CPU.";
   }
}

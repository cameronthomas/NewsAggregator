package Login;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Database.DatabaseAPI;
import Database.DatabaseInfo;
import Homepage.GenerateHomepage;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cameronthomas
 */
@WebServlet(urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if(validateLogin(username, password))
        {
            new GenerateHomepage().generatePage(request, response); 
        }
        else
            response.sendRedirect("invalidLogin.html"); 
    }
    
    /**
     *
     * 
     */
    private boolean validateLogin(String username, String password)
    {
        boolean validLogin = false;
        String query = "Select username, password FROM user";
        ResultSet rs = null;
        
        // Variables need for database setup
        DatabaseInfo databaseInfo = new DatabaseInfo();         
        String db_url = databaseInfo.getDb_url();
        String db_username = databaseInfo.getUser();
        String db_password = databaseInfo.getPass();
        
       rs = new DatabaseAPI().readDatabase(db_url, query, db_username, db_password);
        
        // Check if username and password are valid
        try
        {       
            while(rs.next())
            {
                if(rs.getString("username").equals(username) &&
                   rs.getString("password").equals(password))
                {
                    validLogin = true;
                    System.out.print("username: " + rs.getString("username"));
                    System.out.print(", pass: " + rs.getString("password"));
                    break;
                }
            }
        }
        catch(SQLException se){
        //Handle errors for JDBC
        se.printStackTrace();
        } 
        return validLogin;
    }
}
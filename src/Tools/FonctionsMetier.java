/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Entity.Etat;
import Entity.Ticket;
import Entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jbuffeteau
 */
public class FonctionsMetier implements IMetier
{

    private String statutUser;
    
    @Override
    public User GetUnUser(String login, String mdp)
    {
        User u = null;
        try {
            Connection cnx = ConnexionBDD.getCnx();
            PreparedStatement ps = cnx.prepareStatement("select idUser, nomUser, prenomUser, statutUser from users where loginUser = ? and pwdUser = ?");
            ps.setString(1, login);
            ps.setString(2, mdp);
            ResultSet rs = ps.executeQuery();
            rs.next();
            u = new User(rs.getInt("idUser"), rs.getString("nomUser"), rs.getString("prenomUser"),rs.getString("statutUser"));
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    
    }

    @Override
    public ArrayList<Ticket> GetAllTickets()
    {
        ArrayList<Ticket> lesTickets = new ArrayList<>();
        try {
            Connection cnx = ConnexionBDD.getCnx();
            try (PreparedStatement ps = cnx.prepareStatement("select idTicket, nomTicket, numEtat from tickets")) 
            {
                ResultSet rs = ps.executeQuery();
                while (rs.next())
            {
                    Ticket tic = new Ticket(rs.getInt("idTicket"), rs.getString("nomTicket"),rs.getString("numEtat"));
                    lesTickets.add(tic);
            }
            }
            } catch (SQLException ex) 
            {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
            }
        return lesTickets;
    }

    @Override
    public ArrayList<Ticket> GetAllTicketsByIdUser(int idUser)
    {
         ArrayList<Ticket> lesTickets = new ArrayList<>();
        try {
            Connection cnx = ConnexionBDD.getCnx();
            PreparedStatement ps = cnx.prepareStatement("SELECT tick.idTicket, tick.nomTicket, tick.dateTicket, eta.nomEtat from tickets tick, etats eta where numUser = ? and tick.numEtat = eta.idEtat");
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                    Ticket tick = new Ticket(rs.getInt("idTicket"), rs.getString("nomTicket"), rs.getString("dateTicket"), rs.getString("nomEtat"));
                    lesTickets.add(tick);
               }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lesTickets;
        
        
    }

    @Override
    public void InsererTicket(int idTicket, String nomTicket, String dateTicket, int idUser, int idEtat) 
    {
     try {
            Connection cnx = ConnexionBDD.getCnx();
         try (PreparedStatement ps = cnx.prepareStatement("insert into tickets values('"+idTicket+"','"+nomTicket+"','"+dateTicket+"','"+idUser+"','"+idEtat+"')"))
            {
             ps.executeUpdate();
             ps.close();
            }
            } catch (SQLException ex) 
            {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
            }   
        
    }

    @Override
    public void ModifierEtatTicket(int idTicket, int idEtat) 
    {
        try {
            Connection cnx = ConnexionBDD.getCnx();
            PreparedStatement ps = cnx.prepareStatement("update tickets set numEtat = ? where idTicket = ?");
            ps.setInt(1, idEtat);
            ps.setInt(2, idTicket);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public ArrayList<User> GetAllUsers()
    {
        ArrayList<User> lesUsers = new ArrayList<>();
        try     {
            Connection cnx = ConnexionBDD.getCnx();
            PreparedStatement ps = cnx.prepareStatement("select idUser, nomUser, prenomUser, statutUser from users");
                
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    User use = new User(rs.getInt("idUser"), rs.getString("nomUser"), rs.getString("prenomUser"),rs.getString("statutUser"));
                    lesUsers.add(use);
                }
                ps.close();
                }
                catch (SQLException ex) {
                Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
                }
        return lesUsers;   
        
    }

    @Override
    public int GetLastIdTicket()
    {
        int DernId = 0;
        try 
        {
          Connection cnx = ConnexionBDD.getCnx();
          PreparedStatement ps = cnx.prepareStatement("select max(idTicket) as num from tickets");
          ResultSet rs = ps.executeQuery();
          rs.next();
          DernId = rs.getInt("num") + 1;
          ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DernId;
      
    }

    @Override
    public int GetIdEtat(String nomEtat)
    {
        
        int EtatId = 0;
        try 
        {
         Connection cnx = ConnexionBDD.getCnx();
         PreparedStatement ps = cnx.prepareStatement("select idEtat from etats where nomEtat = ?");
         ps.setString(1, nomEtat );
         ResultSet rs = ps.executeQuery();
         rs.next();
         EtatId = rs.getInt("idEtat");
         ps.close();
        } catch (SQLException ex) 
        {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return EtatId;
    }

    @Override
    public ArrayList<Etat> GetAllEtats()
                {
        ArrayList<Etat> lesEtats = new ArrayList<>();
        try     {
            Connection cnx = ConnexionBDD.getCnx();
            try (PreparedStatement ps = cnx.prepareStatement("select * from etats")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    Etat eta = new Etat(rs.getInt("idEtat"), rs.getString("nomEtat"));
                    lesEtats.add(eta);
                }
                ps.close();
                }
                } catch (SQLException ex){
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
                }
        return lesEtats; 
      
                }
}

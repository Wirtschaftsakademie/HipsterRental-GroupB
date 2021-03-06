package wak.system.server;

import wak.objects.Bestellung;
import wak.objects.Produkt;
import wak.system.email.emailservice;
import wak.user.Kunde;

import javax.mail.Session;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Christoph Nebendahl on 13.10.2015.
 * @author Christoph Nebendahl
 */
@WebServlet(name = "Bestelleintragung")
public class Bestelleintragung extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String weiterleitung = "/index.jsp";
        Cookie[] cookies = request.getCookies();
        boolean cookie_vorhanden=false;
        Cookie cook=null;
        if(cookies!=null){
            for(int i=0;i<cookies.length;i++){
                Cookie c = cookies[i];
                if(c.getName().compareTo("id")==0){
                    cook = c;
                    cookie_vorhanden=true;
                    break;
                }else{
                }
            }
        }else{

        }
        if(cookie_vorhanden){
            Kunde kunde = null;
            for(Kunde k:Seitenaufbau.kunde){
                if(k.getUuid().equals(cook.getValue())){
                    kunde = k;
                    //Pr�fen, ob die Daten ge�ndert wurden
                    if(!(k.getVorname().equals(request.getParameter("vorname")))){
                        k.setVorname(request.getParameter("vorname"));
                    }
                    if(!(k.getNachname().equals(request.getParameter("nachname")))){
                        k.setNachname(request.getParameter("nachname"));
                    }
                    if(!(k.getAddr().getStrasse().equals(request.getParameter("strasse")))){
                        k.getAddr().setStrasse(request.getParameter("strasse"));
                    }
                    if(!(k.getAddr().getHausnummer()== Integer.parseInt(request.getParameter("hausnummer")))){
                        k.getAddr().setHausnummer(Integer.parseInt(request.getParameter("hausnummer")));
                    }
                    if(!(k.getAddr().getPlz().equals(request.getParameter("plz")))){
                        k.getAddr().setPlz(request.getParameter("plz"));
                    }
                    if(!(k.getAddr().getOrt().equals(request.getParameter("ort")))){
                        k.getAddr().setOrt(request.getParameter("ort"));
                    }
                    if(!(k.getTelefon().equals(request.getParameter("telefon")))){
                        k.setTelefon(request.getParameter("telefon"));
                    }
                    if(!(k.getHandy().equals(request.getParameter("handy")))){
                        k.setHandy((request.getParameter("handy")));
                    }
                    if(!(k.getEmail().equals(request.getParameter("email")))){
                        k.setEmail(request.getParameter("email"));
                    }

                    ArrayList<Produkt> produkte  = new ArrayList<Produkt>();
                    for(int i:k.getKorb().getProdukt_id()){
                        for(Produkt p: Seitenaufbau.katalog){
                            if(p.getId()==i){
                                produkte.add(p);
                            }
                        }
                    }
                    String von = request.getParameter("von");
                    String bis = request.getParameter("bis");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy' 'HH:mm", Locale.GERMANY);
                    LocalDateTime von_date = LocalDateTime.parse(von, formatter);
                    LocalDateTime bis_date = LocalDateTime.parse(bis, formatter);

                    try{
                        Bestellung b = new Bestellung(k, produkte, Timestamp.valueOf(von_date), Timestamp.valueOf(bis_date));
                        Session session = emailservice.getSession();
                        emailservice.sendZusammenfassung(session, k, b);
                        weiterleitung = "/index.jsp";
                    }catch(Exception e1){
                        weiterleitung = "/jsp/error.jsp";
                    }

                }

            }

        }
        RequestDispatcher d = getServletContext().getRequestDispatcher(weiterleitung);
        d.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

package wak.system.server;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import wak.objects.Bestellung;
import wak.objects.Kategorie;
import wak.objects.Produkt;
import wak.objects.Warenkorb;
import wak.system.Formatter;
import wak.system.db.DB_Connector;
import wak.user.Adresse;
import wak.user.Kunde;
import wak.user.Mitarbeiter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by chris_000 on 24.09.2015.
 */
public class Seitenaufbau extends HttpServlet{
   public static  ArrayList<Warenkorb> koerbe = new ArrayList<Warenkorb>();
    public static ArrayList<Kunde> kunde = new ArrayList<Kunde>();
    public static ArrayList<Mitarbeiter> mitarbeiter = new ArrayList<Mitarbeiter>();
    public static ArrayList<Produkt> katalog = new ArrayList<Produkt>();
    public static ArrayList<Kategorie> kategorien = new ArrayList<Kategorie>();
    public static ArrayList<Bestellung> bestellungen = new ArrayList<Bestellung>();

    public static void getEmpfehlungen(JspWriter stream ){
        DB_Connector.connecttoDatabase();

        String produkte_string = "SELECT id,name, bezeichnung, mietzins FROM produkt ORDER BY id";
        PreparedStatement produkte_ps = null;
        ResultSet produkte_rs;

        try {
            produkte_ps = DB_Connector.con.prepareStatement(produkte_string);
           produkte_rs = produkte_ps.executeQuery();

            int zaehler=0;
            for(int i=0;i<5;i++){
                stream.print("<tr>\n");
                for(int ii=0;ii<3;ii++){
                    if(produkte_rs.next()) {
                        int id = produkte_rs.getInt("id");
                        String name = produkte_rs.getString("name");
                        String bezeichnung = produkte_rs.getString("bezeichnung");
                        Double mietzins = produkte_rs.getDouble("mietzins");
                        String mietzins_string = Formatter.formatdouble(mietzins);
                        stream.print("<td onmouseover=this.style.background=\"#FCFD7A\" onmouseout=this.style.background=\"#FCFD5A\" style=\"width:33%; align:center; border:solid 1px #000000\" onclick=self.location.href=\"./jsp/artikel.jsp?id=" + id + "\">");
                        stream.print("<table style=\"max-width:100%\" border=0 ><tr><td colspan=\"2\">" +
                                name +
                                "</td></tr><tr><td rowspan=\"2\" style=\" min-width:30pt; max-width:30pt; min-height:30pt ; max-height:30pt\">" +
                                "<img  src=\"data:image/jpg;base64,"+ ImageServlet.getImage(id, 3)+"\" >" +
                                "</td><td>" +
                                bezeichnung +
                                "</td></tr><tr><td>" +
                                mietzins_string + "" +
                                "</td></tr></table>");

                        stream.print("</td>\n");
                        zaehler++;
                    }
                }
                stream.print("</tr>\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getMenu(JspWriter writer, Cookie[] cookies){
        DB_Connector.connecttoDatabase();
        String produkt_string = "SELECT name, bezeichnung, mietzins  FROM produkt WHERE id=?";
        PreparedStatement produkt_ps = null;
        String tabelle_anfang= "<td><table border=0 style=\"width:100%\">";
        ResultSet produkt_rs;
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
        DB_Connector.closeDatabase();
        Mitarbeiter arbeiter=null;
        for(Mitarbeiter m: mitarbeiter){
            if(cookie_vorhanden){
            if(m.getUuid().toString().equals(cook.getValue())){
                arbeiter=m;
                break;
            }}
        }
        try{

            writer.print("<table><tr><td onclick=self.location.href=\"../index.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Shop</td>" +
                    "<td onclick=self.location.href=\"../jsp/warenkorb.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Warenkorb </td>" +
                    "<td onclick=self.location.href=\"/jsp/Buchungen.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Buchung</td>" +
                    "<td onclick=self.location.href=\"/jsp/Profil.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Profil</td>");
            if(arbeiter!=null){
                writer.print("<td onclick=self.location.href=\"../jsp/mitarbeiter/Uebersicht-Mitarbeiter.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Mitarbeiterbereich </td>");
            }
            writer.print("</tr></table>");
        }catch (IOException e){

        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getMitarbeiterMenu(JspWriter writer, Cookie[] cookies){
        DB_Connector.connecttoDatabase();
        String produkt_string = "SELECT name, bezeichnung, mietzins  FROM produkt WHERE id=?";
        PreparedStatement produkt_ps = null;
        String tabelle_anfang= "<td><table border=0 style=\"width:100%\">";
        ResultSet produkt_rs;
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

        Mitarbeiter arbeiter=null;
        for(Mitarbeiter m: mitarbeiter){
            if(m.getUuid().toString().equals(cook.getValue())){
                arbeiter=m;
            }
        }
        try{

            writer.print("<tr><td onclick=self.location.href=\"./neues_produkt.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Neuer Artikel</td></tr><tr>" +
                    "<td  style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Neues Paket</td></tr><tr>" +
                    "<td onclick=self.location.href=\"./bestelluebersicht.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Bestell&uumlbersicht</td></tr><tr>" +
                    "<td onclick=self.location.href=\"./neueBestellung.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Neue Bestellung</td></tr><tr>"+
                    "<td onclick=self.location.href=\"./KategorieAnlegen.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Neue Kategorie</td></tr><tr>"+
                    "<td onclick=self.location.href=\"./GeraetEinfuegen.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Neues Ger&aumlt</td></tr><tr>");

            if (arbeiter != null) {


            if(arbeiter.isAdmin()){
            writer.print(
                    "<td onclick=self.location.href=\"./Neuer-Mitarbeiter.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Neuer Mitarbeiter</td></tr><tr>"+
                "<td onclick=self.location.href=\"./MitarbeiterUebersicht.jsp\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\">Mitarbeiter &Uml;bersicht</td>");
            }}
                writer.print("</tr>");
        }catch (IOException e){

        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getKategorie(JspWriter writer){

        try {

            DB_Connector.connecttoDatabase();

        String kategorie_string = "SELECT name,id FROM kategorie WHERE oberkategorie is null ORDER BY name";
        PreparedStatement kategorie_ps = null;
        ResultSet kategorie_rs;
        //Vorbereiten der Bestellung f�r die Datenbank

            kategorie_ps = DB_Connector.con.prepareStatement(kategorie_string);
            kategorie_rs = kategorie_ps.executeQuery();
            while(kategorie_rs.next()){
                String kategorie= kategorie_rs.getString("name");
                int id= kategorie_rs.getInt("id");
                writer.print("<tr><td onclick=self.location.href=\"../jsp/kategorie.jsp?katid="+id+"\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#FCFD5A\" onmouseout=this.style.color=\"#000000\" >" +
                        kategorie+
                        "</td></tr>");
            }
        }catch(SQLException e){

        }catch(IOException e1){

        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getArtikel(JspWriter writer,String id){
       try {
           int int_id = Integer.parseInt(id);
           DB_Connector.connecttoDatabase();

           String produkt_string = "SELECT name, bezeichnung, details, beschreibung, mietzins, Kategorieid,hersteller_name FROM produkt WHERE id=?";
           PreparedStatement produkt_ps = null;

           ResultSet produkt_rs;

           produkt_ps = DB_Connector.con.prepareStatement(produkt_string);
           produkt_ps.setInt(1,int_id);
           produkt_rs = produkt_ps.executeQuery();
           produkt_rs.next();
           String name= produkt_rs.getString("name");
           String beschreibung = produkt_rs.getString("beschreibung");
           String details = produkt_rs.getString("details");
           String bezeichnung = produkt_rs.getString("bezeichnung");
           String hersteller = produkt_rs.getString("hersteller_name");
           Double mietzins = produkt_rs.getDouble("mietzins");
           String mietzins_string = Formatter.formatdouble(mietzins);
           writer.print("<td style=\"width:33%; align:center; border:solid 1px #000000\" >");
           writer.print("<table border=0 width=100%><th colspan=4>"+name+"</th><tr><td rowspan =4 style=\"min-width=300pt; min-height=300pt\"> Bild</td>" +
                   "<td width=\"100\">Beschreibung:</td><td>"+beschreibung+"</td><td style:\"text-align:right\" onmouseover=this.style.background=\"#6565FC\" onmouseout=this.style.background=\"#FCFD5A\" onclick=self.location.href=\"../jsp/warenkorb.jsp?addid=" + int_id + "\">Warenkorb</td></tr><tr>" +
                   "<td width=\"100\">Miete:</td><td>"+mietzins_string+"</td><td><div class=\"fb-share-button\" data-href=\"http://www.hipster-rental.de/jsp/artikel?id="+int_id+"\" data-layout=\"button\"></div></tr><tr>" +
                   "<td width=\"100\">Bezeichnung:</td><td>"+bezeichnung+"</td></tr><tr>" +
                   "<td width=\"100\">Hersteller:</td><td>"+hersteller+"</table>");
       }catch(IOException e){

       } catch (SQLException e) {
        }finally {
           DB_Connector.closeDatabase();
       }
    }
    public static void getWarenkorb(JspWriter writer,Cookie[] cookies, String produkt_id){
        DB_Connector.connecttoDatabase();
        String produkt_string = "SELECT name, bezeichnung, mietzins  FROM produkt WHERE id=?";
        PreparedStatement produkt_ps = null;
        String tabelle_anfang= "<td><table border=0 style=\"width:100%\">";
        ResultSet produkt_rs;
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
        try {
            if (cookie_vorhanden) {
               for(Warenkorb b: koerbe){
                   if(b.getUuid().equals(cook.getValue())){
                       if(produkt_id!=null) {
                           b.addprodukt(Integer.parseInt(produkt_id));
                       }
                        writer.print(tabelle_anfang);
                       if(b.getProdukt_id().size()==1){
                           writer.print("<td style=\"text-align: center\">Es ist 1 Produkt im Warenkorb</td>");
                       }else {
                           writer.print("<td style=\"text-align: center\">Es sind " + b.getProdukt_id().size() + " Produkte im Warenkorb</td>");
                       }
                        double summe=0.00;
                       for(Integer produkt: b.getProdukt_id()){
                           produkt_ps = DB_Connector.con.prepareStatement(produkt_string);
                           produkt_ps.setInt(1,produkt);
                           produkt_rs = produkt_ps.executeQuery();
                           produkt_rs.next();
                           String name= produkt_rs.getString("name");
                           String bezeichnung = produkt_rs.getString("bezeichnung");
                           Double mietzins = produkt_rs.getDouble("mietzins");
                           summe+=mietzins;
                           String mietzins_string = Formatter.formatdouble(mietzins);
                           writer.print("<tr><td style=\"width:33%; align:center; border:solid 1px #000000\"><table style=\"width:100%\"><th colspan=\"2\" align=left>"+name+"</th><tr><td style=\" max-width: 200px;\">Bezeichnung:</td><td>"+bezeichnung+"</td><td align=right>"+mietzins_string+"</td></tr></table></td></tr>");
                       }
                       String summe_string = Formatter.formatdouble(summe);
                       writer.print("<tr><td><table style=\"width:100%\"><td>Summe</td><td align=\"right\">"+summe_string+"</td></table></td></tr>");
                       writer.print("</td></tr></table>");
                       writer.print("<table border=0 width\"100%\"><tr><td width=\"90%\"></td><td><form action=\"/Bestellung\" method=\"post\"><input type=submit value=\"Kostenpflichtig bestellen\" name=\"Registrieren\"");
                       if(b.getProdukt_id().size()==0){
                           writer.print("disabled");
                       }
                       writer.print("></form></td><td><form action=\"/clear\" method=\"post\"><input type=submit value=\"Warenkorb leeren\" name=\"clear\"></form></td></tr></table> ");
                       writer.print("</td>");
                   }
               }
            } else {
                if(produkt_id==null){
                   writer.print(tabelle_anfang);
                    writer.print("<th style=\"text-align: center\">Es sind keine Produkte ausgewaehlt.</th>");
                    writer.print("</table>");
                }else{
                    //Fall, falls keine Cookie gefunden wurde.
                }
            }
        }catch(IOException e){

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getKategorieArtikel(JspWriter writer,String kat_id){
        DB_Connector.connecttoDatabase();

        String kategorien_string = "Select id,name FROM kategorie WHERE oberkategorie=? Order BY id";
        PreparedStatement kategorien_ps = null;
        ResultSet kategorien_rs;
        String produkte_string = "SELECT id,name, bezeichnung, mietzins FROM produkt WHERE Kategorieid=? ORDER BY id";
        PreparedStatement produkte_ps = null;
        ResultSet produkte_rs;

        try {
            kategorien_ps = DB_Connector.con.prepareStatement(kategorien_string);
            kategorien_ps.setInt(1,Integer.parseInt(kat_id));
            kategorien_rs = kategorien_ps.executeQuery();
            while(kategorien_rs.next()){

                int kategorie_id = kategorien_rs.getInt("id");
                String kategorie_name = kategorien_rs.getString("name");
                produkte_ps = DB_Connector.con.prepareStatement(produkte_string);
                produkte_ps.setInt(1,kategorie_id);
                produkte_rs = produkte_ps.executeQuery();

                int zaehler=0;
                String kat_name =".";
                for(int i=0;i<5;i++){
                    writer.print("<tr>\n");
                    if(!kat_name.equals(kategorie_name)){
                        writer.print("<td><b>"+kategorie_name+"</b></td></tr><tr>\n");
                        kat_name=kategorie_name;
                    }

                    for(int ii=0;ii<3;ii++){
                        if(produkte_rs.next()) {
                            int id = produkte_rs.getInt("id");
                            String name = produkte_rs.getString("name");
                            String bezeichnung = produkte_rs.getString("bezeichnung");
                            Double mietzins = produkte_rs.getDouble("mietzins");
                            String mietzins_string = Formatter.formatdouble(mietzins);
                            writer.print("<td onmouseover=this.style.background=\"#FCFD7A\" onmouseout=this.style.background=\"#FCFD5A\" style=\"width:33%; align:center; border:solid 1px #000000\" onclick=self.location.href=\"./artikel.jsp?id=" + id + "\">");
                            writer.print("<table style=\"max-width:100%\" border=0 ><tr><td colspan=\"2\">" +
                                    name +
                                    "</td></tr><tr><td rowspan=\"2\" style=\" min-width:30pt; max-width:30pt; min-height:30pt ; max-height:30pt\">" +
                                    "Bild" +
                                    "</td><td>" +
                                    bezeichnung +
                                    "</td></tr><tr><td>" +
                                    mietzins_string + "" +
                                    "</td></tr></table>");

                            writer.print("</td>\n");
                            zaehler++;
                        }
                    }
                    writer.print("</tr>\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getBestellung(JspWriter writer, Cookie[] cookies){
        try {
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
                DB_Connector.connecttoDatabase();
                writer.print("<td>");
                Kunde k = getKunde(cook.getValue());
                String vorname=" ", nachname=" ", strasse=" ",  plz=" ", ort=" ", telefon=" ", handy=" ", email = " ";
                int hausnummer=0;

                    String kunde_string = "SELECT nutzer.vorname, nutzer.nachname, kunde.strasse, kunde.hausnummer, kunde.plz, kunde.ort, kunde.telefonnummer, kunde.handynummer, kunde.email FROM kunde Inner join nutzer on kunde.Nutzerid=nutzer.id WHERE kunde.Nutzerid=?";
                    PreparedStatement kunde_ps = null;
                    ResultSet kunde_rs;
                    kunde_ps = DB_Connector.con.prepareStatement(kunde_string);
                    kunde_ps.setString(1, k.getId());
                    kunde_rs = kunde_ps.executeQuery();
                    kunde_rs.next();
                     vorname = kunde_rs.getString("vorname");
                     nachname = kunde_rs.getString("nachname");
                     strasse = kunde_rs.getString("strasse");
                     hausnummer = kunde_rs.getInt("hausnummer");
                     plz = kunde_rs.getString("plz");
                     ort = kunde_rs.getString("Ort");
                     telefon = kunde_rs.getString("telefonnummer");
                     handy = kunde_rs.getString("handynummer");
                     email = kunde_rs.getString("email");
                    k.setAddr(new Adresse(strasse,ort, plz, hausnummer));
                    k.setEmail(email);
                    k.setTelefon(telefon);
                    k.setHandy(handy);
                    k.setVorname(vorname);
                    k.setNachname(nachname);

                writer.print("<form action=\"/Bestelleintragung\" method=\"post\"><table width=100%><tr><td>Vorname</td><td><input type=\"text\" name=\"vorname\" value=\""+vorname+"\"></td><td rowspan=\"12\" valign=\"top\">"+getWarenkorbTabelle(k.getUuid())+"</td></tr>");
                writer.print("<tr><td>Nachname</td><td><input type=text name=nachname value="+nachname+"></td></tr>");
                writer.print("<tr><td>Stra&#223;e</td><td><input type=text name=strasse value="+strasse+"></td></tr>");
                writer.print("<tr><td>Hausnummer</td><td><input type=text name=hausnummer value="+hausnummer+"></td></tr>");
                writer.print("<tr><td>PLZ</td><td><input type=text name=plz value="+plz+"></td></tr>");
                writer.print("<tr><td>Ort</td><td><input type=text name=ort value="+ort+"></td></tr>");
                writer.print("<tr><td>Telefon</td><td><input type=text name=telefon value="+telefon+"></td></tr>");
                writer.print("<tr><td>Handy</td><td><input type=text name=handy value="+handy+"></td></tr>");
                writer.print("<tr><td>E-Mail</td><td><input type=text name=email value="+email+"></td></tr>");
                writer.print("<tr><td>Von</td><td><input type=\"Text\" name=\"von\" id=\"von\" ><img src=\"../img/calender/cal.gif\" onclick=\"javascript:NewCssCal('von','ddMMyyyy','arrow', 'true', '24')\" style=\"cursor:pointer\"/></td></tr>");
                writer.print("<tr><td>Bis</td><td><input type=\"Text\"  name=\"bis\"id=\"bis\"/><img src=\"../img/calender/cal.gif\" onclick=\"javascript:NewCssCal('bis','ddMMyyyy','arrow', 'true', '24')\" style=\"cursor:pointer\"/>      </td></tr>");
                writer.print("<tr><td colspan=2><input type=submit value=\"Kostenpflichtig bestellen\" name=\"bestellen\"><input type=submit value=\"abbrechen\"></td></tr>");
                writer.print("</table></form>");
            }
            writer.print("</td>");
        }catch(IOException e){

        }catch(SQLException e1){
            try{writer.print(e1);}catch(IOException e2){}
        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getBestelluebersicht(JspWriter writer, Cookie[] cookies){
        //Cookie abfragen
        DB_Connector.connecttoDatabase();



        try {

            writer.print("<td><table width=100%><tr><td>Bestellnummer</td><td>von</td><td>bis</td><td>Mietzins</td><td>Annehmen</td><td>Ablehnen</td></tr>");
            String bestell_string = "select Bestellungid, von, bis ,gesamtkosten from (select Bestellungid ,round(sum(mietzins), 2) as Gesamtkosten from bestellposition inner join produkt ON(bestellposition.Produktid = produkt.id) group by Bestellungid) as temp inner join bestellung ON(Bestellungid = bestellung.id) where genehmigt = 0;";
            PreparedStatement bestell_ps = null;
            ResultSet bestell_rs;
            bestell_ps = DB_Connector.con.prepareStatement(bestell_string);
            bestell_rs = bestell_ps.executeQuery();

            while(bestell_rs.next()){
                int bestellid = bestell_rs.getInt("Bestellungid");
                Date von = bestell_rs.getDate("von");
                Date bis = bestell_rs.getDate("bis");
                double mietzins = bestell_rs.getDouble("gesamtkosten");
                writer.print("<tr><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid="+bestellid+"\">"+bestellid+"</td><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid="+bestellid+"\">"+von.toString()+"</td><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid="+bestellid+"\">"+bis.toString()+"</td><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid="+bestellid+"\">"+Formatter.formatdouble(mietzins)+"</td><td  onclick=self.location.href=\"./Bestellgenehmigung?bestellid="+bestellid+"&genehmigt=1\">Annehmen</td><td  onclick=self.location.href=\"./Bestellgenehmigung?bestellid="+bestellid+"&genehmigt=0\">Ablehnen</td></tr>");
            }
            writer.print("</table></td>");

        }catch(IOException e1){


        }catch(SQLException e2){
            try {
                writer.print(e2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally{
            DB_Connector.closeDatabase();
        }

    }
    public static void getBestelldetails(JspWriter writer, Cookie[] cookies, String bestellid){
        //Cookie �berpr�fen
        DB_Connector.connecttoDatabase();


        try{
            writer.print("<td><table width=\"100%\"");
            //Allgemeine Informationen
            String info_string = "SELECT bestellung.id, bestellung.Nutzerid,bestellung.genehmigt,  bestellung.von, nutzer.vorname, nutzer.nachname ,bestellung.bis, kunde.email, kunde.organame, kunde.strasse, kunde.hausnummer, kunde.plz, kunde.ort, kunde.telefonnummer, kunde.handynummer FROM softwareengineering2.bestellung inner join kunde ON(bestellung.Nutzerid=kunde.Nutzerid) inner join nutzer on(bestellung.Nutzerid=nutzer.id) WHERE bestellung.id=?;\n";
            PreparedStatement info_ps = DB_Connector.con.prepareStatement(info_string);
            info_ps.setInt(1, Integer.parseInt(bestellid));
            ResultSet info_rs = info_ps.executeQuery();
            info_rs.next();
            String nutzerid = info_rs.getString("Nutzerid");
            String vorname = info_rs.getString("vorname");
            String nachname = info_rs.getString("nachname");
            String email = info_rs.getString("email");
            Timestamp von = info_rs.getTimestamp("von");
            Time von_time = info_rs.getTime("von");
            Timestamp bis = info_rs.getTimestamp("bis");
            Time bis_time = info_rs.getTime("bis");
            int genehmigt = info_rs.getInt("genehmigt");
            int tage = getTage(von, bis);

            writer.print("<th colspan=4 align=center><b>Bestellnummer: "+bestellid+"</b></th>");
            writer.print("<tr><td class=\"umrandung\"><b>Kundennummer:</b> "+nutzerid+"</td><td class=\"umrandung\"><b>Vorname: </b>"+vorname+"</td><td class=\"umrandung\"><b>Nachname: </b>"+nachname+"</td><td class=\"umrandung\"> <b>E-Mail: </b>"+email+"</td></tr>" +
                   "<tr><td class=\"umrandung\">Von: "+ Formatter.dateFormatter(von)+"</td><td class=\"umrandung\">Bis: "+Formatter.dateFormatter(bis)+"</td>");
            if(genehmigt>=1){
                writer.print("<td></td><td></td></tr>");
            }else{
                writer.print("<td onclick=self.location.href=\"./Bestellgenehmigung?bestellid="+bestellid+"&genehmigt=1\" class=\"umrandung\" >Genehmigen</td><td onclick=self.location.href=\"./Bestellgenehmigung?bestellid="+bestellid+"&genehmigt=0\" class=\"umrandung\">Ablehnen</td></tr>");
            }
            writer.print("<tr><td class\"umrandung\">Anzahl Tage: "+tage+"</td></tr>");
            writer.print("</table><table width=100%>");
            //Tabelle der Positionen
            writer.print("<tr><td><b>Pos.</b></td><td><b>Name</b></td><td><b>Bezeichnung</b></td><td><b>Hersteller</b></td><td><b>Mietzins pro Tag</b></td><td><b>Kategorie</b></td></tr>");
            String position_string = "SELECT bestellposition.position, produkt.name AS produktname,bezeichnung,hersteller_name, mietzins,kategorie.name AS kategoriename FROM bestellposition INNER JOIN produkt ON(bestellposition.Produktid = produkt.id) INNER JOIN kategorie ON(Kategorieid = kategorie.id) WHERE Bestellungid = ? ;\n";
            PreparedStatement position_ps = DB_Connector.con.prepareStatement(position_string);
            position_ps.setInt(1,Integer.parseInt(bestellid));
            ResultSet position_rs = position_ps.executeQuery();
            int pos =0, kategorieid;
            String name, bezeichnung, hersteller_name, kategorie;
            double mietzins, miete=0.0;
            ArrayList<Double> waren = new ArrayList<Double>();
            while(position_rs.next()){
                pos=position_rs.getInt("position");
                name=position_rs.getString("produktname");
                bezeichnung=position_rs.getString("bezeichnung");
                hersteller_name = position_rs.getString("hersteller_name");
                mietzins = position_rs.getDouble("mietzins");
                kategorie = position_rs.getString("kategoriename");
                miete+=mietzins;
                waren.add(mietzins);
                writer.print("<tr><td>"+pos+"</td><td>"+name+"</td><td>"+bezeichnung+"</td><td>"+hersteller_name+"</td><td>"+Formatter.formatdouble(mietzins)+"</td><td>"+kategorie+"</td></tr>");
            }
            writer.print("<tr><td colspan=4><b>Summe pro Tag</b></td><td><b>"+Formatter.formatdouble(miete)+"</b></td><td></td></tr>");
            double gesamtsumme = getgesamtsumme(miete, tage);
            double endsumme = getEndsumme(nutzerid, tage, waren);
            double rabatt = gesamtsumme-endsumme;
            writer.print("<tr><td colspan=4><b>Summe Gesamt ohne Rabatt</b></td><td><b>"+Formatter.formatdouble(gesamtsumme)+"</b></td><td></td></tr>");
            writer.print("<tr><td colspan=4><b>Rabatt</b></td><td><b>"+Formatter.formatdouble(rabatt)+"</b></td><td></td></tr>");
            writer.print("<tr><td colspan=4><b>Summe Gesamt inkl. Rabatt</b></td><td><b>"+Formatter.formatdouble(endsumme)+"</b></td><td></td></tr>");
            writer.print("<tr><td colspan=4><b></b></td><td><b>"+""+"</b></td><td></td></tr>");
            // Tabelle ende
            writer.print("</table>");
            writer.print("</td>");
        }catch(IOException e1){

        }catch(SQLException e2){
            try {
                writer.print(e2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally{
            DB_Connector.closeDatabase();
        }
    }
    public static void getMitarbeiterUebersicht(JspWriter writer){

        try{
            writer.print("<td><table style=\"width:100%\"><tr><td>Vorname</td><td>Nachname</td><td>Benutzerid</td><td>Loeschen?</td></tr>");
                DB_Connector.connecttoDatabase();

                String mitarbeiter = "SELECT vorname, nachname, id FROM softwareengineering2.nutzer WHERE left(id, 1)='M';";
                PreparedStatement mitarbeiter_ps = DB_Connector.con.prepareStatement(mitarbeiter);
                ResultSet mitarbeiter_rs = mitarbeiter_ps.executeQuery();
                String vorname, nachname, Mitarbeiternummer;
                while(mitarbeiter_rs.next()){
                    vorname = mitarbeiter_rs.getString("vorname");
                    nachname = mitarbeiter_rs.getString("nachname");
                    Mitarbeiternummer = mitarbeiter_rs.getString("id");
                    writer.print("<tr><td>"+vorname+"</td><td>"+nachname+"</td><td>"+Mitarbeiternummer+"</td><td  onclick=self.location.href=\"./MitarbeiterLoeschen?id="+Mitarbeiternummer+"\" style=\"min-width:60pt;text-align:center\" onmouseover=this.style.color=\"#6565FC\" onmouseout=this.style.color=\"#000000\"> Loeschen</td></tr>");
                }
            writer.print("</table></td>");
            }catch(SQLException e1){

            }catch(IOException e2){

            }finally{
                DB_Connector.closeDatabase();
            }
    }
    public static void getKategorieRadio(JspWriter writer) {
        for(Kategorie k:Seitenaufbau.kategorien){
            try {
                writer.print("<input type=\"radio\" name=\"kategorie\" value=\"" + k.getName() + "\">"+k.getName()+"</input><br>");
            }catch(IOException e1){

            }
        }
    }
    // Interne Funktionen



    static Kunde getKunde(String uuid){
        for(Kunde k: kunde){
            if(k.getUuid().equals(uuid)){
                return k;
            }
        }
        return null;
    }
    public static String getWarenkorbTabelle(String uuid){

        DB_Connector.connecttoDatabase();
        String produkt_string = "SELECT name, bezeichnung, mietzins  FROM produkt WHERE id=?";
        PreparedStatement produkt_ps = null;
        String tabelle_anfang= "<table border=0 style=\"width:100%\">";
        ResultSet produkt_rs;
        StringBuffer writer=new StringBuffer();

        try {
                for(Warenkorb b: koerbe){
                    if(b.getUuid().equals(uuid)){

                        writer.append(tabelle_anfang);
                        writer.append("<td style=\"text-align: center\">Produkte</td>");

                        double summe=0.00;
                        for(Integer produkt: b.getProdukt_id()){
                            produkt_ps = DB_Connector.con.prepareStatement(produkt_string);
                            produkt_ps.setInt(1,produkt);
                            produkt_rs = produkt_ps.executeQuery();
                            produkt_rs.next();
                            String name= produkt_rs.getString("name");
                            String bezeichnung = produkt_rs.getString("bezeichnung");
                            Double mietzins = produkt_rs.getDouble("mietzins");
                            summe+=mietzins;
                            String mietzins_string = Formatter.formatdouble(mietzins);
                            writer.append("<tr><td style=\"width:33%; align:center; border:solid 1px #000000\"><table style=\"width:100%\"><th colspan=\"2\" align=left>"+name+"</th><tr><td style=\" max-width: 200px;\">Bezeichnung:</td><td>"+bezeichnung+"</td><td align=right>"+mietzins_string+"</td></tr></table></td></tr>");
                        }
                        String summe_string = Formatter.formatdouble(summe);
                        writer.append("<tr><td><table style=\"width:100%\"><td>Summe</td><td align=\"right\">"+summe_string+"</td></table></td></tr>");
                        writer.append("</td></tr></table>");
                    }
                }


        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }
        return writer.toString();
    }


    /**
     * Berechnung der Differenz zwischen den Tagen.
     * @param von Timestamp von wann die Bestellung gilt
     * @param bis Timestamp bis wann die Bestellung gilt
     * @return Gibt einen Integerwert zur�ck, mit der Differenz der Tage
     */
    public static int getTage(Timestamp von, Timestamp bis){
        DateTimeZone Berlin = DateTimeZone.forID("Europe/Berlin");
        DateTime von_joda = new DateTime(von.getTime(), Berlin);
        DateTime bis_joda = new DateTime(bis.getTime(), Berlin);
        Days days = Days.daysBetween(von_joda, bis_joda);

        return days.getDays();
    }

    /**
     *
     * @param mietzins
     * @param tage
     * @return
     */
    private static double getgesamtsumme(double mietzins, int tage){
        mietzins=mietzins*tage;
        double mietzins_round = Math.round(mietzins*100.0)/100.0;
        return mietzins_round;
    }
    public static double getEndsumme(String nutzerid, int tage, ArrayList<Double> waren) {
        Double Summe=0.0;
        for(double d:waren){
               Summe+=d;
            }
        if(tage>1){
            for(int i=1; i<tage;i++){
                for(double d1:waren) {
                    Summe += d1* 0.60;
                }
            }
        }

        try {
            DB_Connector.connecttoDatabase();
            String rabatt_string ="SELECT case when(sum(id)>2) THEN 1 ELSE 0 END AS rabatt FROM bestellung WHERE bestellung.Nutzerid=?";
            PreparedStatement rabatt_ps = DB_Connector.con.prepareStatement(rabatt_string);
            rabatt_ps.setString(1,nutzerid);
            ResultSet rabatt_rs = rabatt_ps.executeQuery();
            rabatt_rs.next();
            if(rabatt_rs.getInt("rabatt")==1){
                Summe=Summe*0.80;
            }
        }catch(SQLException e1){
            e1.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }

        Summe=Math.round(Summe*100.0)/100.0;
        return Summe;
    }

    public static double getEndsumme(String nutzerid, int tage, double mietzins){
        Double Summe=0.0;
        Summe+=mietzins;
        if(tage>1){
            for(int i=1; i<tage;i++){
                Summe+=mietzins*0.60;
            }
        }

        try {
            DB_Connector.connecttoDatabase();
            String rabatt_string ="SELECT case when(sum(id)>2) THEN 1 ELSE 0 END AS rabatt FROM bestellung WHERE bestellung.Nutzerid=?";
            PreparedStatement rabatt_ps = DB_Connector.con.prepareStatement(rabatt_string);
            rabatt_ps.setString(1,nutzerid);
            ResultSet rabatt_rs = rabatt_ps.executeQuery();
            rabatt_rs.next();
            if(rabatt_rs.getInt("rabatt")==1){
                Summe=Summe*0.80;
            }
        }catch(SQLException e1){
            e1.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }

        Summe=Math.round(Summe*100.0)/100.0;
        return Summe;
    }


    public static void getProdukteDatalist(JspWriter writer) {
        try {
            for (Produkt p : katalog) {
                writer.print("<option value=" + p.getId() + ">"+p.getName()+"</option>");
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public static void getKategorieOptionen(JspWriter writer) {
        try {
            for (Kategorie k : kategorien) {
                writer.print("<option value=" + k.getId() + ">"+k.getName()+"</option>");
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public static void getKundenSelect(JspWriter writer) {
        try {
            for (Kunde k : kunde) {
                writer.print("<option value=" + k.getId() + ">"+k.getVorname()+" "+k.getNachname()+"</option>");
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public static void getBestelluebersichtKD(JspWriter writer, Cookie[] cookies) {
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
try {
    DB_Connector.connecttoDatabase();
    if (cookie_vorhanden) {
        Kunde k = getKunde(cook.getValue());
        writer.print("<td><table width=100%><tr><td>Bestellnummer</td><td>von</td><td>bis</td><td>Mietzins</td><td>Stornieren</td></tr>");
        String bestell_string = "SELECT Bestellungid, von, bis ,gesamtkosten FROM (SELECT Bestellungid ,round(sum(mietzins), 2) AS Gesamtkosten FROM bestellposition INNER JOIN produkt ON(bestellposition.Produktid = produkt.id) GROUP BY Bestellungid) AS temp INNER JOIN bestellung ON(Bestellungid = bestellung.id) WHERE genehmigt = 0 AND Nutzerid=?\n ;";
        PreparedStatement bestell_ps = null;
        ResultSet bestell_rs;
        bestell_ps = DB_Connector.con.prepareStatement(bestell_string);
        bestell_ps.setString(1,k.getId());
        bestell_rs = bestell_ps.executeQuery();
        while (bestell_rs.next()) {

            int bestellid = bestell_rs.getInt("Bestellungid");
            Timestamp von = bestell_rs.getTimestamp("von");
            Timestamp bis = bestell_rs.getTimestamp("bis");
            double mietzins = bestell_rs.getDouble("gesamtkosten");
            writer.print("<tr><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid=" + bestellid + "\">" + bestellid + "</td><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid=" + bestellid + "\">" + von.toString() + "</td><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid=" + bestellid + "\">" + bis.toString() + "</td><td onclick=self.location.href=\"./bestelldetails.jsp?bestellid=" + bestellid + "\">" + Formatter.formatdouble(getEndsumme(k.getId(), getTage(von,bis), mietzins)) + "</td><td onclick=self.location.href=\"./bestellannahme.jsp?bestellid=" + bestellid + "\">Stornieren</td><</tr>");
        }
        writer.print("</table></td>");
    }
}catch(SQLException e1){
    e1.printStackTrace();
}catch(IOException e2) {
    e2.printStackTrace();
}finally{
    DB_Connector.closeDatabase();
}


}

    public static void getProfil(JspWriter writer, Cookie[] cookies) {
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
        try{
            DB_Connector.connecttoDatabase();
            writer.print("<td><form method=\"post\" action=\"/ProfilUpdate\"><table>");
            Kunde k = getKunde(cook.getValue());

            String kunde = " SELECT nutzer.vorname, nutzer.nachname, kunde.strasse, kunde.hausnummer, kunde.plz, kunde.ort, kunde.telefonnummer, kunde.handynummer, kunde.email FROM kunde Inner join nutzer on kunde.Nutzerid=nutzer.id WHERE kunde.Nutzerid=?";
            PreparedStatement kunde_ps = DB_Connector.con.prepareStatement(kunde);
            kunde_ps.setString(1, k.getId());
            ResultSet kunde_rs = kunde_ps.executeQuery();
            kunde_rs.next();
            writer.print("<tr><td>Vorname: </td><td><input type=\"text\" name=\"vorname\" value=\"" + kunde_rs.getString("vorname") + "\"></td></tr>");
            writer.print("<tr><td>Nachname: </td><td><input type=\"text\" name=\"nachname\" value=\""+kunde_rs.getString("nachname")+"\"></td></tr>");
            writer.print("<tr><td>Straße: </td><td><input type=\"text\" name=\"strasse\" value=\""+kunde_rs.getString("strasse")+"\"></td></tr>");
            writer.print("<tr><td>Hausnummer: </td><td><input type=\"number\" name=\"hausnummer\" value=\""+kunde_rs.getInt("hausnummer")+"\"></td></tr>");
            writer.print("<tr><td>PLZ: </td><td><input type=\"text\" name=\"plz\" value=\""+kunde_rs.getString("plz")+"\"></td></tr>");
            writer.print("<tr><td>Ort: </td><td><input type=\"text\" name=\"ort\" value=\""+kunde_rs.getString("ort")+"\"></td></tr>");
            writer.print("<tr><td>Telefon: </td><td><input type=\"text\" name=\"telefon\" value=\""+kunde_rs.getString("telefonnummer")+"\"></td></tr>");
            writer.print("<tr><td>Handy: </td><td><input type=\"text\" name=\"handy\" value=\""+kunde_rs.getString("handynummer")+"\"></td></tr>");
            writer.print("<tr><td>E-Mail: </td><td><input type=\"text\" name=\"email\" value=\""+kunde_rs.getString("email")+"\"></td></tr>");
            writer.print("<tr><td>Kennwort: </td><td><input type=\"password\" name=\"password\" ></td></tr>");
            writer.print("<tr><td><input type=\"submit\" name=\"Absenden\"></td></tr>");

            writer.println("</table></form>");
            writer.println("<form method=\"post\" action=\"/UserLoeschen\"><input type=\"submit\" name=\"Benutzer L&oumlschen\" value=\"Benutzer Loeschen\"></form></td>");
        }catch(SQLException e1){
            e1.printStackTrace();
        }catch(IOException e2){
            e2.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }
    }
}

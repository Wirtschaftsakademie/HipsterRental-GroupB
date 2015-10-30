package wak.system.db;

import wak.objects.Kategorie;
import wak.objects.Produkt;
import wak.system.db.DB_Connector;
import wak.system.server.Seitenaufbau;
import wak.user.Adresse;
import wak.user.Kunde;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by chris_000 on 22.10.2015.
 */
public class DB_Loader {
    static Connection con = DB_Connector.con;
    static boolean runned=false;
    public DB_Loader(){
        try {
            if(!runned) {
                Kategorieanlegen();
                Produkteanlegen();
                Kundenanlegen();
                runned = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

   private void Produkteanlegen() throws SQLException{

        DB_Connector.connecttoDatabase();
        String produkt_string = "SELECT * FROM produkt";
        PreparedStatement produkt_ps = DB_Connector.con.prepareStatement(produkt_string);
        ResultSet produkt_rs = produkt_ps.executeQuery();
        String name, bezeichnung, beschreibung, hersteller_name, details;
        int id, kategorie;
        double mietzins;
        while(produkt_rs.next()){
            name = produkt_rs.getString("name");
            bezeichnung= produkt_rs.getString("bezeichnung");
            beschreibung = produkt_rs.getString("beschreibung");
            hersteller_name = produkt_rs.getString("hersteller_name");
            details = produkt_rs.getString("details");
            id = produkt_rs.getInt("id");
            mietzins = produkt_rs.getDouble("mietzins");
            kategorie = produkt_rs.getInt("Kategorieid");
            Kategorie Kat=null;
            for(Kategorie k:Seitenaufbau.kategorien){
                if(k.getId()==kategorie){
                    Kat = k;
                }
            }
            Produkt p = new Produkt(name, bezeichnung, beschreibung, hersteller_name, details, mietzins, null, Kat, false);
            p.setId(id);
            Seitenaufbau.katalog.add(p);
        }

       DB_Connector.closeDatabase();
    }
    private void Kategorieanlegen()throws SQLException{
        DB_Connector.connecttoDatabase();
        String kategorie_string = "SELECT * FROM kategorie";
        PreparedStatement kategorie_ps = DB_Connector.con.prepareStatement(kategorie_string);
        ResultSet kategorie_rs = kategorie_ps.executeQuery();
        String name;
        int id, oberid;
        Kategorie ober;
        while(kategorie_rs.next()){
            name = kategorie_rs.getString("name");
            id=kategorie_rs.getInt("id");
            Kategorie k = new Kategorie(name, id);
            Seitenaufbau.kategorien.add(k);
        }
        DB_Connector.closeDatabase();
    }

    private void Kundenanlegen() throws SQLException{
        DB_Connector.connecttoDatabase();
        String kunden_string="SELECT nutzer.vorname, nutzer.nachname, kunde.strasse, kunde.hausnummer, kunde.plz, kunde.ort, kunde.telefonnummer, kunde.handynummer, kunde.email, kunde.Nutzerid FROM kunde Inner join nutzer on kunde.Nutzerid=nutzer.id";
        PreparedStatement kunde_ps = DB_Connector.con.prepareStatement(kunden_string);
        ResultSet kunde_rs = kunde_ps.executeQuery();
        String vorname, nachname, strasse, plz, ort, telefonnummer, handynummer, email, nutzerid;
        int hausnumer;
        while(kunde_rs.next()){
            vorname = kunde_rs.getString("vorname");
            nachname = kunde_rs.getString("nachname");
            strasse = kunde_rs.getString("strasse");
            int hausnummer= kunde_rs.getInt("hausnummer");
            plz = kunde_rs.getString("plz");
            ort = kunde_rs.getString("ort");
            telefonnummer = kunde_rs.getString("telefonnummer");
            handynummer = kunde_rs.getString("handynummer");
            email = kunde_rs.getString("email");
            nutzerid = kunde_rs.getString("nutzerid");
            Adresse a = new Adresse(strasse, ort, plz, hausnummer);
            Kunde k = new Kunde(nutzerid, vorname, nachname, email, telefonnummer, handynummer, a);
            Seitenaufbau.kunde.add(k);
        }


        DB_Connector.closeDatabase();
    }

}

package wak.objects;

import wak.system.db.DB_Connector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

public class Produkt implements Comparable<Produkt> {
    String name, bezeichnung, beschreibung, herstellername, details;
    double mietzins;
    Produkt alternative;
    Kategorie kategorie;
    String[] bilder;
    Geraet[] geraete;
    int id;

    public Produkt(String name, double mietzins, Kategorie kategorie) {
        this.name = name;
        this.mietzins = mietzins;
        this.kategorie = kategorie;
       // this.id = generiereID();
       // produkt_eintragen(this);
    }

    public Produkt(String name, String bezeichnung, String beschreibung, String herstellername, String details, double mietzins, Produkt alternative, Kategorie kategorie, boolean eintragen) {
        this.name = name;
        this.bezeichnung = bezeichnung;
        this.beschreibung = beschreibung;
        this.herstellername = herstellername;
        this.details = details;
        this.mietzins = mietzins;
        this.alternative = alternative;
        this.kategorie = kategorie;
        if(eintragen) {
            produkt_eintragen(this);
        }
    }


    public int getId() {
        return id;
    }

    int generiereID(){
        int produkt_id=0;
        String produkt_id_query = ("SELECT id from produkt order by id DESC");
        try {
            PreparedStatement produkt_id_result = DB_Connector.con.prepareStatement(produkt_id_query);
            ResultSet produkt_id_set = produkt_id_result.executeQuery();
            produkt_id_set.next();
            produkt_id = produkt_id_set.getInt("id")+1;
            System.out.println(produkt_id);
        }catch(SQLException e){
            System.out.println("Fehler beim generieren einer ProduktID");
        }
        return produkt_id;
    }

    public void setId(int id) {
        //Generieren einer Produkt ID

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getHerstellername() {
        return herstellername;
    }

    public void setHerstellername(String herstellername) {
        this.herstellername = herstellername;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getMietzins() {
        return mietzins;
    }

    public void setMietzins(double mietzins) {
        this.mietzins = mietzins;
    }

    public Produkt getAlternative() {
        return alternative;
    }

    public void setAlternative(Produkt alternative) {
        this.alternative = alternative;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public String[] getBilder() {
        return bilder;
    }

    public void setBilder(String[] bilder) {
        this.bilder = bilder;
    }

    public Geraet[] getGeraete() {
        return geraete;
    }

    public void setGeraete(Geraet[] geraete) {
        this.geraete = geraete;
    }

    @Override
    public String toString() {
        return "Produkt{" +
                "name='" + name + '\'' +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                ", herstellername='" + herstellername + '\'' +
                ", details='" + details + '\'' +
                ", mietzins=" + mietzins +
                ", alternative=" + alternative +
                ", kategorie=" + kategorie +
                ", bilder=" + Arrays.toString(bilder) +
                ", geraete=" + Arrays.toString(geraete) +
                ", id=" + id +
                '}';
    }

    public void produkt_eintragen(Produkt p){
        DB_Connector.connecttoDatabase();
        String einfuegen = "INSERT INTO produkt (name, bezeichnung, hersteller_name, beschreibung, details, mietzins, Kategorieid, alternative)" + "VALUES (?, ?, ?, ?, ?, ?,(select id from kategorie WHERE kategorie.id=?),?)";
        PreparedStatement bestellung = null;
        //Vorbereiten der Bestellung f�r die Datenbank
        try {
            bestellung = DB_Connector.con.prepareStatement(einfuegen);
            bestellung.setString(1, p.getName());
            bestellung.setString(2, p.getBezeichnung());
            bestellung.setString(3, p.getHerstellername());
            bestellung.setString(4, p.getBeschreibung());
            bestellung.setString(5, p.getDetails());
            bestellung.setDouble(6, p.getMietzins());
            bestellung.setInt(7, p.getKategorie().getId());
            if(p.getAlternative()!=null){
                bestellung.setInt(8, p.getAlternative().getId());
            }else{
                bestellung.setNull(8, Types.INTEGER);
            }

            System.out.println(bestellung.toString());
            bestellung.executeUpdate();
        }catch(SQLException e){
            System.out.println("Fehler bei der Produkteintragung");
            e.printStackTrace();
        }finally{
            DB_Connector.closeDatabase();
        }


    }

    @Override
    public int compareTo(Produkt o) {
        if(this.getId()>o.getId()){
            return 1;
        }else if(this.getId()==o.getId()){
            return 0;
        }else if(this.getId()<o.getId()){
            return -1;
        }else{
            return 0;
        }
    }
}

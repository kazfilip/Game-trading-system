import java.io.Serializable;
import java.util.Date;

public class Recenzja implements Serializable {
    private int ocena;
    private String tresc;
    private Date dataWystawienia;
    private Kolekcjoner autor;
    private Gra gra;

    public Recenzja(Kolekcjoner autor, Gra gra, int ocena, String tresc) {
        if (autor == null) {
            throw new IllegalArgumentException("Autor nie moze byc null");
        }
        if (gra == null) {
            throw new IllegalArgumentException("Gra nie moze byc null");
        }
        if (ocena < 1 || ocena > 5) {
            throw new IllegalArgumentException("Ocena musi byc od 1 do 5");
        }
        if (tresc == null || tresc.isBlank()) {
            throw new IllegalArgumentException("Tresc nie moze byc pusta");
        }
        this.autor = autor;
        this.gra = gra;
        this.ocena = ocena;
        this.tresc = tresc;
        this.dataWystawienia = new Date();
        gra.dodajRecenzje(this);
    }

    public int getOcena() { return ocena; }
    public String getTresc() { return tresc; }
    public Date getDataWystawienia() { return dataWystawienia; }
    public Kolekcjoner getAutor() { return autor; }
    public Gra getGra() { return gra; }
}

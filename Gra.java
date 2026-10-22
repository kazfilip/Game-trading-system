import java.io.Serializable;
import java.util.*;

public class Gra implements Serializable {
    private String tytul;
    private Date dataWydania;
    private Wydawnictwo wydawnictwo;
    private int wartosc;
    private String opis;
    
    private List<Recenzja> recenzje = new ArrayList<>();
    public static List<Gra> wszystkieGry = new ArrayList<>();

    public Gra(String tytul, Date dataWydania, int wartosc, Wydawnictwo wydawnictwo) {
        if (tytul == null || tytul.isBlank()) {
            throw new IllegalArgumentException("Tytul nie moze byc pusty");
        }
        if (dataWydania == null || dataWydania.after(new Date())) {
            throw new IllegalArgumentException("Data wydania nie moze byc null lub w przyszlosci");
        }
        if (wartosc < 0) {
            throw new IllegalArgumentException("Wartosc nie moze byc ujemna");
        }
        if (wydawnictwo == null) {
            throw new IllegalArgumentException("Wydawnictwo nie moze byc null");
        }
        this.tytul = tytul;
        this.dataWydania = dataWydania;
        this.wartosc = wartosc;
        this.wydawnictwo = wydawnictwo;
        wydawnictwo.dodajGre(this);
        wszystkieGry.add(this);
    }

    public void setTytul(String tytul) { this.tytul = tytul; }
    public void setDataWydania(Date data) { this.dataWydania = data; }
    public void setWartosc(int wartosc) { this.wartosc = wartosc; }
    public void setWydawnictwo(Wydawnictwo wydawnictwo) { this.wydawnictwo = wydawnictwo; }
    public void setOpis(String opis) { this.opis = opis; }

    public String getTytul() { return tytul; }
    public Date getDataWydania() { return dataWydania; }
    public int getWartosc() { return wartosc; }
    public Wydawnictwo getWydawnictwo() { return wydawnictwo; }
    public String getOpis() { return opis; }
    public List<Recenzja> getRecenzje() { return recenzje; }

    public void dodajRecenzje(Recenzja recenzja) {
        if (recenzja != null && !recenzje.contains(recenzja)) {
            recenzje.add(recenzja);
        }
    }

    public static void pokazGry() {
        if (wszystkieGry.isEmpty()) {
            System.out.println("Brak gier");
        }
        for (Gra gra : wszystkieGry) {
            System.out.println(gra.getTytul());
        }
    }
    
    @Override
    public String toString() {
        return tytul + " (" + wydawnictwo.getNazwa() + ") - " + wartosc + " zl";
    }
}

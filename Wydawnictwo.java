import java.io.Serializable;
import java.util.*;

public class Wydawnictwo implements Serializable {
    private String nazwa;
    private String kraj;
    
    private List<Gra> wydaneGry = new ArrayList<>();

    public Wydawnictwo(String nazwa, String kraj) {
        if (nazwa == null || nazwa.isBlank()) {
            throw new IllegalArgumentException("Nazwa nie moze byc pusta");
        }
        if (kraj == null || kraj.isBlank()) {
            throw new IllegalArgumentException("Kraj nie moze byc pusty");
        }
        this.nazwa = nazwa;
        this.kraj = kraj;
    }

    public void setNazwa(String nazwa) { this.nazwa = nazwa; }
    public void setKraj(String kraj) { this.kraj = kraj; }

    public String getNazwa() { return nazwa; }
    public String getKraj() { return kraj; }
    public List<Gra> getWydaneGry() { return wydaneGry; }

    public void dodajGre(Gra gra) {
        if (!wydaneGry.contains(gra)) {
            wydaneGry.add(gra);
        }
    }

    public void pokazWydaneGry() {
        if (wydaneGry.isEmpty()) {
            System.out.println("Brak gier");
            return;
        }
        for (Gra gra : wydaneGry) {
            System.out.println(gra.getTytul());
        }
    }
}

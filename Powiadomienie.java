import java.io.Serializable;
import java.util.Date;

public class Powiadomienie implements Serializable {
    private String tresc;
    private Date dataPowiadomienia;
    private boolean przeczytane;

    public Powiadomienie(String tresc) {
        if (tresc == null || tresc.isBlank()) {
            throw new IllegalArgumentException("Tresc nie moze byc pusta");
        }
        this.tresc = tresc;
        this.dataPowiadomienia = new Date();
        this.przeczytane = false;
    }

    public void oznaczJakoPrzeczytane() {
        this.przeczytane = true;
    }

    public String getTresc() { return tresc; }
    public Date getDataPowiadomienia() { return dataPowiadomienia; }
    public boolean czyPrzeczytane() { return przeczytane; }
}

import java.util.Date;

public class KolekcjonerPremium extends Kolekcjoner {
    private static final int limitListingow = Integer.MAX_VALUE;
    private Date dataWaznosciPremium;

    public KolekcjonerPremium(String imie, String nazwisko, Poziom poziom, Region region, Date dataWaznosciPremium) {
        super(imie, nazwisko, poziom, region);
        if (dataWaznosciPremium == null || dataWaznosciPremium.before(new Date())) {
            throw new IllegalArgumentException("Data waznosci nie moze byc null lub w przeszlosci");
        }
        this.dataWaznosciPremium = dataWaznosciPremium;
    }

    public KolekcjonerPremium(Kolekcjoner poprzedni, Date dataWaznosciPremium) {
        super(poprzedni.getImie(), poprzedni.getNazwisko(), poprzedni.getPoziom(), poprzedni.getRegion());
        if (dataWaznosciPremium == null || dataWaznosciPremium.before(new Date())) {
            throw new IllegalArgumentException("Data waznosci nie moze byc null lub w przeszlosci");
        }
        this.dataWaznosciPremium = dataWaznosciPremium;
        for (Gra gra : poprzedni.getKolekcja().getKolekcja()) {
            this.getKolekcja().dodajGre(gra);
        }
        wszyscyKolekcjonerzy.remove(poprzedni);
    }

    public boolean czyAktywnePremium() {
        return new Date().before(dataWaznosciPremium);
    }

    @Override
    public int getLimitListingow() { return limitListingow; }

    @Override
    public String getTyp() { return "Premium"; }

    @Override
    public void wyswietlInfo() {
        super.wyswietlInfo();
        System.out.println("Data waznosci konta: " + dataWaznosciPremium);
    }

    public Date getDataWaznosciPremium() { return dataWaznosciPremium; }

    public void setDataWaznosciPremium(Date dataWaznosciPremium) {
        if (dataWaznosciPremium == null || dataWaznosciPremium.before(new Date())) {
            throw new IllegalArgumentException("Data waznosci nie moze byc null lub w przeszlosci");
        }
        this.dataWaznosciPremium = dataWaznosciPremium;
    }
}

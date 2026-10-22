public class KolekcjonerNormal extends Kolekcjoner {
    private static final int limitListingow = 3;

    public KolekcjonerNormal(String imie, String nazwisko, Poziom poziom, Region region) {
        super(imie, nazwisko, poziom, region);
    }

    public KolekcjonerNormal(Kolekcjoner poprzedni) {
        super(poprzedni.getImie(), poprzedni.getNazwisko(), poprzedni.getPoziom(), poprzedni.getRegion());
        for (Gra gra : poprzedni.getKolekcja().getKolekcja()) {
            this.getKolekcja().dodajGre(gra);
        }
        wszyscyKolekcjonerzy.remove(poprzedni);
    }

    @Override
    public int getLimitListingow() { return limitListingow; }

    @Override
    public String getTyp() { return "Normal"; }

    @Override
    public void wyswietlInfo() {
        super.wyswietlInfo();
        System.out.println("Limit listingow: " + limitListingow);
    }
}

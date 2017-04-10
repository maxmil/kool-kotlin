package my.favorite.kotlin.features;

import my.favorite.kotlin.features.ClassDelegates.Drummer;
import my.favorite.kotlin.features.ClassDelegates.DrunkenSinger;
import my.favorite.kotlin.features.ClassDelegates.OneRhythmDrummer;
import my.favorite.kotlin.features.ClassDelegates.Singer;

public class JavaOneManBand implements Drummer, Singer {

    private Drummer drummer = new OneRhythmDrummer();
    private Singer singer = new DrunkenSinger();

    @Override
    public String hiphop() {
        return drummer.hiphop();
    }

    @Override
    public String bosanova() {
        return drummer.bosanova();
    }

    @Override
    public String rock() {
        return drummer.rock();
    }

    @Override
    public String funky() {
        return drummer.funky();
    }

    @Override
    public String doe() {
        return singer.doe();
    }

    @Override
    public String re() {
        return singer.re();
    }

    @Override
    public String mi() {
        return singer.mi();
    }

    @Override
    public String fa() {
        return singer.fa();
    }

    @Override
    public String sol() {
        return singer.sol();
    }

    @Override
    public String la() {
        return singer.la();
    }

    @Override
    public String si() {
        return singer.si();
    }
}

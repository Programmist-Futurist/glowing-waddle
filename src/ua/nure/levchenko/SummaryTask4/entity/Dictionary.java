package ua.nure.levchenko.SummaryTask4.entity;

import ua.nure.levchenko.SummaryTask4.internationalization.Language;

/**
 * Dictionary entity.
 *
 * @author K.Levchenko
 */
public class Dictionary extends Entity {
    private static final long serialVersionUID = -1757409997615731642L;
    private Language language = Language.ENG;
    private String eng;
    private String rus;

    public Dictionary(int id, String eng, String rus) {
        super(id);
        this.eng = eng;
        this.rus = rus;
    }


    public Dictionary(String eng, String rus) {
        this.eng = eng;
        this.rus = rus;
    }

    public Dictionary() {
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getRus() {
        return rus;
    }

    public void setRus(String rus) {
        this.rus = rus;
    }

    public String getWord() {
        if (language.equals(Language.ENG))
            return eng;
        if (language.equals(Language.RUS))
            return rus;
        return null;
    }

}
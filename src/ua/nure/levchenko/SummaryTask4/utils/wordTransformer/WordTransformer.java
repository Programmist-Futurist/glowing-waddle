package ua.nure.levchenko.SummaryTask4.utils.wordTransformer;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.dao.StudentDao;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

/**
 * Transliterate words from russian to english
 *
 * @author K.Levchenko
 */
public class WordTransformer {
    private static final Logger LOG = Logger.getLogger(StudentDao.class);
    private static final String[] f = {"А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ч", "Ц", "Ш", "Щ", "Э", "Ю", "Я", "Ы", "Ъ", "Ь", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
    private static final String[] t = {"A", "B", "V", "G", "D", "E", "Jo", "Zh", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "Csh", "E", "Ju", "Ja", "Y", "`", "'", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "`", "'"};


    public static String rus2eng(String src) {
        LOG.trace(Messages.TRACE_TRANSLIT_WORD);

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < src.length(); ++i) {
            String add = src.substring(i, i + 1);
            for (int j = 0; j < f.length; j++) {
                if (f[j].equals(add)) {
                    add = t[j];
                    break;
                }
            }
            res.append(add);
        }

        return res.toString();
    }
}

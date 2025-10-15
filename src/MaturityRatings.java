
import java.io.Serializable;

/**
 * Enumerator for maturity ratings:
 * G: General Audiences
 * PG: Parental Guidance Suggested
 * PG13: Parents Strongly Cautioned
 * R: Restricted
 * NC17: Clearly Adult
 *
 * @author Vasiliki Raskopoulou
 */
public enum MaturityRatings implements Serializable{
    G, PG, PG13, R, NC17
}
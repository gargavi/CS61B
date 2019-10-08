/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static final String P1 = "(0?[1-9]|1[0-2])/(0?[1-9]|1[0-9]|2[0-9]|3[0-1])/[1-2][0-9]{3}";

    /* Pattern to match 61b notation for literal IntLists. */
    public static final String P2 = "[(](([0-9]+), +)+([0-9]+)[)]";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static final String P3 = "(([a-zA-Z\\d]([a-zA-Z\\d-][a-zA-Z\\d])?)+\\.)+[a-zA-Z]{2,6}";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static final String P4 = "[a-zA-Z_$][a-zA-Z0-9_$]*";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static final String P5 = "((0?[0-9]?[0-9]|1[0-9][0-9]|2[0-5][0-5])\\.){3}(0?[0-9]?[0-9]|1[0-9][0-9]|2[0-5][0-5])";

}

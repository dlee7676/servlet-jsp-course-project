/* David Lee, A00783233 */

package comp3613.springapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorBean {
	public static boolean isValidInput(String input, String pattern) {
		Pattern patt = Pattern.compile(pattern);
		Matcher match = patt.matcher(input);
		return match.matches();
	}
}

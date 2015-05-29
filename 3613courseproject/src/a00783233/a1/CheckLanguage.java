package a00783233.a1;

import java.util.Locale;
import javax.servlet.http.Cookie;

/* Checks the current language settings */
public class CheckLanguage {
	public static Locale checkCookies(Cookie[] cookies, Locale current) {
		if (cookies != null) {
			String currentLanguage = "en";
			String currentCountry = "US";
			boolean foundLanguage = false;
			boolean foundCountry = false;
		    for (int i = 0; i < cookies.length; i++) {
			   	if (cookies[i].getName().compareTo("languageCookie") == 0) {
				   currentLanguage = cookies[i].getValue();
				   foundLanguage = true;
			   	}
			   	if (cookies[i].getName().compareTo("countryCookie") == 0) {
				   currentCountry = cookies[i].getValue();
				   foundCountry = true;
			   	}
		   	}
		    if (foundLanguage && foundCountry)
	    		current = new Locale(currentLanguage, currentCountry);
		}
		return current;
	}
}

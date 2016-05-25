package comp3613.springapp;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PagesController {
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home(@ModelAttribute("language")Language language, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String currentLanguage = "en", currentCountry = "US";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().compareTo("languageCookie") == 0) {
				   currentLanguage = cookies[i].getValue();
			    }
			    if (cookies[i].getName().compareTo("countryCookie") == 0) {
				   currentCountry = cookies[i].getValue();
			    }
			}
		}
		String bundlePath = "comp3613.springapp.language";
		HttpSession session = request.getSession();
		setStaticText(session, bundlePath, currentLanguage, currentCountry);
		session.setAttribute("language", currentLanguage);
		session.setAttribute("country", currentCountry);
		return "index";
	}
	
	@RequestMapping(value="/summary", method=RequestMethod.GET)
	public String summary() {
		return "summary";
	}
	
	@RequestMapping(value="/about", method=RequestMethod.GET)
	public ModelAndView about() {
		return new ModelAndView("about", "command", new Language());
	}
	
	@RequestMapping(value="/changeLanguage", method=RequestMethod.POST)
	public String changeLanguage(@ModelAttribute("language")Language language, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		String bundlePath = "comp3613.springapp.language";
		String currentLanguage = "en", currentCountry = "US";
		if (language.getLanguage().compareTo("english") == 0) {
			currentLanguage = "en";
			currentCountry = "US";
		}
		if (language.getLanguage().compareTo("french") == 0) {
			currentLanguage = "fr";
			currentCountry = "FR";
		}
		if (language.getLanguage().compareTo("greek") == 0) {
			currentLanguage = "el";
			currentCountry = "EL";
		}
		HttpSession session = request.getSession();
		setStaticText(session, bundlePath, currentLanguage, currentCountry);
		session.setAttribute("language", currentLanguage);
		session.setAttribute("country", currentCountry);
		// set language cookies with current settings
		Cookie languageCookie = null;
		Cookie countryCookie = null;
		languageCookie = new Cookie("languageCookie", currentLanguage);
		countryCookie = new Cookie("countryCookie", currentCountry);
		languageCookie.setMaxAge(100);
		countryCookie.setMaxAge(100);
		response.addCookie(languageCookie);
		response.addCookie(countryCookie);
		return "about";
	}
	
	/* Set static text based on the chosen language settings */
	public void setStaticText(HttpSession session, String bundlePath, String currentLanguage, String currentCountry)  {
		ResourceBundle resourceBundle;
		resourceBundle = ResourceBundle.getBundle(bundlePath, new Locale(currentLanguage, currentCountry));
		session.setAttribute("welcome", resourceBundle.getString("Welcome_Text"));
		session.setAttribute("about", resourceBundle.getString("About_Text"));
		session.setAttribute("buttons", resourceBundle.getString("Buttons_Text"));
		session.setAttribute("loginText", resourceBundle.getString("Login_Text"));
		session.setAttribute("loginButton", resourceBundle.getString("Login_Button_Text"));
		session.setAttribute("rememberText", resourceBundle.getString("Remember_Text"));
		session.setAttribute("chooseLanguageText", resourceBundle.getString("ChooseLanguage_Text"));
		session.setAttribute("selectText", resourceBundle.getString("Select_Text"));
		session.setAttribute("aboutLink", resourceBundle.getString("About_Link_Text"));
		session.setAttribute("summaryLink", resourceBundle.getString("Summary_Link_Text"));
		session.setAttribute("indexLink", resourceBundle.getString("Index_Link_Text"));
		session.setAttribute("summaryHeading", resourceBundle.getString("Summary_Heading"));
		session.setAttribute("tableHeading", resourceBundle.getString("Table_Heading"));
		session.setAttribute("tableText", resourceBundle.getString("Table_Text"));
		session.setAttribute("idText", resourceBundle.getString("id_text"));
		session.setAttribute("fNameText", resourceBundle.getString("fName_text"));
		session.setAttribute("lNameText", resourceBundle.getString("lName_text"));
		session.setAttribute("addressText", resourceBundle.getString("address_text"));
		session.setAttribute("cityText", resourceBundle.getString("city_text"));
		session.setAttribute("codeText", resourceBundle.getString("code_text"));
		session.setAttribute("countryText", resourceBundle.getString("country_text"));
		session.setAttribute("phoneText", resourceBundle.getString("phone_text"));
		session.setAttribute("emailText", resourceBundle.getString("email_text"));
		session.setAttribute("updateText", resourceBundle.getString("Update_Text"));
		session.setAttribute("deleteText", resourceBundle.getString("Delete_Text"));
		session.setAttribute("insertText", resourceBundle.getString("Insert_Text"));
		session.setAttribute("resetText", resourceBundle.getString("Reset_Text"));
		session.setAttribute("actionText", resourceBundle.getString("Action_Text"));
		session.setAttribute("autofillText", resourceBundle.getString("Autofill_Text"));
		session.setAttribute("fNameError", resourceBundle.getString("fName_Error"));
		session.setAttribute("lNameError", resourceBundle.getString("lName_Error"));
		session.setAttribute("phoneError", resourceBundle.getString("phone_Error"));
		session.setAttribute("emailError", resourceBundle.getString("email_Error"));
		session.setAttribute("deleteConfirm", resourceBundle.getString("delete_Confirm"));
	}
}

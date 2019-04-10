package Imdb_CommonLocators;

import org.openqa.selenium.By;

public class Imdb_Landing_Page_Locators {
	public static final Object movieDropdown = By.cssSelector("#navTitleMenu"),
			topRatedMovies = By.cssSelector("#navMenu1 > div:nth-child(2) > ul:nth-child(2) > li:nth-child(6) > a");
}
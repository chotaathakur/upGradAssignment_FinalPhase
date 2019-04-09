package Imdb_CommonLocators;

import org.openqa.selenium.By;


public class Imdb_SortingPage_Locators {
	public static Object movieDetailsTable = By
			.cssSelector("#main > div > span > div > div > div.lister > table > tbody"),
			noOfMovies=By.tagName("tr"),
			movieTableColumn = By.className("titleColumn"),
			moviePosterColumn = By.className("posterColumn"), 
			movieRankingFetch=By.xpath("./*[@name='rk']"),
			movieTitleFetch = By.xpath("./a"),
			movieReleaseFetch = By.xpath("./span"),
			movieRatingFetch = By.xpath("./*[@name='ir']"),
			movieNoOfVotesFetch= By.xpath("./*[@name='nv']"),
			sortDropdown=By.cssSelector("#lister-sort-by-options"),
			sortButton=By.cssSelector("#main > div > span > div > div > div.lister > div > div > div.controls.float-right.lister-activated > span"),
			releaseDateFull=By.xpath("//*[@title='See more release dates']");
			
}

package upGradAssignment_Imdb_FetchSort;

import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import upGradAssignent_Imdb_CommonLocators.Imdb_Landing_Page_Locators;
import upGradAssignent_Imdb_CommonLocators.Imdb_SortingPage_Locators;

import java.io.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class FetchMovieInformation extends CreateExcel {

	public static WebDriver driver;
	public static WebElement movieTable;
	public static int noOfItems;

	public static String chromedriverpath = "resources/chromedriver/chromedriver.exe";

	public static WebDriver LaunchChrome() {
		System.setProperty("webdriver.chrome.driver", chromedriverpath);
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		return driver;
	}

	public static void createExcel() {

		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("MovieInformation");

		rowhead = sheet.createRow((short) 0);

		cell = rowhead.createCell(0);
		rowhead.getCell(0).setCellValue("Movie Title");
		setBorder(cell);
		setColor(cell);
		cell = rowhead.createCell(1);
		rowhead.getCell(1).setCellValue("Movie Release");
		setBorder(cell);
		setColor(cell);
		cell = rowhead.createCell(2);
		rowhead.getCell(2).setCellValue("Movie Rating");
		setBorder(cell);
		setColor(cell);
	}

	public static void writeExcel(String fetchName) throws IOException {
		String fileName = fetchName;
		String pathName = "D:/" + fileName + ".xls";

		FileOutputStream fileOut = new FileOutputStream(pathName);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
		System.out.println("Please go to the path " + pathName + " to see the fetched results");
	}

	public static void launchIMDBTop250Page() {
		driver = LaunchChrome();
		driver.get("https://www.imdb.com/");
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated((By) Imdb_Landing_Page_Locators.movieDropdown));
		WebElement dropdown = driver.findElement((By) Imdb_Landing_Page_Locators.movieDropdown);
		Actions actions = new Actions(driver);

		actions.moveToElement(dropdown).perform();

		wait.until(ExpectedConditions.elementToBeClickable((By) Imdb_Landing_Page_Locators.topRatedMovies));

		driver.findElement((By) Imdb_Landing_Page_Locators.topRatedMovies).click();
		movieTable = driver.findElement((By) Imdb_SortingPage_Locators.movieDetailsTable);
		noOfItems = movieTable.findElements((By) Imdb_SortingPage_Locators.noOfMovies).size();

	}

	public static void storeMovieTitles() throws IOException {

		List<WebElement> title = driver.findElements((By) Imdb_SortingPage_Locators.movieTable);
		Iterator<WebElement> itr = title.iterator();

		noOfItems = 1;
		while (itr.hasNext() && noOfItems <= 250) {
			WebElement sortIterator = itr.next();
			if (sortIterator.isDisplayed()) {
				String MovieTitleElement = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieTitleFetch)
						.getText();

				rowhead = sheet.createRow((short) noOfItems);
				cell = rowhead.createCell(0);
				rowhead.getCell(0).setCellValue(MovieTitleElement);

				noOfItems++;

			}
		}
		sheet.autoSizeColumn(0);

	}

	public static void storeMovieRelease() throws IOException {
		List<WebElement> releasedate = driver.findElements((By) Imdb_SortingPage_Locators.movieTable);
		Iterator<WebElement> itr = releasedate.iterator();

		noOfItems = 1;
		while (itr.hasNext() && noOfItems <= 250) {
			WebElement sortIterator = itr.next();
			if (sortIterator.isDisplayed()) {

				String MovieReleaseElement = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieReleaseFetch)
						.getText();

				MovieReleaseElement = MovieReleaseElement.replaceAll("\\p{P}", "");

				float MovieReleaseDate = Integer.parseInt(MovieReleaseElement);
				rowhead = sheet.getRow((short) noOfItems);
				cell = rowhead.createCell(1);
				rowhead.getCell(1).setCellValue(MovieReleaseDate);

				noOfItems++;

			}

		}
		sheet.autoSizeColumn(1);
	}

	public static void storeMovieRatings() throws IOException {

		List<WebElement> movieratings = driver.findElements((By) Imdb_SortingPage_Locators.movieRating);
		Iterator<WebElement> itr = movieratings.iterator();

		noOfItems = 1;
		while (itr.hasNext() && noOfItems <= 250) {
			WebElement sortIterator = itr.next();
			if (sortIterator.isDisplayed()) {

				String IMDbMovieRatings = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieRatingFetch)
						.getText();

				rowhead = sheet.getRow((short) noOfItems);
				cell = rowhead.createCell(2);
				rowhead.getCell(2).setCellValue(IMDbMovieRatings);

				noOfItems++;

			}
		}
		sheet.autoSizeColumn(2);
	}

	public static void main(String args[]) {

		createExcel();
		launchIMDBTop250Page();
		try {
			System.out.println("Storing Movie Titles... ");
			storeMovieTitles();
			System.out.println("Movie Titles Stored! ");
			System.out.println("Storing Movie Release Date... ");
			storeMovieRelease();
			System.out.println("Movie Release Date Stored! ");
			System.out.println("Storing Movie Ratings... ");
			storeMovieRatings();
			System.out.println("Movie Ratings Stored!");

			writeExcel("upGradAssignment_MovieStorageInformation");

		} catch (Exception e) {
			System.out.println("Script Failed");
			driver.quit();

		}
		driver.quit();

	}

}

package upGradAssignment_Imdb_FetchSort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import upGradAssignent_Imdb_CommonLocators.Imdb_SortingPage_Locators;

public class SortByMetrics extends FetchMovieInformation {

	public static WebElement DropdownElement;
	public static Select dropdown;
	public static int rankAtCurrentIndexAsc[] = new int[250];
	public static int rankAtNextIndexAsc[] = new int[250];
	public static String sortedMetricStringAscending, sortedMetricStringDescending, stringToAppendPass,
			stringToAppendFail, stringToAppendPassEven, stringToAppendFailEven;

	public static void dropdown() {

		DropdownElement = driver.findElement((By) Imdb_SortingPage_Locators.sortDropdown);
		dropdown = new Select(DropdownElement);

	}

	public static void storeReports(String sortMetricsName) throws IOException {
		String fileName = sortMetricsName;
		String pathName = "D:/" + fileName + ".xls";

		FileOutputStream fileOut = new FileOutputStream(pathName);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
		System.out.println("Please go to the path " + pathName + " to see the fetched results");
		System.out.println();
	}

	public static void sortButton() {
		driver.findElement((By) Imdb_SortingPage_Locators.sortButton).click();
	}

	public static void createExcelSheet() {
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Ascending Rankings");
		rowhead = sheet.createRow((short) 0);

		cell = rowhead.createCell(0);
		rowhead.getCell(0).setCellValue("Ascending Result");
		setBorder(cell);
		setColor(cell);
		cell = rowhead.createCell(1);
		rowhead.getCell(1).setCellValue("Ascending Status");
		setBorder(cell);
		setColor(cell);

		sheet2 = workbook.createSheet("Descending Rankings");
		rowhead2 = sheet2.createRow((short) 0);
		cell2 = rowhead2.createCell(0);
		rowhead2.getCell(0).setCellValue("Descending Result");
		setBorder(cell2);
		setColor(cell2);

		cell2 = rowhead2.createCell(1);
		rowhead2.getCell(1).setCellValue("Descending Status");
		setBorder(cell2);
		setColor(cell2);

	}

	public static void sortByRankings(String sortOrder) throws InterruptedException {
		if (sortOrder == "descending") {
			stringToAppendPass = " is Ranked Lower ";
			stringToAppendFail = " is Ranked Higher ";

			System.out.println("Sort By Rankings Descending will be verified");
			sortButton();
		} else {
			stringToAppendPass = " is Ranked Higher ";
			stringToAppendFail = " is Ranked Lower ";
			System.out.println("Sort By Rankings Ascending will be verified");
		}

		dropdown.selectByVisibleText("Ranking");
		List<WebElement> movieRanking = driver.findElements((By) Imdb_SortingPage_Locators.movieRanking);
		ListIterator<WebElement> itr = movieRanking.listIterator();

		noOfItems = 1;

		while (itr.hasNext() && noOfItems <= 249) {
			WebElement sortIterator = itr.next();
			if (sortIterator.isDisplayed()) {

				String ranking = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieRankingFetch)
						.getAttribute("data-value");

				int currentRankingStore = Integer.parseInt(ranking);
				rankAtCurrentIndexAsc[noOfItems] = currentRankingStore;
				try {
					sortIterator = itr.next();
					String nextElementStorage = sortIterator
							.findElement((By) Imdb_SortingPage_Locators.movieRankingFetch).getAttribute("data-value");
					int nextRankingStore = Integer.parseInt(nextElementStorage);
					rankAtNextIndexAsc[noOfItems] = nextRankingStore;
					SortBase.sortBase(currentRankingStore, nextRankingStore, sortOrder);

				} catch (NoSuchElementException e) {
					itr.previous();
				}

			}
			noOfItems++;
			itr.previous();

		}

	}

	public static void sortByIMDbRatings(String sortOrder) throws InterruptedException {
		dropdown.selectByVisibleText("IMDb Rating");
		if (sortOrder == "ascending") {
			stringToAppendPass = " has lesser IMDB Rating ";
			stringToAppendFail = " has higher IMDB Rating ";
			stringToAppendPassEven = " has same IMDB Rating but is ranked lower ";
			stringToAppendFailEven = " has same IMDB Rating but is ranked higher ";
			System.out.println("Sort Ascending for IMDbRating will be verified");
			sortButton();
		} else {
			stringToAppendPass = " has higher IMDB Rating ";
			stringToAppendFail = " has lesser IMDB Rating ";
			stringToAppendPassEven = " has same IMDB Rating but is ranked higher ";
			stringToAppendFailEven = " has same IMDB Rating but is ranked lower ";
			System.out.println("Sort Descending for IMDbRating will be verified");

		}
		List<WebElement> movieratings = driver.findElements((By) Imdb_SortingPage_Locators.movieRating);
		ListIterator<WebElement> itr = movieratings.listIterator();

		noOfItems = 1;

		while (itr.hasNext() && noOfItems <= 250) {
			WebElement sortIterator = itr.next();
			if (sortIterator.isDisplayed()) {

				String IMDbMovieRatings = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieRatingFetch)
						.getText();

				float CurrentRatingStore = Float.parseFloat(IMDbMovieRatings);
				try {
					WebElement SortCompare = itr.next();
					String nextElementStorage = SortCompare.findElement((By) Imdb_SortingPage_Locators.movieRatingFetch)
							.getText();
					float nextRatingStore = Float.parseFloat(nextElementStorage);
					SortBase.sortBase(CurrentRatingStore, nextRatingStore, sortOrder);
				} catch (NoSuchElementException e) {
					itr.previous();
				}
				noOfItems++;
				itr.previous();

			}
		}
	}

	public static void sortByNoOfRatings(String sortOrder) throws InterruptedException {
		if (sortOrder == "ascending") {
			stringToAppendPass = " has lesser No. of Ratings ";
			stringToAppendFail = " has higher No. of Ratings ";

			System.out.println("Sort Ascending for No Of Ratings will be verified");
			sortButton();
		} else {
			stringToAppendPass = " has higher No. of Ratings ";
			stringToAppendFail = " has lesser No. of Ratings ";
			System.out.println("Sort Descending for No Of Ratings will be verified");
		}

		dropdown.selectByVisibleText("Number of Ratings");

		List<WebElement> noOfRatings = driver.findElements((By) Imdb_SortingPage_Locators.movieRating);
		ListIterator<WebElement> itr = noOfRatings.listIterator();

		noOfItems = 1;

		while (itr.hasNext() && noOfItems <= 250) {
			WebElement sortIterator = itr.next();
			if (sortIterator.isDisplayed()) {

				String TotalRatings = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieRatingFetch)
						.getAttribute("title");

				TotalRatings = TotalRatings.replaceAll("[^0-9]", "");
				TotalRatings = TotalRatings.substring(2);
				int currentNoOfRatingStore = Integer.parseInt(TotalRatings);
				try {
					WebElement SortCompare = itr.next();
					String nextElementStorage = SortCompare.findElement((By) Imdb_SortingPage_Locators.movieRatingFetch)
							.getAttribute("title");
					nextElementStorage = nextElementStorage.replaceAll("[^0-9]", "");
					nextElementStorage = nextElementStorage.substring(2);
					int nextNoOfRatingStore = Integer.parseInt(nextElementStorage);
					SortBase.sortBase(currentNoOfRatingStore, nextNoOfRatingStore, sortOrder);
				} catch (NoSuchElementException e) {
					itr.previous();
				}
				noOfItems++;
				itr.previous();

			}
		}

	}

	public static void sortByReleaseDate(String sortOrder) throws InterruptedException {

		if (sortOrder == "ascending") {
			stringToAppendPass = " was released before ";
			stringToAppendFail = " was released after ";
			stringToAppendPassEven = " was released in the same year but has higher rank ";
			stringToAppendFailEven = " was released in the same year but has lower rank ";
			System.out.println("Sort Ascending for Release Date will be verified");
			sortButton();
		} else {
			stringToAppendPass = " was released after ";
			stringToAppendFail = " was released before ";
			stringToAppendPassEven = " was released in the same year but has lower rank ";
			stringToAppendFailEven = " was released in the same year but has higher rank ";

			System.out.println("Sort Descending for Release Date will be verified");

		}

		dropdown.selectByVisibleText("Release Date");
		List<WebElement> movieReleaseTable = driver.findElements((By) Imdb_SortingPage_Locators.movieTable);
		ListIterator<WebElement> itr = movieReleaseTable.listIterator();

		noOfItems = 1;

		while (itr.hasNext() && noOfItems <= 250) {
			WebElement sortIterator = itr.next();

			if (sortIterator.isDisplayed()) {

				String releaseDate = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieReleaseFetch)
						.getText();
				releaseDate = releaseDate.replaceAll("[^0-9]", "");

				int currentReleaseStore = Integer.parseInt(releaseDate);
				try {
					WebElement SortCompare = itr.next();
					String nextElementStorage = SortCompare
							.findElement((By) Imdb_SortingPage_Locators.movieReleaseFetch).getText();
					nextElementStorage = nextElementStorage.replaceAll("[^0-9]", "");

					int nextReleaseStore = Integer.parseInt(nextElementStorage);

					SortBase.sortBase(currentReleaseStore, nextReleaseStore, sortOrder);

				} catch (NoSuchElementException e) {
					itr.previous();
				}
				noOfItems++;
				itr.previous();

			}
		}

	}

	public static void main(String args[]) {
		//driver = LaunchChrome();
		try {
			launchIMDBTop250Page();
			dropdown();
			dropdown.selectByVisibleText("Release Date");

			createExcelSheet();
			sortByRankings("ascending");
			sortByRankings("descending");
			storeReports("sortByRankings");

			/*
			 * createExcelSheet(); sortByReleaseDate("descending");
			 * sortByReleaseDate("ascending");
			 * 
			 * storeReports("sortByReleaseDate");
			 * 
			 * createExcelSheet(); sortByIMDbRatings("descending");
			 * sortByIMDbRatings("ascending");
			 * 
			 * storeReports("sortByIMDbRatings");
			 * 
			 * createExcelSheet(); sortByNoOfRatings("descending");
			 * sortByNoOfRatings("ascending");
			 * 
			 * storeReports("sortByNoOfRatings");
			 */
			System.out.println("Task Complete");

		} catch (Exception e) {
			System.out.print("Script Failed");
			driver.quit();
		}
		driver.quit();

	}
}

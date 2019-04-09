package Imdb_FetchSort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import java.util.NoSuchElementException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Imdb_CommonLocators.Imdb_SortingPage_Locators;
import Imdb_FetchSort.SortBase;

public class SortByMetrics extends FetchMovieInformation {

	public static WebElement DropdownElement, sortIterator, sortIterator2;
	public static Select dropdown;
	public static int rankAtCurrentIndexAsc[] = new int[300];
	public static int rankAtNextIndexAsc[] = new int[300];
	public static String sortedMetricStringAscending, sortedMetricStringDescending, stringToAppendPass,
			stringToAppendFail, stringToAppendPassEven, stringToAppendFailEven;

	// initializing dropdown
	public static void dropdown() {

		DropdownElement = driver.findElement((By) Imdb_SortingPage_Locators.sortDropdown);
		dropdown = new Select(DropdownElement);

	}

	// will write all the stored data in Excel
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

	// will click the "sort" button
	public static void sortButton() {
		driver.findElement((By) Imdb_SortingPage_Locators.sortButton).click();
	}

	// to create an excel sheet with default columns to store sort data
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

		sheet3 = workbook.createSheet("Sort Order Maintenance");
		rowhead3 = sheet3.createRow((short) 0);
		cell3 = rowhead3.createCell(0);
		rowhead3.getCell(0).setCellValue("Sort Order Maintenance Result");
		setBorder(cell3);
		setColor(cell3);

	}

	// sortByRankings- rank 1 to 250 sort will be verified
	public static void sortByRankings(String sortOrder) throws InterruptedException {
		// pre-determined text to display proper print
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
		SortBase.initialiseAllElementsOfSortTable();

		noOfItems = 1;

		while (SortBase.itr.hasNext() && noOfItems <= 249) {
			sortIterator = SortBase.itr.next();
			sortIterator2 = SortBase.itr2.next();
			if (sortIterator.isDisplayed()) {
				SortBase.findAndStoreAllElementsOfSortTable();
				int currentRankingStore = Integer.parseInt(SortBase.ranking);
				try {
					sortIterator = SortBase.itr.next();
					String nextElementStorage = sortIterator
							.findElement((By) Imdb_SortingPage_Locators.movieRankingFetch).getAttribute("data-value");
					int nextRankingStore = Integer.parseInt(nextElementStorage);
					rankAtNextIndexAsc[noOfItems] = nextRankingStore;
					SortBase.sortBase(currentRankingStore, nextRankingStore, sortOrder);
					SortBase.findFetchedRank();
					SortBase.compareExistingSorting();

				} catch (NoSuchElementException e) {
					SortBase.itr.previous();
				}

			}
			switch (noOfItems) {
			case 50:
				System.out.println("Verified for 50 Elements");

				break;
			case 100:
				System.out.println("Verified for 100 Elements");

				break;
			case 150:
				System.out.println("Verified for 150 Elements");

				break;
			case 200:
				System.out.println("Verified for 200 Elements");

				break;
			default:
				System.out.println("Proceed");
			}
			noOfItems++;
			SortBase.itr.previous();
		}
	}

	// round off function to 1 digit, useful for imdb score verification
	public static double roundToOneDecimal(double currentImdbRankingScore) {
		int decimalPlaces = 1;
		BigDecimal bd = new BigDecimal(currentImdbRankingScore);

		// setScale is immutable
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		currentImdbRankingScore = bd.doubleValue();
		return currentImdbRankingScore;
	}

	// sortByIMDbRatings- all ratings for 1 to 250 sort will be verified
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
		SortBase.initialiseAllElementsOfSortTable();

		noOfItems = 1;

		while (SortBase.itr.hasNext() && noOfItems <= 249) {
			sortIterator = SortBase.itr.next();
			sortIterator2 = SortBase.itr2.next();
			if (sortIterator.isDisplayed()) {
				SortBase.findAndStoreAllElementsOfSortTable();
				float CurrentRatingStore = Float.parseFloat(SortBase.IMDbRating);
				try {
					WebElement SortCompare = SortBase.itr.next();
					String nextElementStorage = SortCompare.findElement((By) Imdb_SortingPage_Locators.movieRatingFetch)
							.getAttribute("data-value");
					float nextRatingStore = Float.parseFloat(nextElementStorage);
					SortBase.sortBase(CurrentRatingStore, nextRatingStore, sortOrder);
					SortBase.findFetchedRank();
					SortBase.compareExistingSorting();
				} catch (NoSuchElementException e) {
					SortBase.itr.previous();
					switch (noOfItems) {
					case 50:
						System.out.println("Verified for 50 Elements");

						break;
					case 100:
						System.out.println("Verified for 100 Elements");

						break;
					case 150:
						System.out.println("Verified for 150 Elements");

						break;
					case 200:
						System.out.println("Verified for 200 Elements");

						break;
					default:
						System.out.println("Proceed");
					}
				}
				noOfItems++;
				SortBase.itr.previous();

			}
		}
	}

	// sortByNoOfRatings- all ratings for movie 1 to 250 sort will be verified
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
		SortBase.initialiseAllElementsOfSortTable();

		noOfItems = 1;

		while (SortBase.itr.hasNext() && noOfItems <= 249) {
			sortIterator = SortBase.itr.next();
			sortIterator2 = SortBase.itr2.next();
			if (sortIterator.isDisplayed()) {
				SortBase.findAndStoreAllElementsOfSortTable();
				float currentNoOfRatingStore = Float.parseFloat(SortBase.MovieNoOfVotes);
				try {
					WebElement SortCompare = SortBase.itr.next();
					String nextElementStorage = SortCompare
							.findElement((By) Imdb_SortingPage_Locators.movieNoOfVotesFetch).getAttribute("data-value");
					int nextNoOfRatingStore = Integer.parseInt(nextElementStorage);
					SortBase.sortBase(currentNoOfRatingStore, nextNoOfRatingStore, sortOrder);
					SortBase.findFetchedRank();
					SortBase.compareExistingSorting();
				} catch (NoSuchElementException e) {
					SortBase.itr.previous();
				}
				switch (noOfItems) {
				case 50:
					System.out.println("Verified for 50 Elements");

					break;
				case 100:
					System.out.println("Verified for 100 Elements");

					break;
				case 150:
					System.out.println("Verified for 150 Elements");

					break;
				case 200:
					System.out.println("Verified for 200 Elements");

					break;
				default:
					System.out.println("Proceed");
				}
				noOfItems++;
				SortBase.itr.previous();

			}
		}

	}

	// sortByReleaseDate- all Release dates for movie 1 to 250 sort will be
	// verified
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
		SortBase.initialiseAllElementsOfSortTable();
		noOfItems = 1;

		while (SortBase.itr.hasNext() && noOfItems <= 249) {
			sortIterator = SortBase.itr.next();
			sortIterator2 = SortBase.itr2.next();
			if (sortIterator.isDisplayed()) {
				SortBase.findAndStoreAllElementsOfSortTable();
				float movieReleaseYear = Float.parseFloat(SortBase.MovieReleaseElement);
				try {

					sortIterator = SortBase.itr.next();
					sortIterator2 = SortBase.itr2.next();
					String nextElementStorage = sortIterator2
							.findElement((By) Imdb_SortingPage_Locators.movieReleaseFetch).getText();
					nextElementStorage = nextElementStorage.replaceAll("[^0-9]", "");

					int nextReleaseStore = Integer.parseInt(nextElementStorage);

					SortBase.sortBase(movieReleaseYear, nextReleaseStore, sortOrder);
					SortBase.findFetchedRank();

				} catch (NoSuchElementException e) {
					SortBase.itr.previous();

				}
				switch (noOfItems) {
				case 50:
					System.out.println("Verified for 50 Elements");

					break;
				case 100:
					System.out.println("Verified for 100 Elements");

					break;
				case 150:
					System.out.println("Verified for 150 Elements");

					break;
				case 200:
					System.out.println("Verified for 200 Elements");

					break;
				default:
					System.out.println("Proceed");
				}
				noOfItems++;
				SortBase.itr.previous();
				SortBase.itr2.previous();

			}
		}
	}

	public static void main(String args[]) {
		try {
			SortBase.fetchDefaultSorting();
			launchIMDBTop250Page();
			dropdown();

			createExcelSheet();
			sortByRankings("ascending");
			sortByRankings("descending");
			storeReports("sortByRankings");

			createExcelSheet();
			sortByReleaseDate("descending");
			sortByReleaseDate("ascending");
			storeReports("sortByReleaseDate");

			createExcelSheet();
			sortByIMDbRatings("descending");
			sortByIMDbRatings("ascending");
			storeReports("sortByIMDbRatings");

			createExcelSheet();
			sortByNoOfRatings("descending");
			sortByNoOfRatings("ascending");
			storeReports("sortByNoOfRatings");

			System.out.println("Task Complete");
		}

		catch (Exception e) {
			System.out.print("Script Failed");
			driver.quit();
		}
		driver.quit();
	}
}

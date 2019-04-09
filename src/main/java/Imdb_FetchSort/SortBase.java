package Imdb_FetchSort;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;
import java.util.ListIterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Imdb_CommonLocators.Imdb_SortingPage_Locators;

public class SortBase extends SortByMetrics {

	public static double storedRank[] = new double[300];
	public static String storedTitle[] = new String[300];
	public static double storedYear[] = new double[300];
	public static double storedIMDbRating[] = new double[300];

	public static double fetchedRank[] = new double[300];
	public static String fetchedTitle[] = new String[300];
	public static double fetchedYear[] = new double[300];
	public static double fetchedIMDbRating[] = new double[300];
	public static List<WebElement> movieRanking, title;
	public static ListIterator<WebElement> itr, itr2;
	public static String availableTestDataDataPath = "resources/testdata/verifySort.xls";
	public static String ranking, IMDbRating, MovieTitleElement, MovieReleaseElement, MovieNoOfVotes;

	public static void fetchDefaultSorting() throws IOException {

		try {
			FileInputStream fileInputStream = new FileInputStream(availableTestDataDataPath);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet("verifySort");

			for (noOfItems = 1; noOfItems <= 250; noOfItems++) {
				HSSFRow row1 = worksheet.getRow(noOfItems);
				HSSFCell cellA1 = row1.getCell((short) 0);
				double a1Val = cellA1.getNumericCellValue();
				storedRank[noOfItems] = a1Val;

				HSSFCell cellB1 = row1.getCell((short) 1);
				String b1Val = cellB1.getStringCellValue();
				storedTitle[noOfItems] = b1Val;

				HSSFCell cellC1 = row1.getCell((short) 2);
				double c1Val = cellC1.getNumericCellValue();
				storedYear[noOfItems] = c1Val;

				HSSFCell cellD1 = row1.getCell((short) 3);
				String d1Val = cellD1.getStringCellValue();
				double d1Value = Float.parseFloat(d1Val);
				d1Value = roundToOneDecimal(d1Value);
				storedIMDbRating[noOfItems] = d1Value;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void findFetchedRank() {
		int toFind = (int) fetchedRank[noOfItems];
		boolean found = false;

		for (double n : storedRank) {
			if (n == toFind) {
				found = true;
				currentFetchedRank = (int) n;
				break;
			}
		}

		if (found) {
			compareExistingSorting();

		} else {
			System.out.println("Fetching rank Failed");
		}

	}

	public static void compareExistingSorting() {

		if (storedTitle[currentFetchedRank].equals(fetchedTitle[noOfItems])
				&& storedYear[currentFetchedRank] == fetchedYear[noOfItems]
				&& storedIMDbRating[currentFetchedRank] == fetchedIMDbRating[noOfItems]) {

			rowhead3 = sheet3.createRow((short) noOfItems);
			cell3 = rowhead3.createCell(0);
			rowhead3.getCell(0)
					.setCellValue("Sorting Order Maintained Properly for Movie: " + fetchedRank[noOfItems] + " "
							+ fetchedTitle[noOfItems] + " with rating " + fetchedIMDbRating[noOfItems]
							+ " with release date ");

		} else {
			rowhead3 = sheet3.createRow((short) noOfItems);
			cell3 = rowhead3.createCell(0);
			rowhead3.getCell(0).setCellValue("Sorting Order not Maintained for Movie: " + fetchedRank[noOfItems] + " "
					+ fetchedTitle[noOfItems] + " with rating " + fetchedIMDbRating[noOfItems] + " with release date ");

		}
	}

	public static void initialiseAllElementsOfSortTable() {
		movieRanking = driver.findElements((By) Imdb_SortingPage_Locators.moviePosterColumn);
		itr = movieRanking.listIterator();
		title = driver.findElements((By) Imdb_SortingPage_Locators.movieTableColumn);
		itr2 = title.listIterator();
	}

	public static void findAndStoreAllElementsOfSortTable() {
		ranking = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieRankingFetch).getAttribute("data-value");

		IMDbRating = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieRatingFetch)
				.getAttribute("data-value");

		MovieTitleElement = sortIterator2.findElement((By) Imdb_SortingPage_Locators.movieTitleFetch).getText();
		MovieReleaseElement = sortIterator2.findElement((By) Imdb_SortingPage_Locators.movieReleaseFetch).getText();
		MovieNoOfVotes = sortIterator.findElement((By) Imdb_SortingPage_Locators.movieNoOfVotesFetch)
				.getAttribute("data-value");
		MovieReleaseElement = MovieReleaseElement.replaceAll("\\p{P}", "");

		int currentRankingStore = Integer.parseInt(ranking);
		double currentIMDbRankingStore = Float.parseFloat(IMDbRating);
		currentIMDbRankingStore = roundToOneDecimal(currentIMDbRankingStore);
		float movieReleaseYear = Float.parseFloat(MovieReleaseElement);

		fetchedRank[noOfItems] = currentRankingStore;
		fetchedIMDbRating[noOfItems] = currentIMDbRankingStore;
		fetchedTitle[noOfItems] = MovieTitleElement;
		fetchedYear[noOfItems] = movieReleaseYear;
		rankAtCurrentIndexAsc[noOfItems] = currentRankingStore;
	}

	public static void sortBase(float currentMetric, float nextMetric, String sortOrder) throws InterruptedException {

		if (currentMetric < nextMetric && sortOrder == "ascending") {
			passScenarioAscending(currentMetric, nextMetric);
		} else if (currentMetric > nextMetric && sortOrder == "descending") {
			passScenarioDescending(currentMetric, nextMetric);
		} else if (currentMetric == nextMetric && sortOrder == "ascending") {
			if (rankAtCurrentIndexAsc[noOfItems] < rankAtNextIndexAsc[noOfItems]) {
				failScenarioAscending(currentMetric, nextMetric);
			} else {
				passScenarioAscending(currentMetric, nextMetric);
			}
		}

		else if (currentMetric == nextMetric && sortOrder == "descending") {
			if (rankAtCurrentIndexAsc[noOfItems] > rankAtNextIndexAsc[noOfItems]) {
				passScenarioDescending(currentMetric, nextMetric);
			} else {
				failScenarioDescending(currentMetric, nextMetric);
			}

		} else if (currentMetric > nextMetric && sortOrder == "ascending") {
			failScenarioAscending(currentMetric, nextMetric);
		} else {
			failScenarioDescending(currentMetric, nextMetric);
		}
	}

	public static void failScenarioAscending(float currentMetricAsc, float nextMetricAsc) {

		rowhead = sheet.createRow((short) noOfItems);
		cell = rowhead.createCell(0);
		if (currentMetricAsc == nextMetricAsc) {
			sortedMetricStringAscending = stringToAppendFailEven;
			rowhead.getCell(0).setCellValue(currentMetricAsc + " " + sortedMetricStringAscending + " " + nextMetricAsc);
		} else {
			sortedMetricStringAscending = stringToAppendFail;
			rowhead.getCell(0).setCellValue(currentMetricAsc + " " + sortedMetricStringAscending + " " + nextMetricAsc);
		}
		rowhead.createCell(1).setCellValue("Fail");
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);

	}

	public static void failScenarioDescending(float currentMetricDesc, float nextMetricDesc) {
		rowhead2 = sheet2.createRow((short) noOfItems);
		cell2 = rowhead2.createCell(0);
		if (currentMetricDesc == nextMetricDesc) {
			sortedMetricStringDescending = stringToAppendFailEven;
			rowhead2.getCell(0)
					.setCellValue(currentMetricDesc + " " + sortedMetricStringDescending + " " + nextMetricDesc);
		} else {
			sortedMetricStringDescending = stringToAppendFail;
			rowhead2.getCell(0)
					.setCellValue(currentMetricDesc + " " + sortedMetricStringDescending + " " + nextMetricDesc);
		}

		rowhead2.createCell(1).setCellValue("Fail");
		sheet2.autoSizeColumn(0);
		sheet2.autoSizeColumn(1);

	}

	public static void passScenarioAscending(float currentRatingStoreAsc, float nextRatingStoreAsc) {
		rowhead = sheet.createRow((short) noOfItems);
		cell = rowhead.createCell(0);
		if (currentRatingStoreAsc == nextRatingStoreAsc) {
			sortedMetricStringAscending = stringToAppendPassEven;
			rowhead.getCell(0)
					.setCellValue(currentRatingStoreAsc + " " + sortedMetricStringAscending + " " + nextRatingStoreAsc);
		} else {
			sortedMetricStringAscending = stringToAppendPass;
			rowhead.getCell(0)
					.setCellValue(currentRatingStoreAsc + " " + sortedMetricStringAscending + " " + nextRatingStoreAsc);
		}
		rowhead.createCell(1).setCellValue("Pass");
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);

	}

	public static void passScenarioDescending(float currentRatingStoreDesc, float nextRatingStoreDesc) {

		rowhead2 = sheet2.createRow((short) noOfItems);
		cell2 = rowhead2.createCell(0);
		if (currentRatingStoreDesc == nextRatingStoreDesc) {
			sortedMetricStringDescending = stringToAppendPassEven;
			rowhead2.getCell(0).setCellValue(
					currentRatingStoreDesc + " " + sortedMetricStringDescending + " " + nextRatingStoreDesc);
		} else {
			sortedMetricStringDescending = stringToAppendPass;
			rowhead2.getCell(0).setCellValue(
					currentRatingStoreDesc + " " + sortedMetricStringDescending + " " + nextRatingStoreDesc);

		}
		rowhead2.createCell(1).setCellValue("Pass");
		sheet2.autoSizeColumn(0);
		sheet2.autoSizeColumn(1);

	}

}

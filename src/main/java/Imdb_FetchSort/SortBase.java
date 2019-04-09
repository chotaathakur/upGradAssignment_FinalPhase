package upGradAssignment_Imdb_FetchSort;


public class SortBase extends SortByMetrics {

	public static float storedRank[]=new float[250];
	public static String storedTitle[]=new String[250];
	public static float storedYear[]=new float[250];
	public static float IMDbRating[]=new float[250];
	
	public static float fetchedRank[]=new float[250];
	public static String fetchedTitle[]=new String[250];
	public static float fetchedYear[]=new float[250];
	public static float fetchedRating[]=new float[250];
	
	public static void verifyExistingSorting(float[] fetchedRank,String[] fetchedTitle,float[] fetchedYear,float[] fetchedRating) {
	
		
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

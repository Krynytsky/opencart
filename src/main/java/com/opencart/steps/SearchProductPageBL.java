package com.opencart.steps;

import com.opencart.pages.ProductContainer;
import com.opencart.pages.SearchProductPage;
import com.opencart.pages.SearchProductResultPage;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import java.util.List;

/*
 * Class that describes steps (Business logic) of searching product
 * with correct search criteria that are located in SearchPhrases enum
 */

public class SearchProductPageBL {

    private SearchProductPage searchProductPage;
    private SearchProductResultPage searchProductResultPage;

    public SearchProductPageBL() {
        searchProductPage = new SearchProductPage();
    }

    public SearchProductPageBL makeSearch(String value) {
        searchProductPage.getInputSearchCriteria().clear();
        searchProductPage.getInputSearchCriteria().sendKeys(value);
        clickOnSearchGroupButton();
        clickOnSelectButton();
        clickOnSearchButton();
        searchProductResultPage = new SearchProductResultPage();
        return this;
    }

    public void verifyPositiveSearchResult(String searchCriteria) {
        SoftAssert softAssert = new SoftAssert();
        String expectedResult = "Products meeting the search criteria";
        String actualResult = searchProductResultPage.getSuccessTitle().getText();
        softAssert.assertEquals(expectedResult, actualResult, "Title is correct, now you can check the list of products");
        softAssert.assertTrue(findAllProductBySearchCriteria(searchCriteria),
                "Check the list of products Done!");
        softAssert.assertAll();
    }

    public void verifyNegativeSearchResult() {
        SoftAssert softAssert = new SoftAssert();
        String expectedResult = "There is no product that matches the search criteria.";
        String actualResult = searchProductResultPage.getNoProductsFoundTitle().getText();
        softAssert.assertEquals(expectedResult, actualResult, "Search criteria is actually correct");
        softAssert.assertAll();
    }

    private boolean findAllProductBySearchCriteria(String searchCriteria) {
        List<ProductContainer> productContainers = searchProductResultPage.getProductContainers();
        boolean isProductListContainsSearchCriteria = true;
        for (ProductContainer productContainer : productContainers) {
            if (!productContainer.getName().contains(searchCriteria)) {
                isProductListContainsSearchCriteria = false;
            }
        }
        return isProductListContainsSearchCriteria;
    }

    private SearchProductPageBL clickOnSearchGroupButton() {
        searchProductPage.getInputSearchGroupButton().click();
        return this;
    }

    private SearchProductPageBL clickOnSearchButton() {
        searchProductPage.getSearchButton().click();
        return this;
    }

    private SearchProductPageBL clickOnSelectButton() {
        Select select = new Select(searchProductPage.getCategoryContainers());
        select.selectByValue("20");

//        Think how to make it more flexible?
//        List<WebElement> optionList = select.getOptions();
//        for (WebElement option : optionList){
//            option.getCssValue("20");
//        }
        return this;
    }
}

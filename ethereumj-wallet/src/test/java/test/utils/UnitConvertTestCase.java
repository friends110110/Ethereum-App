package test.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.ethereum.util.Unit;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

/**
 * 单位转换用例
 * 
 * @author tongsh
 *
 */
public class UnitConvertTestCase {
    private static Logger log = Logger.getLogger(UnitConvertTestCase.class);
    @DataProvider(name = "testDataOne")
    public Object[][] getTestData() {
	return new Object[][]{
		{"1","noether","1"},
		{"1","wei","1"},
		{"1","kwei","1000"},
		{"1","Kwei","1000"},
		{"1","babbage","1000"},
		{"1","femtoether","1000"},
		{"1","mwei","1000000"},
		{"1","Mwei","1000000"},
		{"1","lovelace","1000000"},
		{"1","picoether","1000000"},
		{"1","gwei","1000000000"},
		{"1","Gwei","1000000000"},
		{"1","shannon","1000000000"},
		{"1","nanoether","1000000000"},
		{"1","nano","1000000000"},
		{"1","szabo","1000000000000"},
		{"1","microether","1000000000000"},
		{"1","micro","1000000000000"},
		{"1","finney","1000000000000000"},
		{"1","milliether","1000000000000000"},
		{"1","milli","1000000000000000"},
		{"1","ether","1000000000000000000"},
		{"1","kether","1000000000000000000000"},
		{"1","grand","1000000000000000000000"},
		{"1","mether","1000000000000000000000000"},
		{"1","gether","1000000000000000000000000000"},
		{"1","tether","1000000000000000000000000000000"}
	};
    }

    @Test(dataProvider= "testDataOne")
    public void testStringToWei(String amount, String unit, String expected) {
	BigInteger result = Unit.valueOf(unit).toWei(amount);
	Assert.assertEquals(result.toString(), expected);
    }

    @Test(dataProvider = "testDataOne")
    public void testHexToWei(String amount, String unit, String expected) {
	BigInteger result = Unit.valueOf(unit).toWei("0x" + amount);
	Assert.assertEquals(result.toString(), expected);
    }

    @Test(dataProvider = "decimalData")
    public void testDecimalToWei(String amount, String unit, String expected) {
	BigInteger result = Unit.valueOf(unit).toWei(new BigDecimal(amount));
	Assert.assertEquals(result.toString(), expected);
    }
    @Test(dataProvider = "decimalData")
    public void testFromWeiString(String expected, String unit, String amount) {
	BigDecimal result = Unit.valueOf(unit).fromWei(amount).setScale(3, BigDecimal.ROUND_FLOOR);
	Assert.assertEquals(result.toString(), expected);
    }

    @Test(dataProvider = "decimalData")
    public void testFromWeiHex(String expected, String unit, String amount) {
	String hexString = "0x" + new BigInteger(amount).toString(16);
	BigDecimal result = Unit.valueOf(unit).fromWei(hexString).setScale(3, BigDecimal.ROUND_FLOOR);
	Assert.assertEquals(result.toString(), expected);
    }
        
    @DataProvider(name="decimalData")
    public Object[][] getDecimalData() {
	return new Object[][] {
		{"1.234","kwei","1234"},
		{"1.234","Kwei","1234"},
		{"1.234","babbage","1234"},
		{"1.234","femtoether","1234"},
		{"1.234","mwei","1234000"},
		{"1.234","Mwei","1234000"},
		{"1.234","lovelace","1234000"},
		{"1.234","picoether","1234000"},
		{"1.234","gwei","1234000000"},
		{"1.234","Gwei","1234000000"},
		{"1.234","shannon","1234000000"},
		{"1.234","nanoether","1234000000"},
		{"1.234","nano","1234000000"},
		{"1.234","szabo","1234000000000"},
		{"1.234","microether","1234000000000"},
		{"1.234","micro","1234000000000"},
		{"1.234","finney","1234000000000000"},
		{"1.234","milliether","1234000000000000"},
		{"1.234","milli","1234000000000000"},
		{"1.234","ether","1234000000000000000"},
		{"1.234","kether","1234000000000000000000"},
		{"1.234","grand","1234000000000000000000"},
		{"1.234","mether","1234000000000000000000000"},
		{"1.234","gether","1234000000000000000000000000"},
		{"1.234","tether","1234000000000000000000000000000"}		
	};
    }
}

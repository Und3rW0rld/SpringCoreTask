package com.uw;

import com.uw.util.PasswordGenerator;
import com.uw.util.PasswordGeneratorImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PasswordGeneratorTest {

    @Test
    public void testGeneratePassword_LengthZero() {
        // Arrange
        int digits = 0;
        PasswordGenerator passwordGenerator = new PasswordGeneratorImpl();

        // Act
        String generatedPassword = passwordGenerator.generatePasword(digits);

        // Assert
        assertEquals("", generatedPassword);
    }

    @Test
    public void testGeneratePassword_LengthPositive() {
        // Arrange
        int digits = 8;
        PasswordGenerator passwordGenerator = new PasswordGeneratorImpl();

        // Act
        String generatedPassword = passwordGenerator.generatePasword(digits);

        // Assert
        assertEquals(8, generatedPassword.length());
    }

    @Test
    public void testGeneratePassword_LengthNegative() {
        // Arrange
        int digits = -8;
        PasswordGenerator passwordGenerator = new PasswordGeneratorImpl();

        // Act
        String generatedPassword = passwordGenerator.generatePasword(digits);

        // Assert
        assertEquals("", generatedPassword);
    }

    @Test
    public void testGeneratePassword_Combination() {
        // Arrange
        int digits = 10;
        PasswordGenerator passwordGenerator = new PasswordGeneratorImpl();

        // Act
        String generatedPassword = passwordGenerator.generatePasword(digits);

        // Assert
        assertEquals(true, generatedPassword.matches("[a-zA-Z0-9]+"));
    }
}

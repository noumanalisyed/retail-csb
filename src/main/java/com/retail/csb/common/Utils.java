package com.retail.csb.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;

import com.retail.csb.constant.Constants;

import java.util.TreeMap;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

public class Utils {

    /**
     * Returns the date after a certain time
     *
     * @param date
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return Date
     */
    public static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if (year != 0) {
            cal.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            cal.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            cal.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    public static PageRequest defaultPage(final Integer pageNumber, final Integer pageSize) {
        return PageRequest.of(pageNumber, pageSize);
    }

    public static PageRequest defaultPage(final Integer pageNumber) {
        return PageRequest.of(pageNumber, Constants.pageSize);
    }

    public static PageRequest defaultPage(final Integer pageNumber, final Integer pageSize, final String sortBy) {
        return PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
    }

    public static PageRequest defaultPage(final Integer pageNumber, final Integer pageSize, final String sortBy,
            final String sortDirection) {
        if (sortDirection.toUpperCase().equals("ASC")) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            return PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
    }

    public static String generateRandomCode(int length, boolean isNumeric) {
        // * Chose a character random from this String
        String AlphaNumericString = isNumeric ? "0123456789"
                : "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        // * Create StringBuffer size of AlphaNumericString
        StringBuilder s = new StringBuilder(length);
        int y;
        for (y = 0; y < length; y++) {
            // * Generating a random number
            int index = (int) (AlphaNumericString.length() * Math.random());
            // * Add Character one by one in end of s
            s.append(AlphaNumericString.charAt(index));
        }
        return s.toString();
    }

    /**
     * Convert snake case to camel case
     *
     * @param fieldName Word to be transformed
     * @return Camel cased word
     */
    public static String snakeCaseToCamelCase(String fieldName) {
        var fieldNameArray = fieldName.split("_");

        // * Word does not contain any underscore so we assume its already camel case or
        // * a single word
        if (fieldNameArray.length == 1) {
            return fieldName;
        }

        var returnBuffer = fieldNameArray[0];
        for (int i = 1; i < fieldNameArray.length; ++i) {
            var wordLength = fieldNameArray[i].length();
            returnBuffer += fieldNameArray[i].substring(0, 1).toUpperCase()
                    + fieldNameArray[i].substring(1, wordLength).toLowerCase();
        }
        return returnBuffer.trim();
    }

    /**
     * Helper class that helps to map the fields from one object to another object
     * with same field names
     */
    public static final class Copier {

        /**
         * Method to map the values of private and public field names. It analyzes the
         * passed object and gets the map of the available field names and their values.
         * Then it iterates over those field names and matching field names are assigned
         * the value from source to target object.
         *
         * @param from From object, object from where the fields would be copied
         * @param to   To object, Target/Destination object to where the copied values
         *             will go
         */
        public static void copy(final Object from, final Object to) {
            Map<String, Field> fromFields = analyze(from);
            Map<String, Field> toFields = analyze(to);
            fromFields.keySet().retainAll(toFields.keySet());
            for (Entry<String, Field> fromFieldEntry : fromFields.entrySet()) {
                final String name = fromFieldEntry.getKey();
                final Field sourceField = fromFieldEntry.getValue();
                final Field targetField = toFields.get(name);
                if (targetField.getType().isAssignableFrom(sourceField.getType())) {
                    sourceField.setAccessible(true);
                    if (Modifier.isFinal(targetField.getModifiers()))
                        continue;
                    targetField.setAccessible(true);
                    try {
                        targetField.set(to, sourceField.get(from)); // * Set the value to source object
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * Returns a map of field names that are in an Object along with their values
         *
         * @param object Object to be analyzed
         * @return Map of Field Name, Value
         */
        private static Map<String, Field> analyze(Object object) {
            if (object == null) {
                throw new NullPointerException();
            }

            Map<String, Field> map = new TreeMap<String, Field>();

            Class<?> current = object.getClass();
            while (current != Object.class) {
                for (Field field : current.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        if (!map.containsKey(field.getName())) {
                            map.put(field.getName(), field);
                        }
                    }
                }
                current = current.getSuperclass();
            }
            return map;
        }
    }

    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
